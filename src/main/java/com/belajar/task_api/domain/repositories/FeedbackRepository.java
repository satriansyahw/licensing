package com.belajar.task_api.domain.repositories;

import com.belajar.task_api.domain.entities.Feedback;

import java.util.List;
import java.util.UUID;

public interface FeedbackRepository {
    List<Feedback> findByApplicationIdAndIsResolvedFalse(UUID applicationId);
}
