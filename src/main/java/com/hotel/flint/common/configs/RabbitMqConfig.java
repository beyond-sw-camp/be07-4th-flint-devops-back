package com.hotel.flint.common.configs;

import com.hotel.flint.common.service.RequestQueueManager;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Autowired
    private RequestQueueManager requestQueueManager;

    @Autowired
    private ConnectionFactory connectionFactory;

    public static final String WAITING_LIST_QUEUE = "waitingListQueue";

//    @Value("${spring.rabbitmq.host}")
//    private String host;
//
//    @Value("${spring.rabbitmq.port}")
//    private int port;
//
//    @Value("${spring.rabbitmq.username}")
//    private String username;
//
//    @Value("${spring.rabbitmq.password}")
//    private String password;
//
//    @Value("${spring.rabbitmq.virtual-host}")
//    private String virtualHost;


    /**
     * 큐가 최초로 생성될 때 Redis의 대기열 정보를 모두 제거
     */
    @Bean
    public Queue waitingListQueue() {
        Queue queue = new Queue(WAITING_LIST_QUEUE, true);

        // 큐가 생성될 때 Redis의 기존 대기열 데이터를 모두 삭제
        if (!isQueueExists(WAITING_LIST_QUEUE)) {
            requestQueueManager.removeAll();  // Redis 데이터 초기화
        }

        return queue;
    }

    /**
     * RabbitMQ 큐가 이미 존재하는지 확인하는 메서드
     */
    public boolean isQueueExists(String queueName) {
        try {
            return rabbitAdmin(connectionFactory).getQueueProperties(queueName) != null;
        } catch (Exception e) {
            return false; // 큐가 없으면 false를 반환
        }
    }

    /**
     * 큐가 존재하지 않으면 새로 생성하는 메서드
     */
    public void createQueueIfNotExists(String queueName) {
        if (!isQueueExists(queueName)) {
            // 큐 생성
            Queue queue = new Queue(queueName, true); // 큐가 영구적으로 저장되도록 설정
            rabbitAdmin(connectionFactory).declareQueue(queue);

            // Redis의 기존 대기열 데이터를 모두 삭제
            requestQueueManager.removeAll();  // Redis 데이터 초기화
        }
    }

//    @Bean
//    public ConnectionFactory connectionFactory(){
//        CachingConnectionFactory factory = new CachingConnectionFactory();
//        factory.setHost(host);
//        factory.setPort(port);
//        factory.setUsername(username);
//        factory.setPassword(password);
//        factory.setVirtualHost(virtualHost);
//        return factory;
//    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter Jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
