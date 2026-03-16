package com.example.fintech.day1.exceptions;

/**
 * Exercise 07 — Exception Handling (Custom Exception 1 of 4)
 *
 * Thrown when a withdrawal or transfer exceeds the available balance.
 *
 * Design principle: domain exceptions carry STRUCTURED DATA, not just a message.
 * This allows callers to programmatically inspect what went wrong:
 *   catch (InsufficientFundsException e) {
 *       log.warn("Declined: needed {} but had {}", e.getRequestedCents(), e.getAvailableCents());
 *       return ApiError.insufficientFunds(e.getAvailableCents());
 *   }
 *
 * TODO: Implement this exception class.
 *   - Extend RuntimeException (unchecked — callers don't have to catch it)
 *   - Store availableCents and requestedCents as private final fields
 *   - Constructor should call super() with a generated message:
 *       "Insufficient funds: available %d cents, requested %d cents"
 *       .formatted(availableCents, requestedCents)
 *   - Provide getters for both fields
 */
public class InsufficientFundsException extends RuntimeException {

    // TODO: add private final long availableCents;
    // TODO: add private final long requestedCents;

    // TODO: constructor(long availableCents, long requestedCents)

    // TODO: getAvailableCents()

    // TODO: getRequestedCents()
}
