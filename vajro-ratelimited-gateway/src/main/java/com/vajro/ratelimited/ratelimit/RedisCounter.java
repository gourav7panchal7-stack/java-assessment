package com.vajro.ratelimited.ratelimit;

public interface RedisCounter {
    /**
     * Increment key atomically and return the new count.
     * ttlSeconds should be set by the caller only when creating the key first time.
     */
    long incrementAndGet(String key, long ttlSeconds);
}
