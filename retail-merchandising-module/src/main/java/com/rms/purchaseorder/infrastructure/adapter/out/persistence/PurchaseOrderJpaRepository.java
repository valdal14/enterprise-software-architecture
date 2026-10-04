package com.rms.purchaseorder.infrastructure.adapter.out.persistence;
import com.rms.purchaseorder.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseOrderJpaRepository extends JpaRepository<PurchaseOrderJpaEntity, UUID> {
    Optional<PurchaseOrderJpaEntity> findByOrderId(UUID orderId);
    List<PurchaseOrderJpaEntity> findAllByOrderStatusAndAnalyticsSynced(OrderStatus orderStatus, boolean synced);
}
