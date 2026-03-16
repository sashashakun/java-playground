package com.example.fintech.day4.profiles;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Exercise 07 — Live Gateway (only active on "live" profile)
 *
 * TODO 1 — Add @Component + @Profile("live")
 *
 * TODO 2 — Inject the gateway URL using @Value:
 *   @Value("${payment.gateway-url:https://api.example.com}")
 *   private String gatewayUrl;
 *
 * TODO 3 — Implement charge():
 *   - Log: "LiveGateway [" + gatewayUrl + "]: charging " + amount + " " + currency
 *   - Simulate: generate transactionId = "live-" + UUID.randomUUID()
 *   - Return ChargeResult(transactionId, true, "Live charge accepted")
 *
 * TODO 4 — Implement getGatewayName():
 *   - return "LiveGateway"
 */
// TODO 1: @Component @Profile("live")
public class LivePaymentGateway implements PaymentGateway {

    // TODO 2: inject gatewayUrl with @Value

    @Override
    public ChargeResult charge(String paymentId, BigDecimal amount, String currency) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getGatewayName() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
