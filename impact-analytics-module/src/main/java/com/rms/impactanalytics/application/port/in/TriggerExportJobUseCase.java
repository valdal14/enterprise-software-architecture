package com.rms.impactanalytics.application.port.in;

import java.util.UUID;

@FunctionalInterface
public interface TriggerExportJobUseCase {
    void execute(UUID jobId);
}
