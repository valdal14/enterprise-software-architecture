package com.rms.purchaseorder.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class PurchaseOrder {
    private UUID orderId;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;

    public void approve(BigDecimal seasonalBudget) {
        if(totalAmount.compareTo(seasonalBudget) > 0) {
            this.orderStatus = OrderStatus.REJECTED;
            throw new BudgetExceededException("Seasonal Budget Exceeded");
        }

        this.orderStatus = OrderStatus.APPROVED;
    }
}
