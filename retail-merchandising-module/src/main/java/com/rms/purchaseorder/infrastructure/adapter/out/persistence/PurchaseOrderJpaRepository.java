package com.rms.purchaseorder.infrastructure.adapter.out.persistence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PurchaseOrderJpaRepository extends JpaRepository<PurchaseOrderJpaEntity, UUID> {
    Optional<PurchaseOrderJpaEntity> findByOrderId(UUID orderId);
}
