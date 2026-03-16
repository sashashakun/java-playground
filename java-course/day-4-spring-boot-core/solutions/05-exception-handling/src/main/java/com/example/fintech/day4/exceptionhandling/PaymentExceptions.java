package com.example.fintech.day4.exceptionhandling;

import java.math.BigDecimal;

public final class PaymentExceptions {

    private PaymentExceptions() {}

    public static class PaymentNotFoundException extends RuntimeException {
        private final String paymentId;
        public PaymentNotFoundException(String paymentId) {
            super("Payment not found: " + paymentId);
            this.paymentId = paymentId;
        }
        public String getPaymentId() { return paymentId; }
    }

    public static class DuplicatePaymentException extends RuntimeException {
        private final String idempotencyKey;
        public DuplicatePaymentException(String idempotencyKey) {
            super("Duplicate payment for idempotency key: " + idempotencyKey);
            this.idempotencyKey = idempotencyKey;
        }
        public String getIdempotencyKey() { return idempotencyKey; }
    }

    public static class PaymentLimitExceededException extends RuntimeException {
        private final BigDecimal limit;
        private final BigDecimal requested;
        public PaymentLimitExceededException(BigDecimal limit, BigDecimal requested) {
            super("Payment of %s exceeds limit of %s".formatted(requested, limit));
            this.limit = limit;
            this.requested = requested;
        }
        public BigDecimal getLimit()     { return limit; }
        public BigDecimal getRequested() { return requested; }
    }
}
