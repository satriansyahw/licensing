package com.belajar.task_api.presentation.web.exceptions;

import com.belajar.task_api.application.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // payload validation failed
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        log.warn("Validation error occurred: {}", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Result.failure("Validasi gagal"));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Result<Map<String, String>>> handleNotFoundException(NoResourceFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Result.failure("endpoint not found"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Result<Map<String, String>>> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Result.failure("no access for this feature"));
    }

    @ExceptionHandler(BusinessException.class) // Pastikan Anda sudah membuat class BusinessException
    public ResponseEntity<Result<Map<String, String>>> handleBusinessException(BusinessException ex) {
        log.warn("Business rule violation: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Result.failure("Business rule violation"));
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<Result<Map<String, String>>> handleRateLimitExceededException(RateLimitExceededException ex) {
        log.warn("Rate limit exceeded: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(Result.failure(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Result<Map<String, String>>> handleAllExceptions(Exception ex) {
        log.error("Terjadi error internal sistem: ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.failure("Terjadi kesalahan pada server."));
    }
}
