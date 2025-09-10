# java-assessment
  
## Overview

This project implements a Spring Boot API Gateway that enforces per-user rate limiting. Each authenticated user is allowed a maximum of 100 requests per minute. The system uses JWT-based authentication for stateless security and Redis for atomic per-user request counters. Requests exceeding the limit return HTTP 429 (Too Many Requests). The design ensures thread safety and supports high-concurrency scenarios.

## Design Decisions

- Authentication: Users authenticate via `/api/auth/login`. Successful login returns a JWT token. Protected endpoints require the token in the `Authorization` header.  
- Rate Limiting:  
  - Implemented per-user per-minute using Redis counters.  
  - Redis key format: `ratelimit:<username>:<minute>`.  
  - Atomic increment (`INCR`) ensures thread-safe counting.  
  - TTL of 60 seconds automatically resets counters each minute.  
- Filters:  
  - `JwtAuthenticationFilter` validates JWT tokens and sets the security context.  
  - `RateLimitingFilter` checks Redis counters and throws `TooManyRequestsException` if limits are exceeded.  
- Exception Handling:  
  - Centralized using `@RestControllerAdvice`.  
  - Returns structured JSON with timestamp, HTTP status 429, error, and message.

## Technology Choices

- Spring Boot: Framework for building REST APIs and filters.  
- Spring Security: Handles authentication and security context management.  
- JWT (JSON Web Token): Stateless authentication mechanism.  
- Redis: In-memory data store for fast, atomic counters with TTL.  
- Maven: Build and dependency management.  
- Mockito & Spring Boot Test: Unit testing for rate-limiting logic.

Rationale:  
- JWT allows stateless authentication without server-side sessions.  
- Redis provides atomic, thread-safe counters for high concurrency.  
- Filters modularize authentication and rate-limiting logic, keeping controllers clean.

## Assumptions

- Only authenticated users are rate-limited.  
- Rate limit is fixed at 100 requests per minute per user.  
- Redis is running locally or network-accessible.  
- Counters do not persist across Redis restarts, acceptable for per-minute rate limiting.  
- Authentication credentials are simplified for demonstration purposes.

## Endpoints

- `/api/auth/login` (POST): Authenticates the user and returns a JWT token.  
- `/checkRateLimiter` (GET): Sample protected endpoint subject to rate limiting.

## Running the Project

1. Start a Redis server locally.  
2. Build and run the Spring Boot application using Maven.  
3. Authenticate via `/api/auth/login` to receive a JWT token.  
4. Access protected endpoints using the token in the `Authorization` header.

## Error Response Example

Exceeding the rate limit returns HTTP 429 with JSON:

json
{
  "timestamp": "2025-09-10T19:20:30",
  "status": 429,
  "error": "Too Many Requests",
  "message": "Rate limit exceeded. Try again later."
}   



                        
