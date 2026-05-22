package com.belajar.task_api.domain.repositories;

import com.belajar.task_api.domain.entities.Document;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentRepository {
    Document save(Document document);
    Optional<Document> findById(UUID id);

    List<Document> findAllById(Iterable<UUID> ids);

    List<Document> findByApplicationId(UUID applicationId);

    void deleteById(UUID id);
}
