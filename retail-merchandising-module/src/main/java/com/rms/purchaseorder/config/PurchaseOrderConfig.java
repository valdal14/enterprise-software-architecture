package com.rms.purchaseorder.config;

import com.rms.purchaseorder.application.PurchaseOrderService;
import com.rms.purchaseorder.application.ports.in.ApprovePurchaseOrderUseCase;
import com.rms.purchaseorder.application.ports.out.LoadPurchaseOrderPort;
import com.rms.purchaseorder.application.ports.out.SavePurchaseOrderPort;
import com.rms.purchaseorder.infrastructure.adapter.out.persistence.PurchaseOrderJpaRepository;
import com.rms.purchaseorder.infrastructure.adapter.out.persistence.PurchaseOrderPersistenceAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

@Configuration
public class PurchaseOrderConfig {

    // A clean, non-intercepted builder for Eureka's internal use
    @Bean
    @Primary
    public RestClient.Builder defaultRestClientBuilder() {
        return RestClient.builder();
    }

    // Load-balanced builder
    @Bean("loadBalancedBuilder")
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilder() {
        return RestClient.builder();
    }

    // Inject the load-balanced builder into the domain client
    @Bean
    public RestClient restClient(
            @Qualifier("loadBalancedBuilder") RestClient.Builder builder,
            @Value("${IMPACT.ANALYTICS.BASE_URL}") String baseURL) {
        return builder.baseUrl(baseURL).build();
    }

    @Bean
    public ApprovePurchaseOrderUseCase approvePurchaseOrderUseCase(
            LoadPurchaseOrderPort loadPurchaseOrderPort,
            SavePurchaseOrderPort savePurchaseOrderPort,
            RestClient restClient) {
        return new PurchaseOrderService(loadPurchaseOrderPort, savePurchaseOrderPort, restClient);
    }

    @Bean
    public PurchaseOrderPersistenceAdapter purchaseOrderPersistenceAdapter(
            PurchaseOrderJpaRepository purchaseOrderJpaRepository) {
        return new PurchaseOrderPersistenceAdapter(purchaseOrderJpaRepository);
    }
}
