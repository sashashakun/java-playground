package com.example.fintech.day3.patterns;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Exercise 07 — Builder Pattern: PaymentRequest
 *
 * Complex object with required + optional fields, built via a fluent builder.
 *
 * Required fields: amount, currency, merchantId, idempotencyKey
 * Optional fields: description (default ""), customerId (default null)
 *
 * TODO: Implement the Builder class.
 *
 *   PaymentRequest request = PaymentRequest.builder()
 *       .amount(new BigDecimal("100.00"))
 *       .currency("USD")
 *       .merchantId("merchant-1")
 *       .idempotencyKey("uuid-123")
 *       .description("Monthly subscription")
 *       .build();  // throws IllegalStateException if required fields are missing
 *
 * build() validation:
 *   - amount must not be null and must be > 0
 *   - currency must not be null/blank
 *   - merchantId must not be null/blank
 *   - idempotencyKey must not be null/blank
 *   Throw IllegalStateException("Missing required field: <fieldName>") for each violation.
 *   Check amount first, then currency, then merchantId, then idempotencyKey.
 */
public final class PaymentRequest {

    private final BigDecimal amount;
    private final String currency;
    private final String merchantId;
    private final String idempotencyKey;
    private final String description;
    private final String customerId;

    private PaymentRequest(Builder b) {
        this.amount         = b.amount;
        this.currency       = b.currency;
        this.merchantId     = b.merchantId;
        this.idempotencyKey = b.idempotencyKey;
        this.description    = b.description == null ? "" : b.description;
        this.customerId     = b.customerId;
    }

    public static Builder builder() { return new Builder(); }

    public BigDecimal getAmount()        { return amount; }
    public String getCurrency()          { return currency; }
    public String getMerchantId()        { return merchantId; }
    public String getIdempotencyKey()    { return idempotencyKey; }
    public String getDescription()       { return description; }
    public String getCustomerId()        { return customerId; }

    public static final class Builder {

        private BigDecimal amount;
        private String currency;
        private String merchantId;
        private String idempotencyKey;
        private String description;
        private String customerId;

        private Builder() {}

        // TODO: Add fluent setter methods (each returns `this`):
        //   amount, currency, merchantId, idempotencyKey, description, customerId

        public Builder amount(BigDecimal amount) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        public Builder currency(String currency) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        public Builder merchantId(String merchantId) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        public Builder idempotencyKey(String idempotencyKey) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        public Builder description(String description) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        public Builder customerId(String customerId) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        /**
         * TODO: Validate required fields and construct the PaymentRequest.
         * Order of checks: amount → currency → merchantId → idempotencyKey
         */
        public PaymentRequest build() {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }
}
