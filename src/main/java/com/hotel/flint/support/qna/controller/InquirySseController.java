package com.hotel.flint.support.qna.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/inquiry")
public class InquirySseController implements MessageListener {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Set<String> subscribeList = ConcurrentHashMap.newKeySet();

    @Qualifier("7")
    private final RedisTemplate<String, Object> sseRedisTemplate;
    private final RedisMessageListenerContainer redisMessageListenerContainer;

//    Qualifier 수정 안되어 있던 것 수정
    public InquirySseController(@Qualifier("7") RedisTemplate<String, Object> sseRedisTemplate, RedisMessageListenerContainer redisMessageListenerContainer) {
        this.sseRedisTemplate = sseRedisTemplate;
        this.redisMessageListenerContainer = redisMessageListenerContainer;
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(14400 * 60 * 1000L);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        emitters.put(email, emitter);
        emitter.onCompletion(() -> emitters.remove(email));
        emitter.onTimeout(() -> emitters.remove(email));

        try {
            emitter.send(SseEmitter.event().name("connect").data("connected!!"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        subscribeChannel(email);
        return emitter;
    }

    public void subscribeChannel(String email) {
        if (!subscribeList.contains(email)) {
            MessageListenerAdapter listenerAdapter = createListenerAdapter(this);
            redisMessageListenerContainer.addMessageListener(listenerAdapter, new PatternTopic(email));
            subscribeList.add(email);
        }
    }

    private MessageListenerAdapter createListenerAdapter(InquirySseController sseController) {
        return new MessageListenerAdapter(sseController, "onMessage");
    }

    public void publishQnaMessage(String message, String email) {
//        메시지 내용이 null 이 아닐 때만 발행
        if (message != null && !message.trim().isEmpty()) {
            SseEmitter emitter = emitters.get(email);
            if (emitter != null) {
                try {
                    emitter.send(SseEmitter.event().name("answer").data(message));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                sseRedisTemplate.convertAndSend(email, message);
            }
        }
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {
        String email = new String(pattern, StandardCharsets.UTF_8);
        SseEmitter emitter = emitters.get(email);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("answer").data(new String(message.getBody())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
