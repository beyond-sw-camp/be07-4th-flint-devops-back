package com.hotel.flint.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

//    Redis value에 객체를 담기 위한 설정
    public <T> T getData(String key, Class<T> clazz) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Object value = valueOperations.get(key);
        if (value != null) {
            return clazz.cast(value);
        }
        return null;
    }

//    저장된 데이터 얼마나 유지할 지 결정하는 메서드
    public void setDataExpire(String key, Object value, Long duration) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }
}
