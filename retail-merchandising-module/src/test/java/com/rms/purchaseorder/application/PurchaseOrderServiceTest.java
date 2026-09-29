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
                System.out.println("Order with id: " + orderId + " found");
                return Optional.of(new PurchaseOrder(orderId, BigDecimal.valueOf(200), OrderStatus.DRAFT));
            } else {
                System.out.println("Order Not Found");
                return Optional.empty();
            }
        }
    }

    /**
     * Helper Mocked SaveService class
     */
    @Getter
    private static class SaveService implements SavePurchaseOrderPort {
        private PurchaseOrder purchaseOrder;

        @Override
        public void save(PurchaseOrder purchaseOrder) {
            System.out.println("Saving purchase order with id: " + purchaseOrder.getOrderId());
            System.out.println("Saving purchase order status: " + purchaseOrder.getOrderStatus());
            this.purchaseOrder = purchaseOrder;
        }
    }
}