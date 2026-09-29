package com.rms.purchaseorder.application.ports.out;

import com.rms.purchaseorder.domain.PurchaseOrder;

@FunctionalInterface
public interface SavePurchaseOrderPort {
    void save(PurchaseOrder purchaseOrder);
}
