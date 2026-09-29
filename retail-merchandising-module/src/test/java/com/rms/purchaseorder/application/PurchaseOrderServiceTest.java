package com.rms.purchaseorder.application;

import com.rms.purchaseorder.application.ports.out.LoadPurchaseOrderPort;
import com.rms.purchaseorder.application.ports.out.SavePurchaseOrderPort;
import com.rms.purchaseorder.domain.OrderStatus;
import com.rms.purchaseorder.domain.PurchaseOrder;
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
        PurchaseOrderService service = makeSUT(new LoadService(false), new SaveService());
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

        PurchaseOrderService service = makeSUT(new LoadService(true), saveService);
        // ACT
        service.execute(expectedPurchaseOrder.getOrderId(),  expectedPurchaseOrder.getTotalAmount());
        // VERIFY
        assertAll(
                () -> assertTrue(saveService.verifyCall),
                () -> assertEquals(expectedPurchaseOrder.getOrderId(), saveService.capturedOrder.getOrderId()),
                () -> assertEquals(expectedPurchaseOrder.getTotalAmount(), saveService.capturedOrder.getTotalAmount()),
                () -> assertEquals(expectedPurchaseOrder.getOrderStatus(), saveService.capturedOrder.getOrderStatus())
        );
    }

    /**
     * Helper method used to create the system under test
     * @param load: A LoadPurchaseOrderPort type
     * @param save: A SavePurchaseOrderPort type
     * @return PurchaseOrderService
     */
    private PurchaseOrderService makeSUT(LoadPurchaseOrderPort load, SavePurchaseOrderPort save) {
        return new PurchaseOrderService(load, save);
    }

    /**
     * Helper Mocked LoadService class
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
     * Helper Mocked SaveService class
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
}