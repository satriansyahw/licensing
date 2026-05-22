package com.belajar.task_api.infrastructure.persistence;

import com.belajar.task_api.domain.entities.ApplicationSnapshot;
import com.belajar.task_api.domain.repositories.ApplicationSnapshotRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaApplicationSnapshotRepository extends JpaRepository<ApplicationSnapshot, UUID>
        , ApplicationSnapshotRepository {
}
