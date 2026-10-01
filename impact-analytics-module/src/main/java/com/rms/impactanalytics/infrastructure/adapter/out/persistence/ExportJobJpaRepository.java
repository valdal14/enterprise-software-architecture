package com.rms.impactanalytics.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ExportJobJpaRepository extends JpaRepository<ExportJobJpaEntity, UUID> {
    Optional<ExportJobJpaEntity> findByJobId(UUID jobId);
}
