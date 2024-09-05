package com.hotel.flint.common.service;

import com.hotel.flint.common.controller.SSEController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Component
@Slf4j
public class RequestQueueManager {

    private static final String QUEUE_KEY = "requestQueue"; // Redis List에 저장될 대기열
    private static final String POSITION_HASH_KEY = "requestQueuePosition"; // Redis Hash에 저장될 요청 ID별 위치 정보
    private static final String EMAIL_HASH_KEY = "RequestEmailMapping"; // Redis Hash에 저장될 요청 ID와 이메일 매핑 정보

    @Autowired
    @Qualifier("3")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SSEController sseController;

    // Lua 스크립트를 사용하여 Redis에서 원자적(다른 명령어가 중간에 끼어들 수 없음)으로 데이터 추가 및 위치 계산을 수행
    private static final String LUA_SCRIPT =
            "redis.call('RPUSH', KEYS[1], ARGV[1]); " + // 요청 ID를 List에 추가
                    "local position = redis.call('LLEN', KEYS[1]); " + // List의 길이를 계산(LLEN)하여 대기열 내 위치를 구함
                    "redis.call('HSET', KEYS[2], ARGV[1], tostring(position)); " +  // 계산된 위치를 Hash에 저장(HSET 명령어)
                    "return position;"; // 위치를 반환

    // Lua 스크립트 객체 생성. 스크립트와 반환 타입을 설정
    private final DefaultRedisScript<Long> script = new DefaultRedisScript<>(LUA_SCRIPT, Long.class);

    // 요청을 큐에 추가하고 requestId와 email을 Redis에 저장
    public String addRequest(String email) {
        String requestId = UUID.randomUUID().toString(); // 고유한 요청 ID 생성
        // Lua 스크립트를 실행하여 원자적으로 요청 ID를 추가하고 위치를 계산
        try {
            // KEYS[1] = QUEUE_KEY, KEYS[2] = POSITION_HASH_KEY, ARGV[1] = requestId
            Long position = redisTemplate.execute((RedisCallback<Long>) redisConnection ->
                    redisTemplate.execute(script, Arrays.asList(QUEUE_KEY, POSITION_HASH_KEY), requestId)
            );

            if (position != null) {
                // requestId와 email을 Redis Hash에 저장
                redisTemplate.opsForHash().put(EMAIL_HASH_KEY, requestId, email);
//                log.info("REQUEST_ID {} 위치 {}로 저장됨", requestId, position);
                return requestId; // 요청 ID 반환
            } else {
//                log.error("요청 ID 생성 중 오류 발생");
                throw new RuntimeException("요청 ID 생성 중 오류 발생");
            }
        } catch (Exception e) {
//            log.error("대기열에 요청 추가 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("대기열에 요청 추가 중 오류 발생: " + e.getMessage(), e);
        }
    }

    // 요청 ID의 큐 내 위치를 반환하는 메서드
    public int getPositionInQueue(String requestId) {
        try {
            // 요청 ID에 해당하는 위치를 Hash에서 가져옴
            String positionStr = (String) redisTemplate.opsForHash().get(POSITION_HASH_KEY, requestId);
            if (positionStr != null) {
                return Integer.parseInt(positionStr); // 위치를 정수로 변환하여 반환
            } else {
//                log.error("POSITION_HASH_KEY에서 요청 ID 조회 실패: {}", requestId);
                throw new RuntimeException("POSITION_HASH_KEY에서 요청 ID 조회 실패: " + requestId);
            }
        } catch (Exception e) {
//            log.error("대기열 위치 조회 중 오류 발생: {}", e.getMessage(), e);
            // 예외 발생 시 로그 출력 및 재던지기
            throw new RuntimeException("대기열 위치 조회 중 오류 발생: " + e.getMessage(), e);
        }
    }

