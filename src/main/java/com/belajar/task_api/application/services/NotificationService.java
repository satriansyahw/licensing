package com.belajar.task_api.application.services;

import com.belajar.task_api.application.dto.responses.NotificationResponse;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface NotificationService {
    void addNotification(UUID applicationId, String message, String persona);
    @Async
    CompletableFuture<List<NotificationResponse>> getNotifationByPersona(String persona);

}
