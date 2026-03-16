package com.example.fintech.day1.exceptions;

/**
 * Exercise 07 — Exception Handling (Custom Exception 2 of 4)
 *
 * Thrown when a transaction fails validation (bad input data).
 *
 * TODO: Implement this exception class.
 *   - Extend RuntimeException (unchecked)
 *   - Store the field name that failed validation as a private final String
 *   - Constructor: (String fieldName, String reason)
 *     Message format: "Validation failed for '%s': %s".formatted(fieldName, reason)
 *   - Provide getter: getFieldName()
 *
 * Example usage:
 *   throw new TransactionValidationException("amount", "must be positive");
 *   → message: "Validation failed for 'amount': must be positive"
 */
public class TransactionValidationException extends RuntimeException {

    // TODO: add private final String fieldName;

    // TODO: constructor(String fieldName, String reason)

    // TODO: getFieldName()
}
