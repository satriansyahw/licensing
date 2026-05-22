package com.belajar.task_api.application.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitApplicationRequest {

    @NotBlank(message = "Applicant Name is required")
    @Size(min = 3, max = 100, message = "Applicant Name must be between 3 and 100 characters")
    private String applicantName;

    @NotBlank(message = "Business Name is required")
    private String businessName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String contactEmail;

    private String dataJson = "{}";

    private List<UUID> documentIds = new ArrayList<>();
}