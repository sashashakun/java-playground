package com.example.fintech.day1.records;

import java.time.Instant;

/**
 * Exercise 06 — Records & Sealed Classes (Part 2 of 3)
 *
 * A sealed interface representing all possible states of a payment lifecycle.
 * This is Java's answer to TypeScript's discriminated unions.
 *
 * TypeScript equivalent:
 *   type PaymentEvent =
 *     | { type: "INITIATED";   paymentId: string; amount: Money }
 *     | { type: "PROCESSING";  paymentId: string; gatewayRef: string }
 *     | { type: "SETTLED";     paymentId: string; amount: Money; settledAt: Date }
 *     | { type: "FAILED";      paymentId: string; reason: string; errorCode: number };
 *
 * Java sealed interface:
 *   - 'sealed' keyword means: only the listed 'permits' classes can implement this
 *   - Compiler uses this for exhaustiveness checking in switch expressions
 *   - Each subtype is a 'record' — immutable, value-based
 *
 * NO TODOs here — this is provided code. Read it carefully.
 * The PaymentEventProcessor class (Part 3) has the TODOs.
 */
public sealed interface PaymentEvent
    permits PaymentEvent.PaymentInitiated,
            PaymentEvent.PaymentProcessing,
            PaymentEvent.PaymentSettled,
            PaymentEvent.PaymentFailed {

    /** Payment has been submitted but not yet sent to the gateway. */
    record PaymentInitiated(String paymentId, Money amount, String senderId)
        implements PaymentEvent {}

    /** Payment has been sent to the gateway and is being processed. */
    record PaymentProcessing(String paymentId, String gatewayRef)
        implements PaymentEvent {}

    /** Payment has been successfully settled. */
    record PaymentSettled(String paymentId, Money amount, Instant settledAt)
        implements PaymentEvent {}

    /** Payment failed at the gateway or due to validation. */
    record PaymentFailed(String paymentId, String reason, int errorCode)
        implements PaymentEvent {}
}
