package com.example.fintech.day7.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Currency;

/**
 * Exercise 05 — Custom Validation: ConstraintValidator
 *
 * ConstraintValidator<A, T>:
 *   A = the annotation type (@ValidCurrency)
 *   T = the type being validated (String)
 *
 * initialize(annotation) — called once; use to read annotation attributes.
 * isValid(value, context) — called for each validation. Return true = valid.
 *
 * Tip: null values should be considered valid here (use @NotNull separately).
 * Tip: Currency.getInstance(code) throws IllegalArgumentException for invalid codes.
 *
 * TODO 5: Implement isValid to return true if value is null OR is a valid ISO-4217 code.
 *         Use Currency.getInstance(value) — catch IllegalArgumentException → return false.
 */
public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

    @Override
    public void initialize(ValidCurrency annotation) {
        // No configuration needed from the annotation
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // TODO 5: implement validation logic
        return true; // placeholder — remove this line
    }
}
