package com.rms.purchaseorder.domain;

import com.rms.purchaseorder.application.ports.in.ApprovePurchaseOrderUseCase;
import com.rms.purchaseorder.application.ports.out.LoadPurchaseOrderPort;
import com.rms.purchaseorder.application.ports.out.SavePurchaseOrderPort;
import com.rms.purchaseorder.application.ports.out.TriggerAnalyticsPort;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class PurchaseOrderService implements ApprovePurchaseOrderUseCase {
    private final LoadPurchaseOrderPort loadPurchaseOrderPort;
    private final SavePurchaseOrderPort savePurchaseOrderPort;
    private final TriggerAnalyticsPort triggerAnalyticsPort;

    public PurchaseOrderService(LoadPurchaseOrderPort load, SavePurchaseOrderPort save, TriggerAnalyticsPort triggerAnalyticsPort) {
        this.loadPurchaseOrderPort = load;
        this.savePurchaseOrderPort = save;
        this.triggerAnalyticsPort = triggerAnalyticsPort;
    }

    @Override
    public void execute(UUID orderId, BigDecimal seasonalBudget) {
        Optional<PurchaseOrder> orderStored = loadPurchaseOrderPort.load(orderId);
        if (orderStored.isPresent()) {
            // get the order
            PurchaseOrder purchaseOrder = orderStored.get();
            // approve it
            purchaseOrder.approve(seasonalBudget);
            // send the request to impact analytic service
            boolean syncSuccess = triggerAnalyticsPort.trigger(orderId);
            purchaseOrder.setAnalyticsSynced(syncSuccess);
            // save it
            savePurchaseOrderPort.save(purchaseOrder);
        } else {
            throw new IllegalArgumentException("Order not found");
        }
    }
}
