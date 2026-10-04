package com.rms.purchaseorder.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class PurchaseOrder {
    private UUID orderId;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    @Setter
    private boolean analyticsSynced = false;

    public PurchaseOrder(UUID orderId, BigDecimal totalAmount, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
    }

    public void approve(BigDecimal seasonalBudget) {
        if(totalAmount.compareTo(seasonalBudget) > 0) {
            this.orderStatus = OrderStatus.REJECTED;
            throw new BudgetExceededException("Seasonal Budget Exceeded");
        }

        this.orderStatus = OrderStatus.APPROVED;
    }
}
