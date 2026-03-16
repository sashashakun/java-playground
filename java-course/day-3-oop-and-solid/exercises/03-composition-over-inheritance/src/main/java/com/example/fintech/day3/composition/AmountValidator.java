package com.example.fintech.day3.composition;

import java.math.BigDecimal;

/**
 * Exercise 03 — Amount Validator
 *
 * TODO: Implement validate(PaymentRequest request):
 *   - If request.amount() is null → Invalid("Amount is required")
 *   - If request.amount() <= 0   → Invalid("Amount must be positive")
 *   - Otherwise                  → Valid()
 */
public class AmountValidator implements Validator<PaymentRequest> {

    @Override
    public ValidationResult validate(PaymentRequest request) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
