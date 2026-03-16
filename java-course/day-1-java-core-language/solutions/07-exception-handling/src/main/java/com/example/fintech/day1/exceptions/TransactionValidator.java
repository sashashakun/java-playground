package com.example.fintech.day1.exceptions;

import java.util.Set;

/** Solution for Exercise 07 */
public class TransactionValidator {

    private static final Set<String> VALID_CURRENCIES = Set.of(
        "USD", "EUR", "GBP", "CHF", "JPY", "BTC", "ETH"
    );

    private static final long MAX_AMOUNT_CENTS = 10_000_000_00L;

    public void validateAmount(long amountCents) {
        if (amountCents <= 0)
            throw new TransactionValidationException("amount", "must be positive");
        if (amountCents > MAX_AMOUNT_CENTS)
            throw new TransactionValidationException("amount",
                "exceeds maximum allowed amount of " + MAX_AMOUNT_CENTS);
    }

    public void validateCurrency(String currency) {
        if (currency == null || currency.isBlank())
            throw new TransactionValidationException("currency", "must not be blank");
        if (!VALID_CURRENCIES.contains(currency))
            throw new TransactionValidationException("currency",
                "unsupported currency: " + currency);
    }

    public void validateSufficientFunds(long amountCents, long balanceCents) {
        if (amountCents > balanceCents)
            throw new InsufficientFundsException(balanceCents, amountCents);
    }

    public void validateNoDuplicate(String transactionId, Set<String> existingIds) {
        if (existingIds.contains(transactionId))
            throw new DuplicateTransactionException(transactionId);
    }
}
