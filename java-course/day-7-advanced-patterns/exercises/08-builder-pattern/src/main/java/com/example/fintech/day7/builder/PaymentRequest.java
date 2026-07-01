package com.example.fintech.day7.builder;

import java.util.Map;

public final class PaymentRequest {
    private final double amount;
    private final String currency;
    private final String merchantId;
    private final String description;
    private final String idempotencyKey;
    private final Map<String, String> metadata;

    PaymentRequest(
            double amount,
            String currency,
            String merchantId,
            String description,
            String idempotencyKey,
            Map<String, String> metadata) {
        this.amount = amount;
        this.currency = currency;
        this.merchantId = merchantId;
        this.description = description;
        this.idempotencyKey = idempotencyKey;
        this.metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
    }

    public double getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getMerchantId() { return merchantId; }
    public String getDescription() { return description; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public Map<String, String> getMetadata() { return metadata; }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "amount=" + amount +
                ", currency='" + currency + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", description='" + description + '\'' +
                ", idempotencyKey='" + idempotencyKey + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
