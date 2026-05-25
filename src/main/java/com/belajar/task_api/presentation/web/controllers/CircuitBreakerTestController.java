package com.belajar.task_api.presentation.web.controllers;

import com.belajar.task_api.application.common.Result;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test-circuit-breaker")
@Slf4j
public class CircuitBreakerTestController {

    @GetMapping
    @CircuitBreaker(name = "backendA", fallbackMethod = "fallback")
    public ResponseEntity<Result<String>> callApi(@RequestParam(defaultValue = "false") boolean fail) {
        log.info("[CircuitBreaker] Simulated API call started (fail={})", fail);
        if (fail) {
            throw new RuntimeException("Downstream service is currently unavailable");
        }
        return ResponseEntity.ok(Result.success("Success response from simulated downstream API"));
    }

    // Fallback method must have the same signature + Throwable parameter
    public ResponseEntity<Result<String>> fallback(boolean fail, Throwable t) {
        log.warn("[CircuitBreaker] Fallback triggered. Reason: {}", t.getMessage());
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Result.failure("Layanan tidak tersedia (Circuit Breaker Aktif). Error: " + t.getMessage()));
    }
}
