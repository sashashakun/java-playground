package com.example.fintech.day1.records;

import java.time.Instant;

/** Solution for Exercise 06 — PaymentEvent sealed hierarchy (provided, no changes needed) */
public sealed interface PaymentEvent
    permits PaymentEvent.PaymentInitiated,
            PaymentEvent.PaymentProcessing,
            PaymentEvent.PaymentSettled,
            PaymentEvent.PaymentFailed {

    record PaymentInitiated(String paymentId, Money amount, String senderId)
        implements PaymentEvent {}

    record PaymentProcessing(String paymentId, String gatewayRef)
        implements PaymentEvent {}

    record PaymentSettled(String paymentId, Money amount, Instant settledAt)
        implements PaymentEvent {}

    record PaymentFailed(String paymentId, String reason, int errorCode)
        implements PaymentEvent {}
}
