package com.belajar.task_api.domain.enums;

import lombok.Getter;

@Getter
public enum ApplicationStatus {
    APPLICATION_RECEIVED(1),
    UNDER_REVIEW(2),
    PENDING_PRE_SITE_RESUBMISSION(3),
    PRE_SITE_RESUBMITTED(4),
    SITE_VISIT_SCHEDULED(5),
    SITE_VISIT_DONE(6),
    AWAITING_POST_SITE_CLARIFICATION(7),
    PENDING_POST_SITE_RESUBMISSION(8),
    POST_SITE_CLARIFICATION_RESUBMITTED(9),
    PENDING_APPROVAL(10),
    APPROVED(11),
    REJECTED(12);

    private final int value;

    ApplicationStatus(int value) {
        this.value = value;
    }
}
