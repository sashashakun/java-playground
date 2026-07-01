package com.example.fintech.day7.decorator;

import java.util.UUID;

public class SimplePaymentService implements PaymentService {
    @Override
    public String processPayment(String merchantId, double amount, String currency) {
        String paymentId = UUID.randomUUID().toString();
        return "Payment processed: " + paymentId;
    }
}
