package com.example.fintech.day3.composition;

import java.util.Set;

/**
 * Exercise 03 — Currency Validator
 *
 * TODO: Implement validate(PaymentRequest request):
 *   - If request.currency() is null or blank → Invalid("Currency is required")
 *   - If request.currency() is NOT in supportedCurrencies → Invalid("Unsupported currency: XYZ")
 *   - Otherwise → Valid()
 */
public class CurrencyValidator implements Validator<PaymentRequest> {

    private final Set<String> supportedCurrencies;

    public CurrencyValidator(Set<String> supportedCurrencies) {
        this.supportedCurrencies = Set.copyOf(supportedCurrencies);
    }

    @Override
    public ValidationResult validate(PaymentRequest request) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
