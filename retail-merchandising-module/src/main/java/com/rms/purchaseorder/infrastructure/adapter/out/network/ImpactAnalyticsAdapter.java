package com.rms.purchaseorder.infrastructure.adapter.out.network;

import com.rms.purchaseorder.application.ports.out.TriggerAnalyticsPort;
import com.rms.purchaseorder.domain.PurchaseOrder;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@AllArgsConstructor
public class ImpactAnalyticsAdapter implements TriggerAnalyticsPort {
    private final static String IA_URL_PATH = "/api/v1/export-jobs/{id}/trigger";
    private final RestClient restClient;

    @Override
    @CircuitBreaker(name = "impactAnalytics", fallbackMethod = "triggerFallback")
    public boolean trigger(UUID orderId) {
        // send the request to impact analytic service
        HttpStatusCode code = restClient.post()
                .uri(IA_URL_PATH, orderId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(PurchaseOrder.class)
                .getStatusCode();

        return code == HttpStatus.ACCEPTED;
    }

    public boolean triggerFallback(UUID orderId, Throwable t) {
        System.out.println("ImpactAnalyticsAdapter triggerFallback: " + t.getMessage() + " for orderId: " + orderId);
        return false;
    }
}
