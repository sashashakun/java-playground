package com.example.fintech.day1.exceptions;

/**
 * Exercise 07 — Exception Handling (Custom Exception 3 of 4)
 *
 * Thrown when a transaction with the same ID is submitted more than once.
 * Idempotency keys are critical in payment systems.
 *
 * TODO: Implement this exception class.
 *   - Extend RuntimeException (unchecked)
 *   - Store the duplicate transactionId as a private final String
 *   - Constructor: (String transactionId)
 *     Message: "Duplicate transaction: " + transactionId
 *   - Provide getter: getTransactionId()
 */
public class DuplicateTransactionException extends RuntimeException {

    // TODO: add private final String transactionId;

    // TODO: constructor(String transactionId)

    // TODO: getTransactionId()
}
