package com.hotel.flint.reserve.room.controller;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.flint.reserve.room.dto.RoomReservedDetailDto;
import com.hotel.flint.reserve.room.dto.RoomReservedDto;
import com.hotel.flint.reserve.room.dto.RoomReservedListDto;
import com.hotel.flint.user.employee.domain.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@RestController
@RequestMapping("/room")
public class RoomSSEController implements MessageListener {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    // 여러번 구독 방지
    private Set<String> subscribeList = ConcurrentHashMap.newKeySet();

    @Qualifier("6")
    private final RedisTemplate<String, Object> sseRedisTemplate;
    private final RedisMessageListenerContainer redisMessageListenerContainer;

    public RoomSSEController(@Qualifier("6") RedisTemplate<String, Object> sseRedisTemplate, RedisMessageListenerContainer redisMessageListenerContainer) {
        this.sseRedisTemplate = sseRedisTemplate;
        this.redisMessageListenerContainer = redisMessageListenerContainer;
    }

    /**
     *
     */
    public void subscribeChannel(String email) {

        // 이미 구독한 email 더이상 구독하지 않도록 분기처리
        if (!subscribeList.contains(email)) {
            MessageListenerAdapter listenerAdapter = createListenerAdapter(this);
            redisMessageListenerContainer.addMessageListener(listenerAdapter, new PatternTopic(email));
            subscribeList.add(email); // 다시 구독 못하도록
        }
    }
    private MessageListenerAdapter createListenerAdapter(RoomSSEController roomSSEController) {
        return new MessageListenerAdapter(roomSSEController, "onMessage");
    }


    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(14400 * 60 * 1000L);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        emitters.put(email, emitter);

        emitter.onCompletion(() -> emitters.remove(email));
        emitter.onTimeout(() -> emitters.remove(email));

        try {
            emitter.send(SseEmitter.event().name("connect").data("연결되었습니다."));
        } catch (IOException e) {
            e.printStackTrace();
        }

        subscribeChannel(email);

        return emitter;
    }

    // publish => redis에 서버가 메시지 publish
    public void publishMessage(RoomReservedDetailDto dto, List<Employee> roomEmployeeList) {

        for (Employee employee: roomEmployeeList) {
            SseEmitter emitter = emitters.get(employee.getEmail());

            if (emitter != null) {
                log.info("hihihi");
                try {
                    emitter.send(SseEmitter.event().name("booked").data(dto));

                } catch (IOException e) {
                    log.error("SSE 실패. Redis에 publish하기 => {}: {}", employee.getEmail(), e.getMessage());
                }
            }
            log.info("Redis로 publishing (for employee): {}", employee.getEmail());
            sseRedisTemplate.convertAndSend(employee.getEmail(), dto);
        }
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {

        // message 내용 parsing
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            RoomReservedDetailDto dto = objectMapper.readValue(message.getBody(), RoomReservedDetailDto.class);
            log.info("listen 하는 중");
            log.info("dto: {}", dto);

            String email = new String(pattern, StandardCharsets.UTF_8);
            SseEmitter emitter = emitters.get(email);

            if (emitter != null) {
                emitter.send(SseEmitter.event().name("booked").data(dto));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