    // 요청 ID를 큐에서 제거하고 매핑된 이메일 정보도 삭제
    public void removeRequest(String requestId) {
        // 입력된 요청 ID에서 불필요한 따옴표를 제거 -> requestID가 "requestID" 형식으로 넘어옴(redis 상에는 ""없는 상태)
        String cleanedRequestId = requestId.replace("\"", "");

        try {
            // 트랜잭션을 통해 원자적으로 요청 ID를 제거
            redisTemplate.execute((RedisCallback<Object>) connection -> {
                // 대기열 리스트를 감시(갱신하는 동안 새로운 요청 들어올 수 있음)
                connection.watch(redisTemplate.getStringSerializer().serialize(QUEUE_KEY));
                // 트랜잭션 시작
                connection.multi();

                // Redis List에서 요청 ID 제거
                connection.lRem(redisTemplate.getStringSerializer().serialize(QUEUE_KEY), 1,
                        redisTemplate.getStringSerializer().serialize(cleanedRequestId));

                // POSITION_HASH_KEY에서 해당 요청 ID 삭제
                connection.hDel(redisTemplate.getStringSerializer().serialize(POSITION_HASH_KEY),
                        redisTemplate.getStringSerializer().serialize(cleanedRequestId));

                // EMAIL_HASH_KEY에서 해당 요청 ID와 연결된 이메일 삭제
                connection.hDel(redisTemplate.getStringSerializer().serialize(EMAIL_HASH_KEY),
                        redisTemplate.getStringSerializer().serialize(cleanedRequestId));

                // 트랜잭션 실행
                connection.exec();
//            log.info("REMOVE_REQUEST 트랜잭션 완료 - REQUEST_ID: {}", cleanedRequestId);
                return null;
            });

        } catch (Exception e) {
//        log.error("요청 ID 제거 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("요청 ID 제거 중 오류 발생: " + e.getMessage(), e);
        }
    }

//    // 대기열 업데이트 메서드 (2초마다 실행)
//    @Scheduled(fixedRate = 2000)
//    public void refreshPositions() {
//        try {
//            // 현재 Redis에 남아있는 대기열(List)을 가져옴
//            List<Object> queue = redisTemplate.opsForList().range(QUEUE_KEY, 500, -1);
//            if (queue != null) {
//                // 트랜잭션을 통해 원자적으로 위치를 업데이트
//                redisTemplate.execute((RedisCallback<Object>) connection -> {
//                    // 대기열 리스트를 감시(갱신하는 동안 새로운 요청 들어올 수 있음)
//                    connection.watch(redisTemplate.getStringSerializer().serialize(QUEUE_KEY));
//                    connection.watch(redisTemplate.getStringSerializer().serialize(EMAIL_HASH_KEY));
//
//                    // 트랜잭션 시작
//                    connection.multi();
//
//                    // 큐에 남아있는 모든 요청 ID의 위치를 재계산하여 갱신
//                    for (int i = 0; i < queue.size(); i++) {
//                        String requestId = queue.get(i).toString();
//
//                        // 요청 ID로 이메일을 조회
//                        String email = (String) redisTemplate.opsForHash().get(EMAIL_HASH_KEY, requestId);
//
//                        if (email == null) {
//                            // 이메일을 찾을 수 없는 경우 해당 요청 ID를 제거하고 다음으로 넘어감
//                            System.err.println("해당 요청 ID에 대한 이메일을 찾을 수 없습니다: " + requestId);
//                            connection.lRem(redisTemplate.getStringSerializer().serialize(QUEUE_KEY), 1,
//                                    redisTemplate.getStringSerializer().serialize(requestId));
//                            connection.hDel(redisTemplate.getStringSerializer().serialize(POSITION_HASH_KEY),
//                                    redisTemplate.getStringSerializer().serialize(requestId));
//                            continue;
//                        }
//
//                        int newPosition = i + 1;
//
//                        // 위치 정보를 일관되게 유지하기 위해 매번 위치를 갱신
//                        connection.hSet(redisTemplate.getStringSerializer().serialize(POSITION_HASH_KEY),
//                                redisTemplate.getStringSerializer().serialize(requestId),
//                                redisTemplate.getStringSerializer().serialize(String.valueOf(newPosition)));
//
//                        // SSE를 통해 위치 변동을 클라이언트에 전송
//                        sseController.publishPositionMessage(email, newPosition);
////                    log.info("REQUEST_ID {} 위치 {}로 업데이트 됨", requestId, newPosition);
//                    }
//
//                    // 트랜잭션 실행
//                    connection.exec();
//                    return null;
//                });
//            }
//        } catch (Exception e) {
////        log.error("대기열 위치 업데이트 중 오류 발생: {}", e.getMessage(), e);
//            throw new RuntimeException("대기열 위치 업데이트 중 오류 발생: " + e.getMessage(), e);
//        }
//    }

//    @Scheduled(fixedRate = 20000)
//    public void refreshPositions() {
//        try {
//            // 현재 Redis에 남아있는 대기열(List)을 가져옴
//            List<Object> queue = redisTemplate.opsForList().range(QUEUE_KEY, 0, -1);
//            if (queue != null) {
//                // 트랜잭션을 통해 원자적으로 위치를 업데이트
//                redisTemplate.execute((RedisCallback<Object>) connection -> {
//                    // 대기열 리스트를 감시(갱신하는 동안 새로운 요청 들어올 수 있음)
//                    connection.watch(redisTemplate.getStringSerializer().serialize(QUEUE_KEY));
//
//                    // 트랜잭션 시작
//                    connection.multi();
//
//                    // 큐에 남아있는 모든 요청 ID의 위치를 재계산하여 갱신
//                    for (int i = 0; i < queue.size(); i++) {
//                        String requestId = queue.get(i).toString();
//                        String email = null;
//
//                        try {
//                            email = getEmailByRequestId(requestId);
//                        } catch (IllegalArgumentException e) {
//                            // 이메일을 찾을 수 없는 경우 해당 요청 ID를 제거하고 다음으로 넘어감
//                            System.err.println("해당 요청 ID에 대한 이메일을 찾을 수 없습니다: " + requestId);
//                            connection.lRem(redisTemplate.getStringSerializer().serialize(QUEUE_KEY), 1,
//                                    redisTemplate.getStringSerializer().serialize(requestId));
//                            connection.hDel(redisTemplate.getStringSerializer().serialize(POSITION_HASH_KEY),
//                                    redisTemplate.getStringSerializer().serialize(requestId));
//                            continue;
//                        }
//
//                        int newPosition = i + 1;
//
//                        // 위치 정보를 일관되게 유지하기 위해 매번 위치를 갱신
//                        connection.hSet(redisTemplate.getStringSerializer().serialize(POSITION_HASH_KEY),
//                                redisTemplate.getStringSerializer().serialize(requestId),
//                                redisTemplate.getStringSerializer().serialize(String.valueOf(newPosition)));
//
//                        // SSE를 통해 위치 변동을 클라이언트에 전송
//                        sseController.publishPositionMessage(email, newPosition);
////                    log.info("REQUEST_ID {} 위치 {}로 업데이트 됨", requestId, newPosition);
//                    }
//
//                    // 트랜잭션 실행
//                    connection.exec();
//                    return null;
//                });
//            }
//        } catch (Exception e) {
////        log.error("대기열 위치 업데이트 중 오류 발생: {}", e.getMessage(), e);
//            throw new RuntimeException("대기열 위치 업데이트 중 오류 발생: " + e.getMessage(), e);
//        }
//    }

    // 요청 ID로 이메일을 조회하는 메서드
//    private String getEmailByRequestId(String requestId) {
//        return redisTemplate.execute((RedisCallback<String>) connection -> {
//            connection.watch(redisTemplate.getStringSerializer().serialize(EMAIL_HASH_KEY));
//
//            // 트랜잭션 시작
//            connection.multi();
//
//            // EMAIL_HASH_KEY에서 requestId로 이메일 조회
//            String email = (String) redisTemplate.opsForHash().get(EMAIL_HASH_KEY, requestId);
//
//            // 트랜잭션 실행
//            connection.exec();
//
//            if (email != null) {
//                return email; // 이메일 반환
//            } else {
////                log.error("해당 요청 ID에 대한 이메일을 찾을 수 없습니다: {}", requestId);
//                throw new IllegalArgumentException("해당 요청 ID에 대한 이메일을 찾을 수 없습니다: " + requestId);
//            }
//        });
//    }
}