package com.vajro.ratelimited;

import com.vajro.ratelimited.ratelimit.RateLimiterService;
import com.vajro.ratelimited.ratelimit.RedisCounter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class VajroRatelimitedGatewayApplicationTests {

    private RedisCounter counter;
    private RateLimiterService service;

    @BeforeEach
    void setUp() {
        counter = Mockito.mock(RedisCounter.class);
        service = new RateLimiterService(counter, 3); // small limit for tests
    }

    @Test
    void allowsWhenUnderLimit() {
        when(counter.incrementAndGet(anyString(), Mockito.anyLong())).thenReturn(1L);
        assertTrue(service.isAllowed("user1"));
    }

    @Test
    void blocksWhenOverLimit() {
        when(counter.incrementAndGet(anyString(), Mockito.anyLong())).thenReturn(4L);
        assertFalse(service.isAllowed("user1"));
    }

}
