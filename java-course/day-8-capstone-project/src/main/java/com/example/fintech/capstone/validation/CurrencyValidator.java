package com.example.fintech.capstone.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Currency;

/**
 * Capstone Exercise N — Validation: CurrencyValidator
 *
 * ConstraintValidator<A, T>:
 *   A = @ValidCurrency  (the annotation)
 *   T = String          (the type being validated)
 *
 * TODO N1: Implement isValid:
 *          - Return true if value is null (null check is @NotBlank's responsibility)
 *          - Try Currency.getInstance(value) — if it succeeds, return true
 *          - If it throws IllegalArgumentException, return false
 */
public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

    @Override
    public void initialize(ValidCurrency annotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // TODO N1: implement
        return true; // placeholder — remove this line
    }
}
