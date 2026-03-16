package com.example.fintech.day3.composition;

import java.util.HashSet;
import java.util.Set;

public class DuplicateValidator implements Validator<PaymentRequest> {
    private final Set<String> seenIds = new HashSet<>();

    @Override
    public ValidationResult validate(PaymentRequest request) {
        if (request.id() == null || request.id().isBlank())
            return ValidationResult.fail("Request ID is required");
        if (!seenIds.add(request.id()))
            return ValidationResult.fail("Duplicate request: " + request.id());
        return ValidationResult.ok();
    }
}
