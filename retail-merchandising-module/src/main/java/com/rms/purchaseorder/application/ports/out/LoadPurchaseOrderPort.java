package com.rms.purchaseorder.application.ports.out;

import com.rms.purchaseorder.domain.PurchaseOrder;

import java.util.Optional;
import java.util.UUID;

@FunctionalInterface
public interface LoadPurchaseOrderPort {
    Optional<PurchaseOrder> load(UUID orderId);
}
