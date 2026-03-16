package com.example.fintech.day4.beanvalidation;

import java.util.List;

/**
 * Provided — the JSON shape returned for 400 validation errors.
 */
public record ValidationErrorResponse(String message, List<FieldError> errors) {
    public record FieldError(String field, String message) {}
}
