package com.rms.impactanalytics.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ExportJobTest {
    private ExportJob exportJob;
    private UUID id;
    private String exportType;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        exportType = "PURCHASE_ORDER";
        exportJob = makeSUT(id, exportType, ExportStatus.COMPLETED);
    }

    @AfterEach
    void tearDown() {
        exportJob = null;
    }

    @Test
    void markInProgressSuccessfullyVerifiesTheInProgressStatus() {
        ExportStatus expectedStatus = ExportStatus.IN_PROGRESS;
        exportJob =  makeSUT(id, exportType, ExportStatus.PENDING);
        exportJob.markInProgress();
        assertEquals(expectedStatus, exportJob.getStatus());
    }

    @Test
    void markInProgressThrowsInvalidJobStateExceptionIfTheStatusIsNotPending() {
        assertThrows(InvalidJobStateException.class, () -> exportJob.markInProgress());
    }

    @Test
    void getJobId() {
        UUID expectedUUID = id;
        assertEquals(expectedUUID, exportJob.getJobId());
    }

    @Test
    void getExportType() {
        String expectedExportType = exportType;
        assertEquals(expectedExportType, exportJob.getExportType());
    }

    @Test
    void getStatus() {
        String expectedStatus = ExportStatus.COMPLETED.name();
        assertEquals(expectedStatus, exportJob.getStatus().name());
    }

    /**
     * Private factory method used to create an instance of the System Under Test
     * @param jobId: The ID of the job
     * @param exportType: The type of the export described as a String
     * @param status: The status of the export job defined in the ExportStatus ENUM
     * @return An instance of the ExportJob class
     */
    private ExportJob makeSUT(UUID jobId, String exportType, ExportStatus status) {
        return new ExportJob(jobId, exportType, status);
    }
}