package com.example.fintech.day7.events;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Exercise 01 — Spring Events: Domain Event records
 *
 * These are plain Java records used as event payloads.
 * Spring's event system works with ANY object — no need to extend ApplicationEvent.
 *
 * TypeScript analogy: typed event payloads in an EventEmitter or NestJS EventBus.
 */
public class PaymentEvents {

    public record PaymentCreatedEvent(
        String paymentId,
        String userId,
        BigDecimal amount,
        String currency,
        Instant occurredAt
    ) {}

    public record PaymentFailedEvent(
        String paymentId,
        String userId,
        String reason,
        Instant occurredAt
    ) {}

    public record PaymentCompletedEvent(
        String paymentId,
        String userId,
        BigDecimal amount,
        String currency,
        Instant occurredAt
    ) {}
}
