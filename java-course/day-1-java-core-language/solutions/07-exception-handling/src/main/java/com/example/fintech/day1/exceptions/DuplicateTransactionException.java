package com.example.fintech.day1.exceptions;

/** Solution for Exercise 07 */
public class DuplicateTransactionException extends RuntimeException {

    private final String transactionId;

    public DuplicateTransactionException(String transactionId) {
        super("Duplicate transaction: " + transactionId);
        this.transactionId = transactionId;
    }

    public String getTransactionId() { return transactionId; }
}
