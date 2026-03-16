package com.example.fintech.day1.exceptions;

/** Solution for Exercise 07 */
public class TransactionValidationException extends RuntimeException {

    private final String fieldName;

    public TransactionValidationException(String fieldName, String reason) {
        super("Validation failed for '%s': %s".formatted(fieldName, reason));
        this.fieldName = fieldName;
    }

    public String getFieldName() { return fieldName; }
}
