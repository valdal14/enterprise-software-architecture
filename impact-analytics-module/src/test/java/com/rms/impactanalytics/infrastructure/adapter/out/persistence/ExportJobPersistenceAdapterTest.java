package com.rms.impactanalytics.infrastructure.adapter.out.persistence;

import com.rms.impactanalytics.domain.ExportJob;
import com.rms.impactanalytics.domain.ExportStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExportJobPersistenceAdapterTest {

    @Mock
    private ExportJobJpaRepository repository;

    @InjectMocks
    private ExportJobPersistenceAdapter adapter;


    @Test
    void load() {
        // ARRANGE
        ExportJob exportJob = new ExportJob(UUID.randomUUID(), "PURCHASE_ORDER", ExportStatus.PENDING);
        Optional<ExportJob> expectedExportJob = Optional.of(exportJob);

        // Create the mock entity
        ExportJobJpaEntity mockJpaEntity = new ExportJobJpaEntity();
        mockJpaEntity.setJobId(exportJob.getJobId());
        mockJpaEntity.setExportType(exportJob.getExportType());
        mockJpaEntity.setStatus(exportJob.getStatus());
        // Mock the dependency (repository)
        when(repository.findByJobId(exportJob.getJobId())).thenReturn(Optional.of(mockJpaEntity));

        // ACT
        Optional<ExportJob> actualExportJob = adapter.load(exportJob.getJobId());
        assertTrue(actualExportJob.isPresent());

        assertAll(
                () -> assertEquals(expectedExportJob.get().getJobId(), actualExportJob.get().getJobId()),
                () -> assertEquals(expectedExportJob.get().getExportType(), actualExportJob.get().getExportType()),
                () -> assertEquals(expectedExportJob.get().getStatus(), actualExportJob.get().getStatus())
        );
        // VERIFY
        verify(repository).findByJobId(exportJob.getJobId());
    }

    @Test
    void loadReturnsAnEmptyOptionalWhenTheJobIdIsNotFound() {
        ExportJob exportJob = new ExportJob(UUID.randomUUID(), "PURCHASE_ORDER", ExportStatus.PENDING);
        // Mock the dependency (repository)
        when(repository.findByJobId(exportJob.getJobId())).thenReturn(Optional.empty());
        // ACT & ASSERT
        Optional<ExportJob> actualExportJob = adapter.load(exportJob.getJobId());
        assertFalse(actualExportJob.isPresent());
        // VERIFY
        verify(repository).findByJobId(exportJob.getJobId());
    }

    @Test
    void save() {
    }
}