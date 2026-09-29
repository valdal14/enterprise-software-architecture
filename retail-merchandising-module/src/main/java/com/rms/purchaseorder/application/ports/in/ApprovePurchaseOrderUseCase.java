package com.rms.purchaseorder.application.ports.in;

import java.math.BigDecimal;
import java.util.UUID;

@FunctionalInterface
public interface ApprovePurchaseOrderUseCase {
    void execute(UUID orderId, BigDecimal seasonalBudget);
}
