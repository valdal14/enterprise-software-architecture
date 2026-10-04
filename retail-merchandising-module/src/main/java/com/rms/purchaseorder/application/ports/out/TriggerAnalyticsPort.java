package com.rms.purchaseorder.application.ports.out;

import java.util.UUID;

@FunctionalInterface
public interface TriggerAnalyticsPort {
    boolean trigger(UUID orderId);
}
