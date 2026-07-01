package com.example.fintech.capstone.exception;

public class CurrencyMismatchException extends RuntimeException {
    public CurrencyMismatchException(String fromCurrency, String toCurrency) {
        super("Currency mismatch: from=%s, to=%s".formatted(fromCurrency, toCurrency));
    }
}
