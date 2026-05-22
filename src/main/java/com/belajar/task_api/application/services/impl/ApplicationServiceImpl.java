package com.belajar.task_api.application.services.impl;

import com.belajar.task_api.application.dto.requests.SubmitApplicationRequest;
import com.belajar.task_api.application.dto.responses.LicenseApplicationResponse;
import com.belajar.task_api.application.services.ApplicationService;
import com.belajar.task_api.application.services.NotificationService;
import com.belajar.task_api.domain.entities.ApplicationSnapshot;
import com.belajar.task_api.domain.entities.Document;
import com.belajar.task_api.domain.entities.Feedback;
import com.belajar.task_api.domain.entities.LicenseApplication;
import com.belajar.task_api.domain.enums.ApplicationStatus;
import com.belajar.task_api.domain.repositories.ApplicationRepository;
import com.belajar.task_api.domain.repositories.ApplicationSnapshotRepository;
import com.belajar.task_api.domain.repositories.DocumentRepository;
import com.belajar.task_api.domain.repositories.FeedbackRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final DocumentRepository documentRepository;
    private final ApplicationSnapshotRepository snapshotRepository;
    private final NotificationService notificationService; // Service internal
    private final FeedbackRepository feedbackRepository;

    @Value("${app.auth.simulated-role}")
    private String configuredRole;

    @Override
    @Transactional(rollbackOn = Exception.class) // Pengganti await _dbContext.SaveChangesAsync()
    public LicenseApplication submitApplication(SubmitApplicationRequest request) {
        log.info("Memulai proses submit aplikasi untuk pemohon: {}", request.getApplicantName());
        // 1. Generate Reference Number (LIC-yyyyMMddHHmmss-6Random)
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomSuffix = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        String refNumber = String.format("LIC-%s-%s", timestamp, randomSuffix);

        // 2. Mapping Request ke Entity
        LicenseApplication application = LicenseApplication.builder()
                .applicantName(request.getApplicantName())
                .businessName(request.getBusinessName())
                .contactEmail(request.getContactEmail())
                .dataJson(request.getDataJson())
                .referenceNumber(refNumber)
                .status(ApplicationStatus.APPLICATION_RECEIVED)
                .build();

        LicenseApplication savedApp = applicationRepository.save(application);

        if (request.getDocumentIds() != null && !request.getDocumentIds().isEmpty()) {
            List<Document> documents = documentRepository.findAllById(request.getDocumentIds());
            for (Document doc : documents) {
                doc.setApplication(savedApp);
                documentRepository.save(doc);
            }
        }

        ApplicationSnapshot snapshot = ApplicationSnapshot.builder()
                .application(savedApp)
                .version(1)
                .dataJson(request.getDataJson())
                .submittedBy("Operator")
                .build();
        snapshotRepository.save(snapshot);

        notificationService.addNotification(
                savedApp.getId(),
                String.format("New application %s submitted by %s", savedApp.getReferenceNumber(), savedApp.getApplicantName()),
                configuredRole
        );
        return application;
       // return savedApp.getId();
    }
    public List<LicenseApplicationResponse> getAllMyApplications() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        return applicationRepository.findByCreatedBy(currentUsername)
                .stream()
                .map(entity -> new LicenseApplicationResponse(
                        entity.getId(),
                        entity.getReferenceNumber(), // Sesuaikan dengan field di entity Anda
                        entity.getStatus() != null ? entity.getStatus().name() : null,
                        entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null,
                        entity.getCreatedBy()
                ))
                .collect(Collectors.toList());
    }
    public LicenseApplicationResponse findById(UUID id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        return applicationRepository.findById(id)
                .map(entity -> {
                    // GUARD CLAUSE KEAMANAN:
                    // Memastikan user tidak bisa mengintip data milik orang lain via UUID URL
                    if (!entity.getCreatedBy().equals(currentUsername)) {
                        throw new RuntimeException("Akses ditolak: Anda tidak memiliki hak akses untuk melihat permohonan ini");
                    }

                    // Jika valid, konversi Entity ke DTO tunggal
                    return new LicenseApplicationResponse(
                            entity.getId(),
                            entity.getReferenceNumber(),
                            entity.getStatus() != null ? entity.getStatus().name() : null,
                            entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null,
                            entity.getCreatedBy()
                    );
                })
                // 3. Jika UUID tidak ditemukan sama sekali di database, lempar Exception
                .orElse(null);
    }
    @Transactional(rollbackOn = Exception.class) // WAJIB: Menjamin ACID (jika di tengah jalan error, semua perubahan di-rollback otomatis)
    public void resubmitApplication(UUID applicationId, SubmitApplicationRequest request) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        LicenseApplication app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application dengan ID tersebut tidak ditemukan."));
        if (!app.getCreatedBy().equals(currentUsername)) {
            throw new RuntimeException("Akses ditolak: Anda tidak memiliki hak akses untuk merubah permohonan ini.");
        }

        app.setApplicantName(request.getApplicantName());
        app.setBusinessName(request.getBusinessName());
        app.setContactEmail(request.getContactEmail());
        app.setDataJson(request.getDataJson());
        if (app.getStatus() == ApplicationStatus.PENDING_PRE_SITE_RESUBMISSION) {
            app.setStatus(ApplicationStatus.PRE_SITE_RESUBMITTED);
        } else {
            app.setStatus(ApplicationStatus.POST_SITE_CLARIFICATION_RESUBMITTED);
        }

        // 6. Sinkronisasi Dokumen (JPA Managed State handling)
        if (request.getDocumentIds() != null && !request.getDocumentIds().isEmpty()) {
            List<UUID> currentDocIds = app.getDocuments().stream()
                    .map(Document::getId)
                    .collect(Collectors.toList());

            // Mencari selisih ID Dokumen baru (Setara .Except() di LINQ C#)
            List<UUID> toAdd = request.getDocumentIds().stream()
                    .filter(id -> !currentDocIds.contains(id))
                    .collect(Collectors.toList());

            if (!toAdd.isEmpty()) {
                List<Document> newDocs = documentRepository.findAllById(toAdd);
                for (Document doc : newDocs) {
                    doc.setApplication(app); // Pasangkan foreign key ke parent application
                }
            }
        }

        // 7. Buat Snapshot Versi Riwayat Baru (Application History Versioning)
        int latestVersion = snapshotRepository.findMaxVersionByApplicationId(applicationId).orElse(0);

        ApplicationSnapshot snapshot = new ApplicationSnapshot();
        snapshot.setApplication(app);
        snapshot.setVersion(latestVersion + 1);
        snapshot.setDataJson(request.getDataJson());
        snapshot.setSubmittedBy("Operator (Resubmission)");
        snapshotRepository.save(snapshot);
        List<Feedback> unresolvedFeedbacks = feedbackRepository.findByApplicationIdAndIsResolvedFalse(applicationId);
        for (Feedback f : unresolvedFeedbacks) {
            f.setResolved(true);
        }
        applicationRepository.save(app);
        sendInternalNotification(applicationId, "Application " + app.getReferenceNumber() + " has been resubmitted", "Officer");
    }

    private void sendInternalNotification(UUID applicationId, String message, String targetRole) {
        System.out.println("NOTIF LOG -> Target: " + targetRole + " | Pesan: " + message);
    }
}