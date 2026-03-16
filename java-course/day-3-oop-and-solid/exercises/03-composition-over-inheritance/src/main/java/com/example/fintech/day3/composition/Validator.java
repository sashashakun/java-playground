package com.example.fintech.day3.composition;

/**
 * Exercise 03 — Validator functional interface
 *
 * A single-method interface: validate an input and return Valid or Invalid.
 *
 * TODO: Add a default method `and(Validator<T> other)` that returns a new
 * Validator<T> which runs THIS validator first; if it fails, returns the
 * failure immediately (short-circuit); if it passes, runs OTHER.
 *
 * Example usage:
 *   Validator<PaymentRequest> chain = new AmountValidator()
 *       .and(new CurrencyValidator(Set.of("USD", "EUR")))
 *       .and(new DuplicateValidator());
 *   ValidationResult result = chain.validate(request);
 *
 * Hint:
 *   default Validator<T> and(Validator<T> other) {
 *       return input -> {
 *           ValidationResult first = this.validate(input);
 *           if (!first.isValid()) return first;
 *           return other.validate(input);
 *       };
 *   }
 */
@FunctionalInterface
public interface Validator<T> {

    ValidationResult validate(T input);

    /**
     * TODO: Implement the `and` combinator for short-circuit chaining.
     */
    default Validator<T> and(Validator<T> other) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
