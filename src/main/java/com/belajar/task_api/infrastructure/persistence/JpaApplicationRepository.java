package com.belajar.task_api.infrastructure.persistence;

import com.belajar.task_api.domain.entities.LicenseApplication;
import com.belajar.task_api.domain.repositories.ApplicationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaApplicationRepository
        extends JpaRepository<LicenseApplication, UUID>, ApplicationRepository
{

}
