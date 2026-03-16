package com.example.fintech.day3.composition;

@FunctionalInterface
public interface Validator<T> {

    ValidationResult validate(T input);

    default Validator<T> and(Validator<T> other) {
        return input -> {
            ValidationResult first = this.validate(input);
            if (!first.isValid()) return first;
            return other.validate(input);
        };
    }
}
