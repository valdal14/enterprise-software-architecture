package com.rms.impactanalytics.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ExportJob {
    private UUID jobId;
    private String exportType;
    private ExportStatus status;

    /**
     * Mark the status of an export task in progress.
     * Throws InvalidJobStateException if the at call
     * time the status is NOT PENDING. If the status is
     * not on PENDING it will change the status to
     * IN_PROGRESS
     */
    public void markInProgress() {
        if (status != ExportStatus.PENDING) {
            throw new InvalidJobStateException("Expected status PENDING but got " + status.name());
        }
        status = ExportStatus.IN_PROGRESS;
    }
}
