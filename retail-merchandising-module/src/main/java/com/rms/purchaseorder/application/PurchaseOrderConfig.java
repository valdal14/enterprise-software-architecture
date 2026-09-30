package com.rms.purchaseorder.application;

import com.rms.purchaseorder.application.ports.in.ApprovePurchaseOrderUseCase;
import com.rms.purchaseorder.application.ports.out.LoadPurchaseOrderPort;
import com.rms.purchaseorder.application.ports.out.SavePurchaseOrderPort;
import com.rms.purchaseorder.infrastructure.adapter.out.persistence.PurchaseOrderJpaRepository;
import com.rms.purchaseorder.infrastructure.adapter.out.persistence.PurchaseOrderPersistenceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PurchaseOrderConfig {

    @Bean
    public ApprovePurchaseOrderUseCase approvePurchaseOrderUseCase(LoadPurchaseOrderPort loadPurchaseOrderPort,  SavePurchaseOrderPort savePurchaseOrderPort) {
        return new PurchaseOrderService(loadPurchaseOrderPort,savePurchaseOrderPort);
    }

    @Bean
    public PurchaseOrderPersistenceAdapter purchaseOrderPersistenceAdapter(PurchaseOrderJpaRepository  purchaseOrderJpaRepository) {
        return new PurchaseOrderPersistenceAdapter(purchaseOrderJpaRepository);
    }
}
