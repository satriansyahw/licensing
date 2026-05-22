package com.belajar.task_api.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.UUID;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class) // <-- 1. WAJIB: Ini pemicu agar Spring mengamati insert/update data
public abstract class BaseEntity { // SANGAT DISARANKAN memakai keyword 'abstract'

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @CreatedDate // <-- 2. Mengisi waktu otomatis saat pertama kali data di-INSERT
    @Column(name = "created_at", updatable = false, nullable = false)
    private Date createdAt;

    @LastModifiedDate // <-- 3. Mengisi waktu otomatis saat data di-UPDATE
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @CreatedBy // <-- 4. Otomatis menyedot username dari JWT lewat SecurityAuditorAware saat INSERT
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy // <-- 5. Otomatis menyedot username dari JWT lewat SecurityAuditorAware saat UPDATE
    @Column(name = "updated_by")
    private String updatedBy;
}