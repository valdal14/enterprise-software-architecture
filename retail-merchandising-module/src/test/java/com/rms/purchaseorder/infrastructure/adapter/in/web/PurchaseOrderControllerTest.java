package com.rms.purchaseorder.infrastructure.adapter.in.web;

import com.rms.purchaseorder.application.ports.in.ApprovePurchaseOrderUseCase;
import com.rms.purchaseorder.domain.PurchaseOrderRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PurchaseOrderController.class)
class PurchaseOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ApprovePurchaseOrderUseCase useCase;

    @Test
    void approveOrderUseCase() throws Exception {
        // ARRANGE
        UUID orderId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(500.00);
        PurchaseOrderRequestDTO order = new PurchaseOrderRequestDTO(500);
        // ACT
        mockMvc.perform(post("/api/v1/orders/{orderId}/approve", orderId)
                        .content(objectMapper.writeValueAsString(order))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // VERIFY
        verify(useCase).execute(orderId, amount);
    }
}