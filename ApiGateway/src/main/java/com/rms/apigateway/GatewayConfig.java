package com.rms.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("impact-analytics-route", r -> r.path("/api/v1/export-jobs/**")
                        .uri("lb://IMPACT-ANALYTICS-SERVICE"))
                .route("purchase-order-route", r -> r.path("/api/v1/orders/**")
                        .uri("lb://PURCHASE-ORDER-SERVICE"))
                .build();
    }
}
