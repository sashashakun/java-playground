package com.example.fintech.day7.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Currency;

// SOLUTION 05 — Custom Validation: CurrencyValidator

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

    @Override
    public void initialize(ValidCurrency annotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // TODO 5 ✓
        if (value == null) return true;  // null check is @NotNull's job
        try {
            Currency.getInstance(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
