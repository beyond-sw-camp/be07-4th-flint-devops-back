package com.hotel.flint.reserve.dining.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.flint.reserve.dining.dto.ReservationDetailDto;
import com.hotel.flint.reserve.dining.dto.ReservationSseDetailDto;
import com.hotel.flint.user.employee.domain.Employee;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/dining")
public class DiningSseController implements MessageListener {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    private Set<String> subscribeList =ConcurrentHashMap.newKeySet();

    @Qualifier("5")
    private final RedisTemplate<String, Object> sseRedisTemplat;
    private final RedisMessageListenerContainer redisMessageListenerContainer;

    public DiningSseController(@Qualifier("5") RedisTemplate<String, Object> sseRedisTemplat, RedisMessageListenerContainer redisMessageListenerContainer) {
        this.sseRedisTemplat = sseRedisTemplat;
        this.redisMessageListenerContainer = redisMessageListenerContainer;
    }

    public void subscribeChannel(String email){
        if(!subscribeList.contains(email)) {
            MessageListenerAdapter listenerAdapter = createListenerAdapter(this);
            redisMessageListenerContainer.addMessageListener(listenerAdapter, new PatternTopic(email));
            subscribeList.add(email);
        }
        System.out.println("Subscribed to Redis channel: " + email);
    }

    private MessageListenerAdapter createListenerAdapter(DiningSseController sseController){
        return new MessageListenerAdapter(sseController, "onMessage");
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribe(){
        SseEmitter emitter = new SseEmitter(14400*60*1000L); // emmiter 유효시간 30분
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        emitters.put(email, emitter);
        emitter.onCompletion(()-> emitters.remove(email));
        emitter.onTimeout(() -> emitters.remove(email));
        try{
            emitter.send(SseEmitter.event().name("connect").data("dining Reservation"));
        }catch (IOException e){
            e.printStackTrace();
        }
        subscribeChannel(email);
        return emitter;
    }

    public void publishMessage(ReservationSseDetailDto dto, List<Employee> diningEmployeeList){
        for(Employee employee : diningEmployeeList){
            sseRedisTemplat.convertAndSend(employee.getEmail(), dto);
        }

    }


    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("Received message from Redis: " + message);
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            ReservationSseDetailDto dto = objectMapper.readValue(message.getBody(), ReservationSseDetailDto.class);
            System.out.println("!" + dto);
            String email = new String(pattern, StandardCharsets.UTF_8);
            SseEmitter emitter = emitters.get(email);
            if(emitter != null){
                emitter.send(SseEmitter.event().name("reserved").data(dto));
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
