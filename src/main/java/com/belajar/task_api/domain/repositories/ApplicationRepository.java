package com.belajar.task_api.domain.repositories;

import com.belajar.task_api.domain.entities.LicenseApplication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository {

    LicenseApplication save(LicenseApplication application);

    Optional<LicenseApplication> findById(UUID id);

    Optional<LicenseApplication> findByReferenceNumber(String referenceNumber);

    List<LicenseApplication> findByApplicantName(String name);

    void deleteById(UUID id);

    boolean existsByReferenceNumber(String referenceNumber);
    List<LicenseApplication> findByCreatedBy(String createdBy);
}
