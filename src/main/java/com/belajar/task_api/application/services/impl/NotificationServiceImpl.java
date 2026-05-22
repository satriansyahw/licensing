package com.belajar.task_api.application.services.impl;

import com.belajar.task_api.application.dto.responses.NotificationResponse;
import com.belajar.task_api.application.services.NotificationService;
import com.belajar.task_api.domain.entities.Notification;
import com.belajar.task_api.domain.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addNotification(UUID applicationId, String message, String persona) {
        Notification notification = Notification.builder()
                .applicationId(applicationId)
                .message(message)
                .persona(persona)
                .isRead(false) // Default belum dibaca
                .build();

        notificationRepository.save(notification);
    }

    @Async
    @Override
    public CompletableFuture<List<NotificationResponse>> getNotifationByPersona(String persona) {
        return notificationRepository.findByPersonaOrderByCreatedAtDesc(persona)
                .thenApply(notifications ->
                        notifications.stream()
                                .map(n -> new NotificationResponse(
                                        n.getId(),
                                        n.getApplicationId(),
                                        n.getMessage(),
                                        n.getPersona(),
                                        n.isRead(),
                                        n.getCreatedAt() != null ? n.getCreatedAt().toString() : null
                                ))
                                .collect(Collectors.toList())
                );
    }
}
