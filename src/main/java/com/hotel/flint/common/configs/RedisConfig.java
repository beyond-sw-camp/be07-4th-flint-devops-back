package com.hotel.flint.common.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    public String host;

    @Value("${spring.redis.port}")
    public int port;

    // Redis 3번 DB 사용
    @Bean
    @Qualifier("4")
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        // 3번 db 사용
        configuration.setDatabase(3);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    @Qualifier("4")
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("redisConnectionFactory4") RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
  
  // 황정하
    // Redis 2번 DB 사용
    @Bean
    @Qualifier("3")
    public RedisConnectionFactory waitingListFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(2); // 2번 DB 사용
        return new LettuceConnectionFactory(configuration);
    }

    // 대기열 전용 RedisTemplate 설정
    @Bean
    @Qualifier("3")
    public RedisTemplate<String, Object> waitingListRedisTemplate(@Qualifier("3") RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer()); // 여기서 StringRedisSerializer로 설정
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    // 1번 DB 사용
    @Bean
    @Qualifier("2")
    public RedisConnectionFactory sseFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(1);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    @Qualifier("2")
    public RedisTemplate<String, Object> sseRedisTemplate(@Qualifier("2") RedisConnectionFactory sseFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        serializer.setObjectMapper(objectMapper);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setConnectionFactory(sseFactory);
        return redisTemplate;
    }

    // 리스너 객체
    @Bean
    @Qualifier("2")
    public RedisMessageListenerContainer redisMessageListenerContainer(@Qualifier("2") RedisConnectionFactory sseFactory){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(sseFactory);
        return container;
    }
  // --- 황정하
  
  // --- 이혜진
    @Bean(name = "redisConnectionFactory4")
    public RedisConnectionFactory redisQnaConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        // 3번 db 사용 -> 기존 3번에서 6번으로 바꿈.
        configuration.setDatabase(6);
        return new LettuceConnectionFactory(configuration);
    }

    // Qualifier("4")와 연결된 RedisTemplate -> 기존 4에서 7로 바꿈.
    @Bean
    @Qualifier("7")
    public RedisTemplate<String, Object> redisQnaTemplate(@Qualifier("redisConnectionFactory4") RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    // RedisMessageListenerContainer도 동일한 RedisConnectionFactory를 사용하도록 설정
    @Bean
    public RedisMessageListenerContainer redisQnaMessageListenerContainer(@Qualifier("redisConnectionFactory4") RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        return container;
    }
// --- 이혜진
// --- 홍예지
  
    /**
     * Redis - pub/sub 구현 (객실 예약 알림용)
     */
    @Bean
    @Qualifier("6")
    public RedisConnectionFactory sseRoomFactory() {

        // connection 정보 넣기
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(5); // db 5번 사용

        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    @Qualifier("6")
    public RedisTemplate<String, Object> sseRoomRedisTemplate(@Qualifier("6") RedisConnectionFactory sseFactory) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 직렬화
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());
        serializer.setObjectMapper(objectMapper);
        redisTemplate.setValueSerializer(serializer);

        redisTemplate.setConnectionFactory(sseFactory);

        return redisTemplate;
    }

    /**
     * 리스너 객체 생성
     */
    @Bean
    @Qualifier("6")
    public RedisMessageListenerContainer redisRoomMessageListenerContainer(@Qualifier("6") RedisConnectionFactory sseFactory) {
      RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(sseFactory);
        return container;
    }
// --- 홍예지
// --- 최무리
  
    @Bean
    @Qualifier("5")
    public RedisConnectionFactory sseDiningFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(4);
        return new LettuceConnectionFactory(configuration);
    }
    @Bean
    @Qualifier("5")
    public RedisTemplate<String, Object> sseDiningRedisTemplate(@Qualifier("5") RedisConnectionFactory sseFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

//        객체안의 객체 직렬화 이슈로 인해 아래와 같이 serializer 커스텀
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        serializer.setObjectMapper(objectMapper);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setConnectionFactory(sseFactory);
        return redisTemplate;
    }

    //    리스너 객체 생성.
    @Bean
    @Qualifier("5")
    public RedisMessageListenerContainer redisDiningMessageListenerContainer(@Qualifier("5") RedisConnectionFactory sseFactory){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(sseFactory);
        return container;
    }
// --- 최무리
}
