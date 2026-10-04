package com.rms.purchaseorder.application.ports.out;

import com.rms.purchaseorder.domain.OrderStatus;
import com.rms.purchaseorder.domain.PurchaseOrder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadPurchaseOrderPort {
    Optional<PurchaseOrder> load(UUID orderId);
    List<PurchaseOrder> loadUnsyncedApprovedOrders(OrderStatus orderStatus, boolean synced);
}
