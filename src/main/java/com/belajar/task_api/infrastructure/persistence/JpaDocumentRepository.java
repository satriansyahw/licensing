package com.belajar.task_api.infrastructure.persistence;

import com.belajar.task_api.domain.entities.Document;
import com.belajar.task_api.domain.repositories.DocumentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaDocumentRepository extends JpaRepository<Document, UUID>, DocumentRepository {
    List<Document> findByApplicationId(UUID applicationId);
}
