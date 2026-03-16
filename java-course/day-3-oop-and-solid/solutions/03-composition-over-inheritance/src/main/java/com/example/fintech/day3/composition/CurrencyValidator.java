package com.example.fintech.day3.composition;

import java.util.Set;

public class CurrencyValidator implements Validator<PaymentRequest> {
    private final Set<String> supportedCurrencies;

    public CurrencyValidator(Set<String> supportedCurrencies) {
        this.supportedCurrencies = Set.copyOf(supportedCurrencies);
    }

    @Override
    public ValidationResult validate(PaymentRequest request) {
        if (request.currency() == null || request.currency().isBlank())
            return ValidationResult.fail("Currency is required");
        if (!supportedCurrencies.contains(request.currency()))
            return ValidationResult.fail("Unsupported currency: " + request.currency());
        return ValidationResult.ok();
    }
}
