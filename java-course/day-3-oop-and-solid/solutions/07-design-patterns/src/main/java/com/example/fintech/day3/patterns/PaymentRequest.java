package com.example.fintech.day3.patterns;

import java.math.BigDecimal;

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

    public BigDecimal getAmount()     { return amount; }
    public String getCurrency()       { return currency; }
    public String getMerchantId()     { return merchantId; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public String getDescription()    { return description; }
    public String getCustomerId()     { return customerId; }

    public static final class Builder {
        private BigDecimal amount;
        private String currency;
        private String merchantId;
        private String idempotencyKey;
        private String description;
        private String customerId;

        private Builder() {}

        public Builder amount(BigDecimal amount)             { this.amount = amount;                 return this; }
        public Builder currency(String currency)             { this.currency = currency;             return this; }
        public Builder merchantId(String merchantId)         { this.merchantId = merchantId;         return this; }
        public Builder idempotencyKey(String key)            { this.idempotencyKey = key;            return this; }
        public Builder description(String description)       { this.description = description;       return this; }
        public Builder customerId(String customerId)         { this.customerId = customerId;         return this; }

        public PaymentRequest build() {
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
                throw new IllegalStateException("Missing required field: amount");
            if (currency == null || currency.isBlank())
                throw new IllegalStateException("Missing required field: currency");
            if (merchantId == null || merchantId.isBlank())
                throw new IllegalStateException("Missing required field: merchantId");
            if (idempotencyKey == null || idempotencyKey.isBlank())
                throw new IllegalStateException("Missing required field: idempotencyKey");
            return new PaymentRequest(this);
        }
    }
}
