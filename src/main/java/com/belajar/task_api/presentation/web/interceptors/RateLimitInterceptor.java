package com.belajar.task_api.presentation.web.interceptors;

import com.belajar.task_api.presentation.web.exceptions.RateLimitExceededException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Value("${app.rate-limit.capacity:10}")
    private int capacity;

    @Value("${app.rate-limit.duration-in-seconds:60}")
    private long durationInSeconds;

    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(capacity)
                        .refillGreedy(capacity, Duration.ofSeconds(durationInSeconds))
                        .build())
                .build();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("[RateLimit] Checking request: {} {}, DispatcherType: {}", 
                request.getMethod(), request.getRequestURI(), request.getDispatcherType());

        if (jakarta.servlet.DispatcherType.ASYNC.equals(request.getDispatcherType())) {
            log.info("[RateLimit] Skipping async dispatch");
            return true;
        }

        String key;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() && 
                !"anonymousUser".equals(authentication.getName())) {
            key = authentication.getName();
        } else {
            key = request.getRemoteAddr();
        }

        Bucket bucket = cache.computeIfAbsent(key, k -> createNewBucket());

        long availableTokens = bucket.getAvailableTokens();
        log.info("[RateLimit] Key: {}, Available Tokens before consumption: {}", key, availableTokens);

        if (bucket.tryConsume(1)) {
            log.info("[RateLimit] Request allowed. Remaining Tokens: {}", bucket.getAvailableTokens());
            return true;
        } else {
            log.warn("[RateLimit] Rate limit exceeded for key: {}", key);
            throw new RateLimitExceededException("Terlalu banyak permintaan. Silakan coba lagi nanti.");
        }
    }
}
