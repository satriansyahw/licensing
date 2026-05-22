package com.belajar.task_api.application.services;

import com.belajar.task_api.application.dto.requests.SubmitApplicationRequest;
import com.belajar.task_api.application.dto.responses.LicenseApplicationResponse;
import com.belajar.task_api.domain.entities.LicenseApplication;

import java.util.List;
import java.util.UUID;

public interface ApplicationService {
    LicenseApplication submitApplication(SubmitApplicationRequest request);
    List<LicenseApplicationResponse> getAllMyApplications();
    LicenseApplicationResponse findById(UUID id);
    void resubmitApplication(UUID applicationId, SubmitApplicationRequest request);
}
