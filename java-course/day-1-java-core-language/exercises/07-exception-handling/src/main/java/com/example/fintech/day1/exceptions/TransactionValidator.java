package com.example.fintech.day1.exceptions;

import java.util.Set;

/**
 * Exercise 07 — Exception Handling (Part 5)
 *
 * Validates transaction data and throws appropriate domain exceptions.
 *
 * Implement the 4 TODO methods. Each method validates one aspect of
 * a transaction and throws the right domain exception on failure.
 */
public class TransactionValidator {

    private static final Set<String> VALID_CURRENCIES = Set.of(
        "USD", "EUR", "GBP", "CHF", "JPY", "BTC", "ETH"
    );

    private static final Set<String> VALID_TYPES = Set.of(
        "DEP", "WDR", "TRI", "TRO", "PUR", "REF", "FEE"
    );

    private static final long MAX_AMOUNT_CENTS = 10_000_000_00L; // $10,000,000.00

    /**
     * TODO 1 — Validate the transaction amount.
     *
     * Rules:
     *   - Must be > 0: throw TransactionValidationException("amount", "must be positive")
     *   - Must not exceed MAX_AMOUNT_CENTS: throw TransactionValidationException(
     *       "amount", "exceeds maximum allowed amount of " + MAX_AMOUNT_CENTS)
     */
    public void validateAmount(long amountCents) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 2 — Validate the currency code.
     *
     * Rules:
     *   - Must not be null or blank: throw TransactionValidationException("currency", "must not be blank")
     *   - Must be in VALID_CURRENCIES: throw TransactionValidationException(
     *       "currency", "unsupported currency: " + currency)
     */
    public void validateCurrency(String currency) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 3 — Validate a withdrawal against the current balance.
     *
     * Rules:
     *   - If amountCents > balanceCents: throw InsufficientFundsException(balanceCents, amountCents)
     *
     * Note: no need to validate amount or balance separately here —
     * assume they are already validated.
     */
    public void validateSufficientFunds(long amountCents, long balanceCents) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 4 — Validate that a transaction ID is not already in use.
     *
     * Rules:
     *   - If existingIds contains transactionId:
     *     throw DuplicateTransactionException(transactionId)
     *
     * @param transactionId  the ID being checked
     * @param existingIds    the set of already-used IDs
     */
    public void validateNoDuplicate(String transactionId, Set<String> existingIds) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
