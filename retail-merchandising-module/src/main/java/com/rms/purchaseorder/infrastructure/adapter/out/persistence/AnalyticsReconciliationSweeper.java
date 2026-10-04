package com.rms.purchaseorder.infrastructure.adapter.out.persistence;

import com.rms.purchaseorder.application.ports.out.LoadPurchaseOrderPort;
import com.rms.purchaseorder.application.ports.out.SavePurchaseOrderPort;
import com.rms.purchaseorder.application.ports.out.TriggerAnalyticsPort;
import com.rms.purchaseorder.domain.OrderStatus;
import com.rms.purchaseorder.domain.PurchaseOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsReconciliationSweeper {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsReconciliationSweeper.class);

    private final LoadPurchaseOrderPort loadPurchaseOrderPort;
    private final SavePurchaseOrderPort savePurchaseOrderPort;
    private final TriggerAnalyticsPort triggerAnalyticsPort;

    public AnalyticsReconciliationSweeper(LoadPurchaseOrderPort load, SavePurchaseOrderPort save, TriggerAnalyticsPort trigger) {
        this.loadPurchaseOrderPort = load;
        this.savePurchaseOrderPort = save;
        this.triggerAnalyticsPort = trigger;
    }

    /**
     * Executes the reconciliation process at a fixed interval.
     * Configured to run every 5 minutes (300,000 milliseconds).
     */
    @Scheduled(fixedDelay = 300000)
    public void reconcileUnsyncedOrders() {
        List<PurchaseOrder> strandedOrders = loadPurchaseOrderPort.loadUnsyncedApprovedOrders(OrderStatus.APPROVED, false);

        if (strandedOrders.isEmpty()) { return; }

        logger.info("Starting the invisible stitching phase for {} unsynced orders...", strandedOrders.size());

        for (PurchaseOrder order : strandedOrders) {
            boolean syncSuccess = triggerAnalyticsPort.trigger(order.getOrderId());

            if (syncSuccess) {
                order.setAnalyticsSynced(true);
                savePurchaseOrderPort.save(order);
                logger.info("Successfully reconciled order ID: {}", order.getOrderId());
            } else {
                logger.warn("Reconciliation still failing for order ID: {}", order.getOrderId());
            }
        }
    }
}
