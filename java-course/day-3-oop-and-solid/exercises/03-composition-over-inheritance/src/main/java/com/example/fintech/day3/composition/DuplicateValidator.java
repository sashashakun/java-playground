package com.example.fintech.day3.composition;

import java.util.HashSet;
import java.util.Set;

/**
 * Exercise 03 — Duplicate ID Validator
 *
 * Keeps an internal Set of seen request IDs.
 *
 * TODO: Implement validate(PaymentRequest request):
 *   - If request.id() is null or blank → Invalid("Request ID is required")
 *   - If the id has been seen before   → Invalid("Duplicate request: <id>")
 *   - Otherwise → add to seen set, return Valid()
 */
public class DuplicateValidator implements Validator<PaymentRequest> {

    private final Set<String> seenIds = new HashSet<>();

    @Override
    public ValidationResult validate(PaymentRequest request) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
