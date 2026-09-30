package com.rms.impactanalytics.application.port.out;

import com.rms.impactanalytics.domain.ExportJob;

@FunctionalInterface
public interface SaveExportJobPort {
    void save(ExportJob exportJob);
}
