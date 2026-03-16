package com.example.fintech.day4.exceptionhandling;

import java.math.BigDecimal;

/**
 * Exercise 05 — Custom Exception Hierarchy
 *
 * All exceptions are RuntimeExceptions (unchecked) — Spring controllers
 * do not need try/catch; the @ControllerAdvice handles them globally.
 *
 * TODO 1 — PaymentNotFoundException:
 *   - Extend RuntimeException
 *   - Constructor: PaymentNotFoundException(String paymentId)
 *   - Message: "Payment not found: " + paymentId
 *   - Getter: getPaymentId()
 *
 * TODO 2 — DuplicatePaymentException:
 *   - Extend RuntimeException
 *   - Constructor: DuplicatePaymentException(String idempotencyKey)
 *   - Message: "Duplicate payment for idempotency key: " + idempotencyKey
 *   - Getter: getIdempotencyKey()
 *
 * TODO 3 — PaymentLimitExceededException:
 *   - Extend RuntimeException
 *   - Constructor: PaymentLimitExceededException(BigDecimal limit, BigDecimal requested)
 *   - Message: "Payment of %s exceeds limit of %s".formatted(requested, limit)
 *   - Getters: getLimit(), getRequested()
 */
public final class PaymentExceptions {

    private PaymentExceptions() {}

    // TODO 1
    public static class PaymentNotFoundException extends RuntimeException {
        // Implement here
    }

    // TODO 2
    public static class DuplicatePaymentException extends RuntimeException {
        // Implement here
    }

    // TODO 3
    public static class PaymentLimitExceededException extends RuntimeException {
        // Implement here
    }
}
