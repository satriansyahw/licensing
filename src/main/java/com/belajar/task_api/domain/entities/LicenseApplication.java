package com.belajar.task_api.domain.entities;

import com.belajar.task_api.domain.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "license_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class LicenseApplication extends BaseEntity{
    @Column(name = "reference_number", unique = true, nullable = false)
    private String referenceNumber = "";

    @Column(name = "applicant_name", nullable = false)
    private String applicantName = "";

    @Column(name = "business_name")
    private String businessName = "";

    @Column(name = "contact_email")
    private String contactEmail = "";

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ApplicationStatus status = ApplicationStatus.APPLICATION_RECEIVED;

    @Column(name = "data_json", columnDefinition = "TEXT")
    private String dataJson = "{}";

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbacks = new ArrayList<>();

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplicationSnapshot> snapshots = new ArrayList<>();
}
