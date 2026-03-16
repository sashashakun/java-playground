package com.example.fintech.day1.exceptions;

/** Solution for Exercise 07 */
public class InsufficientFundsException extends RuntimeException {

    private final long availableCents;
    private final long requestedCents;

    public InsufficientFundsException(long availableCents, long requestedCents) {
        super("Insufficient funds: available %d cents, requested %d cents"
            .formatted(availableCents, requestedCents));
        this.availableCents = availableCents;
        this.requestedCents = requestedCents;
    }

    public long getAvailableCents() { return availableCents; }
    public long getRequestedCents() { return requestedCents; }
}
