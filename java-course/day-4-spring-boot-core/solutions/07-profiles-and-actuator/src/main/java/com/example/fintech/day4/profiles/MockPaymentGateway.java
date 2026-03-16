package com.example.fintech.day4.profiles;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@Profile({"default", "test"})
public class MockPaymentGateway implements PaymentGateway {

    @Override
    public ChargeResult charge(String paymentId, BigDecimal amount, String currency) {
        System.out.println("MockGateway: charging " + amount + " " + currency);
        return new ChargeResult("mock-" + UUID.randomUUID(), true, "Mock charge accepted");
    }

    @Override
    public String getGatewayName() { return "MockGateway"; }
}
