package com.belajar.task_api.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "application_snapshots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationSnapshot extends BaseEntity {
    @Column(name = "version", nullable = false)
    private int version;

    @Lob
    @Column(name = "data_json", columnDefinition = "TEXT", nullable = false)
    private String dataJson = "";

    @Column(name = "submitted_by")
    private String submittedBy = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", insertable = false, updatable = false)
    private LicenseApplication application;
}