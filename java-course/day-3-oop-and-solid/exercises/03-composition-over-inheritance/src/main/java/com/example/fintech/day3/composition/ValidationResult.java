package com.example.fintech.day3.composition;

/**
 * Exercise 03 — Sealed result type for validation.
 *
 * Provided — no changes needed.
 */
public sealed interface ValidationResult permits ValidationResult.Valid, ValidationResult.Invalid {

    record Valid() implements ValidationResult {}
    record Invalid(String reason) implements ValidationResult {}

    static ValidationResult ok() { return new Valid(); }
    static ValidationResult fail(String reason) { return new Invalid(reason); }

    default boolean isValid() { return this instanceof Valid; }
    default String getReason() {
        return switch (this) {
            case Valid __ -> "";
            case Invalid(var r) -> r;
        };
    }
}
