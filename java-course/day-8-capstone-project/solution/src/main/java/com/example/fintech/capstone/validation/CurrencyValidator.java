package com.example.fintech.capstone.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Currency;

// SOLUTION N — Validation: CurrencyValidator

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

    @Override
    public void initialize(ValidCurrency annotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // TODO N1 ✓
        if (value == null) return true;
        try {
            Currency.getInstance(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
