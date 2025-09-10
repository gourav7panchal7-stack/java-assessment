package com.vajro.ratelimited.config;

import com.vajro.ratelimited.security.JwtAuthenticationFilter;
import com.vajro.ratelimited.filter.RateLimitingFilter;
import com.vajro.ratelimited.ratelimit.RateLimiterService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final RateLimiterService rateLimiterService;

    public SecurityConfig(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.securityMatcher("/**").authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll().anyRequest().authenticated()).csrf(csrf -> csrf.disable()); // modern way to disable CSRF

        return http.build();
    }
}
