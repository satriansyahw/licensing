package com.belajar.task_api.application.dto.responses;

import java.util.UUID;

public record NotificationResponse(
        UUID id,
        UUID applicationId,
        String message,
        String persona,
        boolean isRead,
        String createdAt
) {}