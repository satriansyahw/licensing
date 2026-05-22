package com.belajar.task_api.domain.repositories;

import com.belajar.task_api.domain.entities.Notification;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface NotificationRepository {
    Notification save(Notification notification);
    Optional<Notification> findById(UUID id);
    List<Notification> findByApplicationId(UUID applicationId);
    List<Notification> findUnreadByPersona(String persona);
    long countUnreadByPersona(String persona);
    void markAsRead(UUID id);
    @Async
    CompletableFuture<List<Notification>>findByPersonaOrderByCreatedAtDesc(String persona);
}
