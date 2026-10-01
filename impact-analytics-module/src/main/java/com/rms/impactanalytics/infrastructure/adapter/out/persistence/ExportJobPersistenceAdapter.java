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
        return repository.findByJobId(jobId)
                .map(exportJobJpaEntity -> new ExportJob(
                        exportJobJpaEntity.getJobId(),
                        exportJobJpaEntity.getExportType(),
                        exportJobJpaEntity.getStatus()
        ));
    }

    @Override
    public void save(ExportJob exportJob) {
        ExportJobJpaEntity entity = repository.findByJobId(exportJob.getJobId())
                        .orElseGet(() -> new ExportJobJpaEntity(
                                exportJob.getJobId(),
                                exportJob.getExportType(),
                                exportJob.getStatus()
                        ));
        // Update the state with the value from the exportJob
        entity.setExportType(exportJob.getExportType());
        entity.setStatus(exportJob.getStatus());

        repository.save(entity);
    }
}
