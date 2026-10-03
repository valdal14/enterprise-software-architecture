package com.rms.purchaseorder.application;

import com.rms.purchaseorder.application.ports.in.ApprovePurchaseOrderUseCase;
import com.rms.purchaseorder.application.ports.out.LoadPurchaseOrderPort;
import com.rms.purchaseorder.application.ports.out.SavePurchaseOrderPort;
import com.rms.purchaseorder.domain.PurchaseOrder;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class PurchaseOrderService implements ApprovePurchaseOrderUseCase {
    private final static String IA_URL_PATH = "/api/v1/export-jobs/{id}/trigger";
    private final LoadPurchaseOrderPort loadPurchaseOrderPort;
    private final SavePurchaseOrderPort savePurchaseOrderPort;
    private final RestClient restClient;

    public PurchaseOrderService(LoadPurchaseOrderPort load, SavePurchaseOrderPort save, RestClient restClient) {
        this.loadPurchaseOrderPort = load;
        this.savePurchaseOrderPort = save;
        this.restClient = restClient;
    }

    @Override
    public void execute(UUID orderId, BigDecimal seasonalBudget) {
        Optional<PurchaseOrder> orderStored = loadPurchaseOrderPort.load(orderId);
        if (orderStored.isPresent()) {
            // get the order
            PurchaseOrder purchaseOrder = orderStored.get();
            // approve it
            purchaseOrder.approve(seasonalBudget);
            // save it
            savePurchaseOrderPort.save(purchaseOrder);
            // send the request to impact analytic service
            restClient.post()
                    .uri(IA_URL_PATH, orderId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(PurchaseOrder.class);
        } else {
            throw new IllegalArgumentException("Order not found");
        }
    }
}
