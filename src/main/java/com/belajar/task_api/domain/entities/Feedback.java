package com.belajar.task_api.domain.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "feedbacks")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback extends BaseEntity {

    @Column(name = "field_name", nullable = false)
    private String fieldName = "";

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment = "";

    @Column(name = "officer_name")
    private String officerName = "System Officer";

    @Column(name = "is_resolved")
    private boolean isResolved = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", insertable = false, updatable = false)
    private LicenseApplication application;
}