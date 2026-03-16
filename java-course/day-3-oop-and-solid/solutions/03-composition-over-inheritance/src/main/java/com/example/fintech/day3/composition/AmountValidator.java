package com.example.fintech.day3.composition;

import java.math.BigDecimal;

public class AmountValidator implements Validator<PaymentRequest> {
    @Override
    public ValidationResult validate(PaymentRequest request) {
        if (request.amount() == null) return ValidationResult.fail("Amount is required");
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0)
            return ValidationResult.fail("Amount must be positive");
        return ValidationResult.ok();
    }
}
