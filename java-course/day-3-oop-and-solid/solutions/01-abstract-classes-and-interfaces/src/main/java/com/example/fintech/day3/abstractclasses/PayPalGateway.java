package com.example.fintech.day3.abstractclasses;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PayPalGateway extends PaymentGateway implements Verifiable {

    private final String clientId;
    private final String clientSecret;
    private final Set<String> processedTransactions = new HashSet<>();

    private static final BigDecimal LIMIT = new BigDecimal("10000");

    public PayPalGateway(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    protected void authenticate() {
        if (clientId == null || clientId.isBlank() || clientSecret == null || clientSecret.isBlank()) {
            throw new IllegalStateException("PayPal: missing credentials");
        }
    }

    @Override
    protected ChargeResult submitCharge(ChargeRequest request) {
        if (request.amount().compareTo(LIMIT) > 0) {
            return new ChargeResult(UUID.randomUUID().toString(), false, "PayPal: amount exceeds limit");
        }
        String txId = UUID.randomUUID().toString();
        processedTransactions.add(txId);
        return new ChargeResult(txId, true, "PayPal charge accepted");
    }

    @Override
    public String getGatewayName() { return "PayPal"; }

    @Override
    public boolean verify(String transactionId) {
        return processedTransactions.contains(transactionId);
    }
}
