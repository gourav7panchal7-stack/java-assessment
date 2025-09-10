package com.vajro.ratelimited.ratelimit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RateLimiterService {

    private final RedisCounter redisCounter;
    private final int requestsPerMinute;

    public RateLimiterService(RedisCounter redisCounter,
                              @Value("${ratelimit.requests-per-minute}") int requestsPerMinute) {
        this.redisCounter = redisCounter;
        this.requestsPerMinute = requestsPerMinute;
    }


    public boolean isAllowed(String userId) {
        long now = Instant.now().getEpochSecond();
        long window = now / 60;
        String key = "rl:" + userId + ":" + window;
        long ttl = 65;
        long count = redisCounter.incrementAndGet(key, ttl);
        return count <= requestsPerMinute;
    }
}
