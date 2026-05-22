package com.belajar.task_api.application.dto.responses;

public record AuthResponse(
        String token,
        String username,
        String role
) {}