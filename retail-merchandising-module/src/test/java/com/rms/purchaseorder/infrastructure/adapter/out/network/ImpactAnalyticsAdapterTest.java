package com.rms.purchaseorder.infrastructure.adapter.out.network;

import com.rms.purchaseorder.domain.PurchaseOrder;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImpactAnalyticsAdapterTest {

    @Test
    void triggerReturnsTrueOnSuccessfulNetworkCall() {
        // ARRANGE
        UUID orderId = UUID.randomUUID();

        // Create explicit mocks for each step of the RestClient chain
        RestClient mockClient = mock(RestClient.class);
        RestClient.RequestBodyUriSpec mockUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec mockBodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec mockResponseSpec = mock(RestClient.ResponseSpec.class);

        // Wire the mock chain together
        when(mockClient.post()).thenReturn(mockUriSpec);
        when(mockUriSpec.uri(anyString(), (Object) any())).thenReturn(mockBodySpec);
        when(mockBodySpec.accept(MediaType.APPLICATION_JSON)).thenReturn(mockBodySpec);
        when(mockBodySpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.toEntity(PurchaseOrder.class)).thenReturn(ResponseEntity.accepted().build());

        ImpactAnalyticsAdapter adapter = new ImpactAnalyticsAdapter(mockClient);

        // ACT
        boolean result = adapter.trigger(orderId);

        // ASSERT & VERIFY
        assertTrue(result, "Adapter should return true on a successful HTTP call");

        // Verify the network client was instructed to hit the exact correct URI
        verify(mockUriSpec, times(1)).uri("/api/v1/export-jobs/{id}/trigger", orderId);
    }

    @Test
    void triggerReturnsFalseOnFailureNetworkCall() {
        // ARRANGE
        UUID orderId = UUID.randomUUID();

        // Create explicit mocks for each step of the RestClient chain
        RestClient mockClient = mock(RestClient.class);
        RestClient.RequestBodyUriSpec mockUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec mockBodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec mockResponseSpec = mock(RestClient.ResponseSpec.class);

        // Wire the mock chain together
        when(mockClient.post()).thenReturn(mockUriSpec);
        when(mockUriSpec.uri(anyString(), (Object) any())).thenReturn(mockBodySpec);
        when(mockBodySpec.accept(MediaType.APPLICATION_JSON)).thenReturn(mockBodySpec);
        when(mockBodySpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.toEntity(PurchaseOrder.class)).thenReturn(ResponseEntity.badRequest().build());

        ImpactAnalyticsAdapter adapter = new ImpactAnalyticsAdapter(mockClient);

        // ACT
        boolean result = adapter.trigger(orderId);

        // ASSERT & VERIFY
        assertFalse(result, "Adapter should return true on a successful HTTP call");

        // Verify the network client was instructed to hit the exact correct URI
        verify(mockUriSpec, times(1)).uri("/api/v1/export-jobs/{id}/trigger", orderId);
    }
}