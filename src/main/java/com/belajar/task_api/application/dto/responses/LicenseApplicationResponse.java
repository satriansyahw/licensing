package com.belajar.task_api.application.dto.responses;

import java.util.UUID;

public record LicenseApplicationResponse(
        UUID id,
        String applicationNumber,
        String status,
        String createdAt,
        String createdBy
) {}