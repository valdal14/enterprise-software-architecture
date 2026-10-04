package com.rms.purchaseorder.infrastructure.adapter.out.persistence;

import com.rms.purchaseorder.application.ports.out.LoadPurchaseOrderPort;
import com.rms.purchaseorder.application.ports.out.SavePurchaseOrderPort;
import com.rms.purchaseorder.domain.OrderStatus;
import com.rms.purchaseorder.domain.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PurchaseOrderPersistenceAdapter implements LoadPurchaseOrderPort, SavePurchaseOrderPort {
    private final PurchaseOrderJpaRepository  purchaseOrderJpaRepository;

    @Autowired
    public PurchaseOrderPersistenceAdapter(PurchaseOrderJpaRepository purchaseOrderJpaRepository) {
        this.purchaseOrderJpaRepository = purchaseOrderJpaRepository;
    }

    @Override
    public Optional<PurchaseOrder> load(UUID orderId) {
        return purchaseOrderJpaRepository.findByOrderId(orderId)
                .map(orderJpaEntity -> new PurchaseOrder(
                        orderJpaEntity.getOrderId(),
                        orderJpaEntity.getTotalAmount(),
                        orderJpaEntity.getOrderStatus()
                ));
    }

    @Override
    public void save(PurchaseOrder purchaseOrder) {
        PurchaseOrderJpaEntity entity = purchaseOrderJpaRepository.findByOrderId(purchaseOrder.getOrderId())
                .orElseGet(() -> new PurchaseOrderJpaEntity(
                        purchaseOrder.getOrderId(),
                        purchaseOrder.getTotalAmount(),
                        purchaseOrder.getOrderStatus(),
                        purchaseOrder.isAnalyticsSynced()
                ));

        // Update the state (crucial for transitioning from DRAFT to APPROVED/REJECTED)
        entity.setOrderStatus(purchaseOrder.getOrderStatus());
        entity.setTotalAmount(purchaseOrder.getTotalAmount());
        entity.setAnalyticsSynced(purchaseOrder.isAnalyticsSynced());

        purchaseOrderJpaRepository.save(entity);
    }

    @Override
    public List<PurchaseOrder> loadUnsyncedApprovedOrders(OrderStatus orderStatus, boolean synced) {
        List<PurchaseOrderJpaEntity> orderJpaEntities = purchaseOrderJpaRepository.findAllByOrderStatusAndAnalyticsSynced(orderStatus, synced);
        List<PurchaseOrder> purchaseOrders = new ArrayList<>();
        orderJpaEntities.forEach(orderJpaEntity -> purchaseOrders.add(new PurchaseOrder(
                orderJpaEntity.getOrderId(),
                orderJpaEntity.getTotalAmount(),
                orderJpaEntity.getOrderStatus(),
                orderJpaEntity.isAnalyticsSynced())
        ));
        return purchaseOrders;
    }
}
