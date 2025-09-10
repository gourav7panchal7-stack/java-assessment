package com.vajro.ratelimited.ratelimit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisCounterRedisImpl implements RedisCounter {

    private final StringRedisTemplate redisTemplate;

    public RedisCounterRedisImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public long incrementAndGet(String key, long ttlSeconds) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        Long value = ops.increment(key);
        // When key is first created (value == 1) set TTL so the window expires.
        if (value != null && value == 1L) {
            redisTemplate.expire(key, Duration.ofSeconds(ttlSeconds));
        }
        return value != null ? value : 0L;
    }
}
