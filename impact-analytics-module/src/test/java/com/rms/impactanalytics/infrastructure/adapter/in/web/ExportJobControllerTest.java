package com.rms.impactanalytics.infrastructure.adapter.in.web;

import com.rms.impactanalytics.application.port.in.TriggerExportJobUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExportJobController.class)
class ExportJobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TriggerExportJobUseCase useCase;

    @Test
    void markJobInProgressUseCase() throws Exception {
        // ARRANGE & ACT
        UUID jobId = UUID.randomUUID();
        mockMvc.perform(post("/api/v1/export-jobs/{jobId}/trigger", jobId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // VERIFY
        verify(useCase).execute(jobId);
    }
}