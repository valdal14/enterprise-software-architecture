package com.rms.purchaseorder.infrastructure.adapter.in.web;

import com.rms.purchaseorder.application.ports.in.ApprovePurchaseOrderUseCase;
import com.rms.purchaseorder.domain.PurchaseOrderRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class PurchaseOrderController {
    @Autowired
    private final ApprovePurchaseOrderUseCase purchaseOrderService;

    public PurchaseOrderController(ApprovePurchaseOrderUseCase purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping(value = "orders/{orderId}/approve", produces = "application/json")
    public void approveOrderUseCase(@PathVariable UUID orderId, @RequestBody PurchaseOrderRequestDTO purchaseOrderRequestDTO) {
        purchaseOrderService.execute(orderId, BigDecimal.valueOf(purchaseOrderRequestDTO.seasonalBudget()));
    }
}
