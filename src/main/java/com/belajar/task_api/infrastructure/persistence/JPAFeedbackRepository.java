package com.belajar.task_api.infrastructure.persistence;

import com.belajar.task_api.domain.entities.Feedback;
import com.belajar.task_api.domain.repositories.FeedbackRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JPAFeedbackRepository extends JpaRepository<Feedback, UUID>, FeedbackRepository {
}
