package com.belajar.task_api.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog extends BaseEntity {

    @Column(name = "actor_name", nullable = false)
    private String actorName = "";

    @Column(name = "actor_role")
    private String actorRole = "";

    @Column(name = "action", nullable = false)
    private String action = "";

    @Column(name = "old_status")
    private String oldStatus = "";

    @Column(name = "new_status")
    private String newStatus = "";

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment = "";

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now(ZoneOffset.UTC);
}