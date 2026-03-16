package com.example.fintech.day6.parameterized;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Exercise 01 — JUnit 5 Parameterized Tests
 *
 * This class is provided in full. Your task is to write parameterized tests
 * that exhaustively verify its behaviour without duplicating test code.
 */
public class PaymentValidator {

    private static final Set<String> SUPPORTED_CURRENCIES = Set.of("USD", "EUR", "GBP", "CHF");
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("50000.00");
    private static final BigDecimal MIN_AMOUNT = new BigDecimal("0.01");

    public record ValidationResult(boolean valid, String errorCode) {
        public static ValidationResult ok() { return new ValidationResult(true, null); }
        public static ValidationResult error(String code) { return new ValidationResult(false, code); }
    }

    public enum ErrorCode {
        AMOUNT_NULL,
        AMOUNT_TOO_LOW,
        AMOUNT_TOO_HIGH,
        CURRENCY_NULL,
        CURRENCY_NOT_SUPPORTED,
        DESCRIPTION_NULL,
        DESCRIPTION_TOO_LONG
    }

    public ValidationResult validate(BigDecimal amount, String currency, String description) {
        if (amount == null) return ValidationResult.error(ErrorCode.AMOUNT_NULL.name());
        if (amount.compareTo(MIN_AMOUNT) < 0) return ValidationResult.error(ErrorCode.AMOUNT_TOO_LOW.name());
        if (amount.compareTo(MAX_AMOUNT) > 0) return ValidationResult.error(ErrorCode.AMOUNT_TOO_HIGH.name());

        if (currency == null) return ValidationResult.error(ErrorCode.CURRENCY_NULL.name());
        if (!SUPPORTED_CURRENCIES.contains(currency)) return ValidationResult.error(ErrorCode.CURRENCY_NOT_SUPPORTED.name());

        if (description == null) return ValidationResult.error(ErrorCode.DESCRIPTION_NULL.name());
        if (description.length() > 200) return ValidationResult.error(ErrorCode.DESCRIPTION_TOO_LONG.name());

        return ValidationResult.ok();
    }

    public boolean isSupportedCurrency(String currency) {
        return currency != null && SUPPORTED_CURRENCIES.contains(currency);
    }
}
