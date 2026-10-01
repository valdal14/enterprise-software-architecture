package com.rms.impactanalytics.application;

import com.rms.impactanalytics.application.port.out.LoadExportJobPort;
import com.rms.impactanalytics.application.port.out.SaveExportJobPort;
import com.rms.impactanalytics.infrastructure.adapter.out.persistence.ExportJobJpaRepository;
import com.rms.impactanalytics.infrastructure.adapter.out.persistence.ExportJobPersistenceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImpactAnalyticsConfig {

    @Bean
    public ExportJobService exportJobService(LoadExportJobPort load, SaveExportJobPort save) {
        return new ExportJobService(load, save);
    }

    @Bean
    public ExportJobPersistenceAdapter exportJobPersistenceAdapter(ExportJobJpaRepository repository) {
        return new ExportJobPersistenceAdapter(repository);
    }
}
