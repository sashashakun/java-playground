package com.example.fintech.day4.profiles;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@Profile("live")
public class LivePaymentGateway implements PaymentGateway {

    @Value("${payment.gateway-url:https://api.example.com}")
    private String gatewayUrl;

    @Override
    public ChargeResult charge(String paymentId, BigDecimal amount, String currency) {
        System.out.println("LiveGateway [" + gatewayUrl + "]: charging " + amount + " " + currency);
        return new ChargeResult("live-" + UUID.randomUUID(), true, "Live charge accepted");
    }

    @Override
    public String getGatewayName() { return "LiveGateway"; }
}
