package com.rms.purchaseorder.domain;

import com.rms.purchaseorder.application.ports.out.LoadPurchaseOrderPort;
import com.rms.purchaseorder.application.ports.out.SavePurchaseOrderPort;
import com.rms.purchaseorder.application.ports.out.TriggerAnalyticsPort;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseOrderServiceTest {

    @Test
    void executeThrowsIllegalArgumentExceptionWhenTheOrderIsNotFound() {
        // ARRANGE
        UUID orderId = UUID.randomUUID();
        BigDecimal orderAmount = BigDecimal.valueOf(300);
        PurchaseOrderService service = makeSUT(new LoadService(false), new SaveService(), new AnalyticsAdapter(false));
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> service.execute(orderId,  orderAmount));
    }

    @Test
    void executeSuccessfullyProcessPurchaseOrder() {
        // ARRANGE
        UUID orderId = UUID.randomUUID();
        BigDecimal orderAmount = BigDecimal.valueOf(200);
        PurchaseOrder expectedPurchaseOrder = new PurchaseOrder(orderId, orderAmount, OrderStatus.APPROVED);
        SaveService saveService = new SaveService();
        AnalyticsAdapter  analyticsAdapter = new AnalyticsAdapter(false);

        PurchaseOrderService service = makeSUT(new LoadService(true), saveService, analyticsAdapter);
        // ACT
        service.execute(expectedPurchaseOrder.getOrderId(), expectedPurchaseOrder.getTotalAmount());
        // ASSERT
        assertAll(
                () -> assertTrue(saveService.isVerifyCall()),
                () -> assertEquals(expectedPurchaseOrder.getOrderId(), saveService.getCapturedOrder().getOrderId()),
                () -> assertEquals(expectedPurchaseOrder.getTotalAmount(), saveService.getCapturedOrder().getTotalAmount()),
                () -> assertEquals(expectedPurchaseOrder.getOrderStatus(), saveService.getCapturedOrder().getOrderStatus())
        );

        // VERIFY
        assertTrue(analyticsAdapter.verifyTriggerCall);
        assertFalse(analyticsAdapter.verifyFallbackCall);
    }

    @Test
    void executeProcessTheOrderAndFailThePost() {
        // ARRANGE
        UUID orderId = UUID.randomUUID();
        BigDecimal orderAmount = BigDecimal.valueOf(200);
        PurchaseOrder expectedPurchaseOrder = new PurchaseOrder(orderId, orderAmount, OrderStatus.DRAFT);
        SaveService saveService = new SaveService();
        AnalyticsAdapter  analyticsAdapter = new AnalyticsAdapter(true);

        PurchaseOrderService service = makeSUT(new LoadService(true), saveService, analyticsAdapter);
        // ACT
        service.execute(expectedPurchaseOrder.getOrderId(), expectedPurchaseOrder.getTotalAmount());
        // VERIFY
        assertTrue(analyticsAdapter.verifyTriggerCall);
        assertTrue(analyticsAdapter.verifyFallbackCall);
    }

    /**
     * Helper method used to create the system under test
     * @param load: A LoadPurchaseOrderPort type
     * @param save: A SavePurchaseOrderPort type
     * @return PurchaseOrderService
     */
    private PurchaseOrderService makeSUT(LoadPurchaseOrderPort load, SavePurchaseOrderPort save, TriggerAnalyticsPort analytics) {
        return new PurchaseOrderService(load, save, analytics);
    }

    /**
     * Helper Mocked LoadPurchaseOrderPort class
     */
    @AllArgsConstructor
    private static class LoadService implements LoadPurchaseOrderPort {
        private final boolean found;

        @Override
        public Optional<PurchaseOrder> load(UUID orderId) {
            if (found) {
                return Optional.of(new PurchaseOrder(orderId, BigDecimal.valueOf(200), OrderStatus.DRAFT));
            } else {
                return Optional.empty();
            }
        }
    }

    /**
     * Helper Mocked SavePurchaseOrderPort class
     */
    @Getter
    private static class SaveService implements SavePurchaseOrderPort {
        private boolean verifyCall;
        private PurchaseOrder capturedOrder;

        @Override
        public void save(PurchaseOrder purchaseOrder) {
            verifyCall = true;
            capturedOrder = purchaseOrder;
        }
    }

    /**
     * Helper Mocked TriggerAnalyticsPort class
     */
    @Getter
    private static class AnalyticsAdapter implements TriggerAnalyticsPort {
        private boolean verifyTriggerCall;
        private boolean verifyFallbackCall;
        private final boolean shouldFail;

        public AnalyticsAdapter(boolean shouldFail) {
            this.shouldFail = shouldFail;
        }

        @Override
        public boolean trigger(UUID orderId) {
            // Simulate failed or success post request
            verifyTriggerCall  = true;

            if (shouldFail) {
                return triggerFallback(orderId, new Exception("Trigger Request to ImpactAnalytics Service failed"));
            } else
                return true;
            }

        public boolean triggerFallback(UUID orderId, Throwable t) {
            verifyFallbackCall = true;
            System.out.println("ImpactAnalyticsAdapter triggerFallback: " + t.getMessage() + " for orderId: " + orderId);
            return false;
        }
    }
}