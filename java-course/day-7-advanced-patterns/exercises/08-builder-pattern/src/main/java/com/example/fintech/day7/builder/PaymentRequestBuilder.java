package com.example.fintech.day7.builder;

import java.util.HashMap;
import java.util.Map;

public class PaymentRequestBuilder {
    private Double amount;
    private String currency;
    private String merchantId;
    private String description;
    private String idempotencyKey;
    private Map<String, String> metadata = new HashMap<>();

    public PaymentRequestBuilder amount(double amount) {
        this.amount = amount;
        return this;
    }

    public PaymentRequestBuilder currency(String currency) {
        this.currency = currency;
        return this;
    }

    public PaymentRequestBuilder merchantId(String merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    public PaymentRequestBuilder description(String description) {
        this.description = description;
        return this;
    }

    public PaymentRequestBuilder idempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
        return this;
    }

    public PaymentRequestBuilder metadata(Map<String, String> metadata) {
        this.metadata = new HashMap<>(metadata);
        return this;
    }

    public PaymentRequestBuilder addMetadata(String key, String value) {
        this.metadata.put(key, value);
        return this;
    }

    public PaymentRequest build() {
        if (amount == null) {
            throw new IllegalStateException("amount is required");
        }
        if (amount <= 0) {
            throw new IllegalStateException("amount must be greater than 0, got: " + amount);
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalStateException("currency is required");
        }
        if (currency.length() != 3) {
            throw new IllegalStateException("currency must be exactly 3 characters, got: '" + currency + "'");
        }
        if (merchantId == null || merchantId.isBlank()) {
            throw new IllegalStateException("merchantId is required");
        }
        return new PaymentRequest(amount, currency, merchantId, description, idempotencyKey, metadata);
    }
}
