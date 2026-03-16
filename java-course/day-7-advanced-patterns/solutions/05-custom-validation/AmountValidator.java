package com.example.fintech.day7.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;
import java.util.Map;

// SOLUTION 05 — Custom Validation: AmountValidator (cross-field)

public class AmountValidator implements ConstraintValidator<ValidAmount, PaymentRequest> {

    private static final Map<String, BigDecimal> MAX_AMOUNTS = Map.of(
        "USD", new BigDecimal("10000"),
        "EUR", new BigDecimal("8000"),
        "JPY", new BigDecimal("1000000")
    );
    private static final BigDecimal DEFAULT_MAX = new BigDecimal("5000");

    @Override
    public boolean isValid(PaymentRequest request, ConstraintValidatorContext context) {
        // TODO 6 ✓
        if (request == null) return true;
        if (request.amount() == null || request.currency() == null) return true;

        BigDecimal amount = request.amount();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) return false;

        BigDecimal max = MAX_AMOUNTS.getOrDefault(request.currency(), DEFAULT_MAX);
        return amount.compareTo(max) <= 0;
    }
}
