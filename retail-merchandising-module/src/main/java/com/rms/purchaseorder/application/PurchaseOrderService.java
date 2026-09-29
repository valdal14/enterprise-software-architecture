package com.rms.purchaseorder.application;

import com.rms.purchaseorder.application.ports.in.ApprovePurchaseOrderUseCase;
import com.rms.purchaseorder.application.ports.out.LoadPurchaseOrderPort;
import com.rms.purchaseorder.application.ports.out.SavePurchaseOrderPort;
import com.rms.purchaseorder.domain.PurchaseOrder;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class PurchaseOrderService implements ApprovePurchaseOrderUseCase {
    private final LoadPurchaseOrderPort loadPurchaseOrderPort;
    private final SavePurchaseOrderPort savePurchaseOrderPort;

    @Override
    public void execute(UUID orderId, BigDecimal seasonalBudget) {
        Optional<PurchaseOrder> orderStored = loadPurchaseOrderPort.load(orderId);
        if (orderStored.isPresent()) {
            PurchaseOrder purchaseOrder = orderStored.get();
            savePurchaseOrderPort.save(purchaseOrder);
        } else {
            throw new IllegalArgumentException("Order not found");
        }
    }
}
