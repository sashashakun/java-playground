package com.example.fintech.day4.profiles;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Exercise 07 — Mock Gateway (active on "default" and "test" profiles)
 *
 * TODO 1 — Add @Component annotation
 *
 * TODO 2 — Add @Profile annotation:
 *   @Profile({"default", "test"})
 *   This gateway is active when no profile is set (default) or during tests.
 *
 * TODO 3 — Implement charge():
 *   - Generate transactionId = "mock-" + UUID.randomUUID()
 *   - Log to console: "MockGateway: charging " + amount + " " + currency
 *   - Return ChargeResult(transactionId, true, "Mock charge accepted")
 *
 * TODO 4 — Implement getGatewayName():
 *   - return "MockGateway"
 */
// TODO 1: @Component
// TODO 2: @Profile({"default", "test"})
public class MockPaymentGateway implements PaymentGateway {

    @Override
    public ChargeResult charge(String paymentId, BigDecimal amount, String currency) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getGatewayName() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
