package com.rms.impactanalytics.application;

import com.rms.impactanalytics.application.port.out.LoadExportJobPort;
import com.rms.impactanalytics.application.port.out.SaveExportJobPort;
import com.rms.impactanalytics.domain.ExportJob;
import com.rms.impactanalytics.domain.ExportStatus;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class ExportJobServiceTest {

    @Test
    void executeSuccessfullyLoadTheExportJob() {
        // ARRANGE
        boolean mustFail = false;
        ExportJob expectedExportJob = new ExportJob(UUID.randomUUID(), "PURCHASE_ORDER", ExportStatus.PENDING);
        LoadExportJobPortImpl load = new LoadExportJobPortImpl(mustFail);
        ExportJobService sut = new ExportJobService(load, new SaveExportJobPortImpl());
        // ACT
        sut.execute(expectedExportJob.getJobId());
        // VERIFY
        assertTrue(load.verifyCall);
    }

    @Test
    void executeSuccessfullyLoadAndMarkTheStateOfTheExportStatusToInProgress() {

    }

    /**
     * Concrete helper mocked class that implements LoadExportJobPort
     * and the load method
     */
    @Getter
    private static class LoadExportJobPortImpl implements LoadExportJobPort {
        private final boolean mustFail;
        private boolean verifyCall;

        public LoadExportJobPortImpl(boolean mustFail) {
            this.mustFail = mustFail;
        }

        @Override
        public Optional<ExportJob> load(UUID jobId) {
            verifyCall = true;

            if (mustFail) {
                return Optional.of(new ExportJob(UUID.randomUUID(), "PURCHASE_ORDER", ExportStatus.COMPLETED));
            } else {
                return Optional.of(new ExportJob(UUID.randomUUID(), "PURCHASE_ORDER", ExportStatus.PENDING));
            }
        }
    }

    /**
     * Concrete helper mocked class that implements SaveExportJobPort
     * and the save method
     */
    private static class SaveExportJobPortImpl implements SaveExportJobPort {
        @Override
        public void save(ExportJob exportJob) {

        }
    }
}