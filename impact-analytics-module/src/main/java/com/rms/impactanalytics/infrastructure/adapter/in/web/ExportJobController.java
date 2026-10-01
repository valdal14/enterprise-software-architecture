package com.rms.impactanalytics.infrastructure.adapter.in.web;

import com.rms.impactanalytics.application.port.in.TriggerExportJobUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class ExportJobController {
    private final TriggerExportJobUseCase triggerExportJobUseCase;

    @Autowired
    public ExportJobController(TriggerExportJobUseCase triggerExportJobUseCase) {
        this.triggerExportJobUseCase = triggerExportJobUseCase;
    }

    @PostMapping(value = "export-jobs/{jobId}/trigger", produces = "application/json")
    public void markJobInProgressUseCase(@PathVariable UUID jobId) {
        triggerExportJobUseCase.execute(jobId);
    }
}
