package com.example.fintech.day3.abstractclasses;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StripeGateway extends PaymentGateway implements Refundable, Verifiable {

    private final String apiKey;
    private final Set<String> processedTransactions = new HashSet<>();

    public StripeGateway(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    protected void authenticate() {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Stripe: missing API key");
        }
    }

    @Override
    protected ChargeResult submitCharge(ChargeRequest request) {
        if ("XYZ".equals(request.currency())) {
            return new ChargeResult(UUID.randomUUID().toString(), false, "Unsupported currency");
        }
        String txId = UUID.randomUUID().toString();
        processedTransactions.add(txId);
        return new ChargeResult(txId, true, "Stripe charge accepted");
    }

    @Override
    public String getGatewayName() { return "Stripe"; }

    @Override
    public RefundResult refund(String transactionId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Refund amount must be positive");
        }
        return new RefundResult(true, "Stripe refund processed");
    }

    @Override
    public boolean verify(String transactionId) {
        return processedTransactions.contains(transactionId);
    }
}
