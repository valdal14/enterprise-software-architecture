package com.rms.purchaseorder.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseOrderTest {

    @Test
    void approveSuccessfullyApprovesPurchaseOrderWhenTotalAmountIsLessThanSeasonalBudget() {
        // ARRANGE
        UUID id = UUID.randomUUID();
        OrderStatus expectedOrderStatus = OrderStatus.APPROVED;
        PurchaseOrder purchaseOrder = makeSUT(id, BigDecimal.valueOf(300.00));
        // ACT
        purchaseOrder.approve(BigDecimal.valueOf(500.00));
        // ASSERT
        OrderStatus currentStatus = purchaseOrder.getOrderStatus();
        assertEquals(expectedOrderStatus, currentStatus);
    }

    @Test
    void approveThrowsExceptionWhenTotalAmountIsGreaterThanSeasonalBudget() {
        // ARRANGE
        UUID id = UUID.randomUUID();
        OrderStatus expectedOrderStatus = OrderStatus.REJECTED;
        PurchaseOrder purchaseOrder = makeSUT(id, BigDecimal.valueOf(300.00));
        // ACT & ASSERT
        assertThrows(BudgetExceededException.class, () -> purchaseOrder.approve(BigDecimal.valueOf(100.00)));
        assertEquals(expectedOrderStatus, purchaseOrder.getOrderStatus());
    }

    @Test
    void approveSuccessfullyApprovesPurchaseOrderWhenTotalAmountIsEqualThanSeasonalBudget() {
        // ARRANGE
        UUID id = UUID.randomUUID();
        OrderStatus expectedOrderStatus = OrderStatus.APPROVED;
        PurchaseOrder purchaseOrder = makeSUT(id, BigDecimal.valueOf(300.00));
        // ACT
        purchaseOrder.approve(BigDecimal.valueOf(300.00));
        // ASSERT
        OrderStatus currentStatus = purchaseOrder.getOrderStatus();
        assertEquals(expectedOrderStatus, currentStatus);
    }

    private PurchaseOrder makeSUT(UUID orderId, BigDecimal totalAmount) {
        return new PurchaseOrder(orderId, totalAmount, OrderStatus.DRAFT);
    }
}