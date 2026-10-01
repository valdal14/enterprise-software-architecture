package com.rms.impactanalytics.infrastructure.adapter.out.persistence;

import com.rms.impactanalytics.application.port.out.LoadExportJobPort;
import com.rms.impactanalytics.application.port.out.SaveExportJobPort;
import com.rms.impactanalytics.domain.ExportJob;

import java.util.Optional;
import java.util.UUID;

public class ExportJobPersistenceAdapter implements LoadExportJobPort,  SaveExportJobPort  {
    private final ExportJobJpaRepository repository;

    public ExportJobPersistenceAdapter(ExportJobJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ExportJob> load(UUID jobId) {
        Optional<ExportJobJpaEntity> entity = repository.findByJobId(jobId);
        return entity.map(exportJobJpaEntity -> new ExportJob(
                exportJobJpaEntity.getJobId(),
                exportJobJpaEntity.getExportType(),
                exportJobJpaEntity.getStatus()
        ));
    }

    @Override
    public void save(ExportJob exportJob) {

    }
}
