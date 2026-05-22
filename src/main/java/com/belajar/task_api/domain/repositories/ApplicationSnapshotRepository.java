package com.belajar.task_api.domain.repositories;

import com.belajar.task_api.domain.entities.ApplicationSnapshot;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationSnapshotRepository {
    ApplicationSnapshot save(ApplicationSnapshot snapshot);
    List<ApplicationSnapshot> findByApplicationIdOrderByVersionDesc(UUID applicationId);
    Optional<ApplicationSnapshot> findByApplicationIdAndVersion(UUID applicationId, int version);
    Optional<Integer> findMaxVersionByApplicationId(UUID applicationId);
}
