package com.vajro.ratelimited.filter;

import com.vajro.ratelimited.exceptions.RestExceptionHandler;
import com.vajro.ratelimited.exceptions.TooManyRequestsException;
import com.vajro.ratelimited.ratelimit.RateLimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final RateLimiterService rateLimiterService;

    public RateLimitingFilter(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null && auth.getPrincipal() != null) ? auth.getPrincipal().toString() : "anonymous";

        if (!rateLimiterService.isAllowed(user)) {
            throw new TooManyRequestsException("Rate limit exceeded. Try again later.");
        }

        filterChain.doFilter(request, response);
    }
}
