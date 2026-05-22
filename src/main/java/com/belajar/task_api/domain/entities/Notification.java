package com.belajar.task_api.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {
   @Column(name = "application_id", nullable = false)
   private UUID applicationId;

   @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message = "";

    @Column(name = "persona", nullable = false)
    private String persona = "";

    @Column(name = "is_read")
    private boolean isRead = false;
}