package com.rms.purchaseorder.application;

import com.rms.purchaseorder.application.ports.out.LoadPurchaseOrderPort;
import com.rms.purchaseorder.application.ports.out.SavePurchaseOrderPort;
import com.rms.purchaseorder.domain.OrderStatus;
import com.rms.purchaseorder.domain.PurchaseOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PurchaseOrderServiceTest {

    @Test
    void executeThrowsIllegalArgumentExceptionWhenTheOrderIsNotFound() {
        // ARRANGE
        UUID orderId = UUID.randomUUID();
        BigDecimal orderAmount = BigDecimal.valueOf(300);
        RestClient networkClient = mock(RestClient.class);
        PurchaseOrderService service = makeSUT(new LoadService(false), new SaveService(), networkClient);
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

        // Create explicit mocks for each step of the fluent chain
        RestClient mockClient = mock(RestClient.class);
        RestClient.RequestBodyUriSpec mockUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec mockBodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec mockResponseSpec = mock(RestClient.ResponseSpec.class);

        // Wire the chain together to return the next mock in sequence
        when(mockClient.post()).thenReturn(mockUriSpec);
        // Using (Object) any() safely bypasses Mockito's vararg matching issues
        when(mockUriSpec.uri(anyString(), (Object) any())).thenReturn(mockBodySpec);
        when(mockBodySpec.accept(any(MediaType.class))).thenReturn(mockBodySpec);
        when(mockBodySpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.toEntity(PurchaseOrder.class)).thenReturn(ResponseEntity.ok().build());

        PurchaseOrderService service = makeSUT(new LoadService(true), saveService, mockClient);

        // ACT
        service.execute(expectedPurchaseOrder.getOrderId(), expectedPurchaseOrder.getTotalAmount());

        // ASSERT
        assertAll(
                () -> assertTrue(saveService.isVerifyCall()),
                () -> assertEquals(expectedPurchaseOrder.getOrderId(), saveService.getCapturedOrder().getOrderId()),
                () -> assertEquals(expectedPurchaseOrder.getTotalAmount(), saveService.getCapturedOrder().getTotalAmount()),
                () -> assertEquals(expectedPurchaseOrder.getOrderStatus(), saveService.getCapturedOrder().getOrderStatus())
        );

        // VERIFY
        verify(mockClient, times(1)).post();
        verify(mockUriSpec, times(1)).uri("/api/v1/export-jobs/{id}/trigger", expectedPurchaseOrder.getOrderId());
    }

    /**
     * Helper method used to create the system under test
     * @param load: A LoadPurchaseOrderPort type
     * @param save: A SavePurchaseOrderPort type
     * @return PurchaseOrderService
     */
    private PurchaseOrderService makeSUT(LoadPurchaseOrderPort load, SavePurchaseOrderPort save, RestClient restClient) {
        return new PurchaseOrderService(load, save, restClient);
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