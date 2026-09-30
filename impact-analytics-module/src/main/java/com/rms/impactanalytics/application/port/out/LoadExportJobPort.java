package com.rms.impactanalytics.application.port.out;

import com.rms.impactanalytics.domain.ExportJob;

import java.util.Optional;
import java.util.UUID;

@FunctionalInterface
public interface LoadExportJobPort {
    Optional<ExportJob> load(UUID jobId);
}
