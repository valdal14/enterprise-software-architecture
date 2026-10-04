package com.rms.purchaseorder.infrastructure.adapter.out.persistence;

import com.rms.purchaseorder.domain.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@Table(name = "purchase_order")
@Entity
public class PurchaseOrderJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ORDER_ID")
    private UUID orderId;
    @Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;
    @Column(name = "ORDER_STATUS")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Column(name = "ANALYTICS_SYNCED")
    private boolean analyticsSynced;

    public PurchaseOrderJpaEntity(UUID orderId, BigDecimal totalAmount, OrderStatus orderStatus, boolean analyticsSynced) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.analyticsSynced = analyticsSynced;
    }
}
