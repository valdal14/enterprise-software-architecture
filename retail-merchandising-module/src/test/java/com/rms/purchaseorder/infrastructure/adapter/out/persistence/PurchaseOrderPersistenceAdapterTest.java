package com.rms.purchaseorder.infrastructure.adapter.out.persistence;

import com.rms.purchaseorder.domain.OrderStatus;
import com.rms.purchaseorder.domain.PurchaseOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseOrderPersistenceAdapterTest {

    @Mock
    PurchaseOrderJpaRepository repository;

    @InjectMocks
    PurchaseOrderPersistenceAdapter adapter;

    @Test
    void load() {
        // ARRANGE
        UUID orderId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(300);
        OrderStatus orderStatus = OrderStatus.APPROVED;

        PurchaseOrderJpaEntity mockEntity = new PurchaseOrderJpaEntity();
        mockEntity.setId(1L);
        mockEntity.setOrderId(orderId);
        mockEntity.setTotalAmount(amount);
        mockEntity.setOrderStatus(orderStatus);

        // Mock the dependency (repository)
        when(repository.findByOrderId(orderId)).thenReturn(Optional.of(mockEntity));

        // Define the expected domain model output
        Optional<PurchaseOrder> expectedOrder = Optional.of(new PurchaseOrder(orderId, amount, orderStatus));

        // ACT
        Optional<PurchaseOrder> actualOrder = adapter.load(orderId);

        // ASSERT
        // Check it is present first
        assertTrue(actualOrder.isPresent(), "The purchase order should be present");
        // Then if it is present assert the values of the order
        assertAll(
                ()-> assertEquals(expectedOrder.get().getOrderId(), actualOrder.get().getOrderId()),
                ()-> assertEquals(expectedOrder.get().getTotalAmount(), actualOrder.get().getTotalAmount()),
                ()-> assertEquals(expectedOrder.get().getOrderStatus(), actualOrder.get().getOrderStatus())
        );

        // VERIFY
        verify(repository).findByOrderId(orderId);
    }

    @Test
    void save() {
        // ARRANGE
        UUID orderId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(300);
        OrderStatus orderStatus = OrderStatus.APPROVED;
        PurchaseOrder purchaseOrder = new PurchaseOrder(orderId, amount, orderStatus);

        // Set up the captor to intercept the JPA entity
        ArgumentCaptor<PurchaseOrderJpaEntity> entityCaptor = ArgumentCaptor.forClass(PurchaseOrderJpaEntity.class);

        // ACT
        adapter.save(purchaseOrder);

        // VERIFY
        verify(repository).save(entityCaptor.capture());

        // Extract the captured entity
        PurchaseOrderJpaEntity capturedEntity = entityCaptor.getValue();

        // Assert the internal mapping logic transferred the fields correctly
        assertAll(
                ()-> assertEquals(orderId, capturedEntity.getOrderId()),
                ()-> assertEquals(amount, capturedEntity.getTotalAmount()),
                ()-> assertEquals(orderStatus, capturedEntity.getOrderStatus())
        );
    }

    @Test
    void loadUnsyncedApprovedOrders() {
        // ARRANGE
        List<PurchaseOrderJpaEntity> expectedEntities = new ArrayList<>();
        // Make individual purchase orders
        PurchaseOrderJpaEntity mc1 = new PurchaseOrderJpaEntity();
        mc1.setId(1L);
        mc1.setOrderId(UUID.randomUUID());
        mc1.setTotalAmount(BigDecimal.valueOf(200));
        mc1.setOrderStatus(OrderStatus.APPROVED);
        PurchaseOrderJpaEntity mc2 = new PurchaseOrderJpaEntity();
        mc2.setId(2L);
        mc2.setOrderId(UUID.randomUUID());
        mc2.setTotalAmount(BigDecimal.valueOf(300));
        mc2.setOrderStatus(OrderStatus.APPROVED);

        expectedEntities.add(mc1);
        expectedEntities.add(mc2);

        when(repository.findAllByOrderStatusAndAnalyticsSynced(OrderStatus.APPROVED, false)).thenReturn(expectedEntities);

        // ACT
        List<PurchaseOrder> orders = adapter.loadUnsyncedApprovedOrders(OrderStatus.APPROVED, false);
        // ASSERT
        assertEquals(expectedEntities.size(), orders.size());
    }
}