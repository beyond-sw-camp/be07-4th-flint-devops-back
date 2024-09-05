package com.hotel.flint.common.service;

import com.hotel.flint.common.configs.RabbitMqConfig;
import com.hotel.flint.common.controller.SSEController;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RequestQueueProcessor {

    @Autowired
    private RequestQueueManager queueManager;

    @Autowired
    private SSEController sseController;

    @Autowired
    @Qualifier("3")
    private RedisTemplate<String, Object> redisTemplate;

    private static final String EMAIL_HASH_KEY = "RequestEmailMapping";

    @Transactional
    @RabbitListener(queues = RabbitMqConfig.WAITING_LIST_QUEUE)
    public void processRequest(String requestId) {
        try {
            List<Object> emailList = redisTemplate.opsForHash().values(EMAIL_HASH_KEY);

            for(Object email : emailList){
                String userEmail = email.toString();
                sseController.publishMessage(userEmail);
            }
            // 처리 완료된 후 큐에서 요청 제거
            queueManager.removeRequest(requestId);
        } catch (Exception e) {
            // 에러 발생 시 예외 처리
            System.err.println("Error processing request ID: " + requestId);
            e.printStackTrace();
            // 예외를 던져 Spring의 재시도 메커니즘을 트리거
            throw e;
        }
        // 시연을 위해 0.1초마다 메시지 소모
//        Thread.sleep(100);
    }
}