package com.vajro.ratelimited.controller;

import com.vajro.ratelimited.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class RateLimitedController {
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String username,
                                                     @RequestParam String password) {
        if ("vajro".equals(username) && "vajro123".equals(password)) {
            String token = JwtUtils.generateToken(username);
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }

    @GetMapping("/checkRateLimiter")
    public String hello() {
        return "Hello! Your request passed the rate limiter and authentication.";
    }
}
