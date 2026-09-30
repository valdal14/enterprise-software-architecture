package com.rms.impactanalytics.application;

import com.rms.impactanalytics.application.port.in.TriggerExportJobUseCase;
import com.rms.impactanalytics.application.port.out.LoadExportJobPort;
import com.rms.impactanalytics.application.port.out.SaveExportJobPort;
import com.rms.impactanalytics.domain.ExportJob;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class ExportJobService implements TriggerExportJobUseCase {
    private final LoadExportJobPort loadInterface;
    private final SaveExportJobPort saveInterface;

    @Override
    public void execute(UUID jobId) {
        Optional<ExportJob> exportJob = loadInterface.load(jobId);
        if (exportJob.isPresent()) {
            ExportJob job = exportJob.get();
            job.markInProgress();
            saveInterface.save(job);
        } else {
            throw new IllegalArgumentException("Could not find any export job with id: " + jobId.toString());
        }
    }
}
