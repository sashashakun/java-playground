package com.example.fintech.day7.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Exercise 05 — Custom Validation: Cross-field validator
 *
 * This validator receives the whole PaymentRequest (class-level constraint).
 * It can read multiple fields and validate them together.
 *
 * Rules:
 * - Amount must be > 0
 * - Amount must be <= the max for the given currency:
 *     USD → 10,000
 *     EUR → 8,000
 *     JPY → 1,000,000
 *     (all others) → 5,000
 *
 * TODO 6: Implement isValid to enforce the rules above.
 *         Return true if request is null (defensive guard).
 *         Access request.amount() and request.currency().
 */
public class AmountValidator implements ConstraintValidator<ValidAmount, PaymentRequest> {

    private static final Map<String, BigDecimal> MAX_AMOUNTS = Map.of(
        "USD", new BigDecimal("10000"),
        "EUR", new BigDecimal("8000"),
        "JPY", new BigDecimal("1000000")
    );
    private static final BigDecimal DEFAULT_MAX = new BigDecimal("5000");

    @Override
    public boolean isValid(PaymentRequest request, ConstraintValidatorContext context) {
        // TODO 6: implement cross-field validation
        return true; // placeholder — remove this line
    }
}
