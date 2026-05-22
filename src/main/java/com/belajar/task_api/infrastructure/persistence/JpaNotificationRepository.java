package com.belajar.task_api.infrastructure.persistence;

import com.belajar.task_api.domain.entities.Notification;
import com.belajar.task_api.domain.repositories.NotificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JpaNotificationRepository extends JpaRepository<Notification, UUID>
, NotificationRepository {
    // 1. save() dan findById() TIDAK perlu ditulis lagi
    // karena sudah otomatis disediakan bawaan dari JpaRepository.

    // 2. Spring Data JPA otomatis memahami query ini dari nama metodenya
    @Override
    List<Notification> findByApplicationId(UUID applicationId);

    // 3. Menggunakan JPQL manual karena nama metode "findUnreadByPersona"
    // adalah bahasa bisnis (Domain), bukan bahasa standar Spring Data
    @Override
    @Query("SELECT n FROM Notification n WHERE n.persona = :persona AND n.isRead = false ORDER BY n.createdAt DESC")
    List<Notification> findUnreadByPersona(@Param("persona") String persona);

    // 4. Sama seperti di atas, kita terjemahkan ke JPQL untuk menghitung baris
    @Override
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.persona = :persona AND n.isRead = false")
    long countUnreadByPersona(@Param("persona") String persona);

    // 5. Menggunakan @Modifying karena ini adalah operasi UPDATE, bukan sekadar SELECT data
    @Override
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id = :id")
    void markAsRead(@Param("id") UUID id);
}
