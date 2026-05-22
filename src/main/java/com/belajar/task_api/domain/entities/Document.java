package com.belajar.task_api.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class Document extends  BaseEntity{

    @Column(name = "file_name", nullable = false)
    private String fileName = "";

    @Column(name = "file_path", nullable = false)
    private String filePath = "";

    @Column(name = "content_type")
    private String contentType = "";

    @Column(name = "ai_status")
    private String aiStatus = "Pending";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", insertable = false, updatable = false)
    private LicenseApplication application;
/*
* @Query("SELECT d FROM Document d JOIN FETCH d.application")
    List<Document> findAllWithApplication();
* */
}
