package com.rms.impactanalytics.infrastructure.adapter.out.persistence;

import com.rms.impactanalytics.domain.ExportStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@Table(name = "export_job")
@Entity
public class ExportJobJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "JOB_ID")
    private UUID jobId;
    @Column(name = "EXPORT_TYPE")
    private String exportType;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private ExportStatus status;

    public ExportJobJpaEntity(UUID jobId, String exportType, ExportStatus status) {
        this.jobId = jobId;
        this.exportType = exportType;
        this.status = status;
    }
}
