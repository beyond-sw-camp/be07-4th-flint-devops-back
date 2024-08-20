package com.hotel.flint.common.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class SSEController implements MessageListener {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Set<String> subscribeList = ConcurrentHashMap.newKeySet();

    @Qualifier("2")
    private final RedisTemplate<String, Object> sseRedisTemplate;

    private final RedisMessageListenerContainer redisMessageListenerContainer;

    public SSEController(@Qualifier("2") RedisTemplate<String, Object> sseRedisTemplate, RedisMessageListenerContainer redisMessageListenerContainer) {
        this.sseRedisTemplate = sseRedisTemplate;
        this.redisMessageListenerContainer = redisMessageListenerContainer;
    }

    // 채널을 구독하고 메시지 리스너를 설정
    public void subscribeChannel(String email){
        if(!subscribeList.contains(email)){
            System.out.println("채널 구독 시작: " + email); // 구독 시작 로그 추가
            MessageListenerAdapter listenerAdapter = createListenerAdapter(this);
            redisMessageListenerContainer.addMessageListener(listenerAdapter, new PatternTopic(email));
            subscribeList.add(email);
        } else {
            System.out.println("이미 구독 중인 채널: " + email); // 이미 구독 중인 경우 로그 추가
        }
    }

    // MessageListenerAdapter를 생성하는 메서드
    private MessageListenerAdapter createListenerAdapter(SSEController sseController){
        return new MessageListenerAdapter(sseController, "onMessage");
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribe(@RequestParam String email) {
        SseEmitter emitter = new SseEmitter(14400 * 60 * 1000L); // 4시간 유지되는 SSE 연결

//        System.out.println("SSE 연결 요청: " + email);
        emitters.put(email, emitter);

        emitter.onCompletion(() -> {
            emitters.remove(email);
//            System.out.println("SSE 연결 종료: " + email);
        });
        emitter.onTimeout(() -> {
            emitters.remove(email);
//            System.out.println("SSE 연결 타임아웃: " + email);
        });
        emitter.onError((e) -> {
            emitters.remove(email);
//            System.out.println("SSE 연결 오류: " + email);
        });

        try {
            emitter.send(SseEmitter.event().name("connected").data("대기열에 들어왔습니다."));
        } catch (IOException e) {
            e.printStackTrace();
        }

        subscribeChannel(email);

        return emitter;
    }

    // 새로운 대기 위치를 클라이언트에 전송하는 메서드
    public void publishMessage(String email) {
        SseEmitter emitter = emitters.get(email);
        if (emitter != null) {
            try {
                // 새로운 대기 번호를 클라이언트에 전송
                emitter.send(SseEmitter.event().name("갱신 완료").data("대기열 갱신 됨"));
            } catch (IOException e) {
                // 클라이언트 연결이 끊어진 경우, emitters에서 제거하고 예외를 기록
                emitters.remove(email);
                System.out.println("SSE 연결이 끊어졌습니다: " + email);
            }
        } else {
            // 클라이언트가 연결되어 있지 않으면 Redis 메시지로 전송
            sseRedisTemplate.convertAndSend(email, String.valueOf(""));
        }
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String email = new String(pattern); // Redis 채널 이름에서 이메일을 가져옴
//            String newPositionStr = new String(message.getBody()); // 메시지에서 새 대기 번호 가져옴
//            int newPosition = Integer.parseInt(newPositionStr); // 새 대기 번호를 정수로 변환

            // 메시지 수신 후, 해당 사용자의 대기열 위치를 클라이언트에게 전송
            publishMessage(email);
//            publishPositionMessage(email, newPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    // 새로운 대기 위치를 클라이언트에 전송하는 메서드
//    public void publishPositionMessage(String email, int newPosition) {
//        SseEmitter emitter = emitters.get(email);
//        if (emitter != null) {
//            try {
//                // 새로운 대기 번호를 클라이언트에 전송
//                emitter.send(SseEmitter.event().name("현재 위치").data("나의 대기 순서: " + newPosition + "번째"));
//            } catch (IOException e) {
//                // 클라이언트 연결이 끊어진 경우, emitters에서 제거하고 예외를 기록
//                emitters.remove(email);
//                System.out.println("SSE 연결이 끊어졌습니다: " + email);
//            }
//        } else {
//            // 클라이언트가 연결되어 있지 않으면 Redis 메시지로 전송
//            sseRedisTemplate.convertAndSend(email, String.valueOf(newPosition));
//        }
//    }
}