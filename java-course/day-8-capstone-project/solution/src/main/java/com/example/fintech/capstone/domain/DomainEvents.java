package com.example.fintech.capstone.domain;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Domain events published via ApplicationEventPublisher.
 * Handlers use @TransactionalEventListener(AFTER_COMMIT) so they
 * only fire when the source transaction successfully commits.
 */
public class DomainEvents {

    public record AccountCreated(String accountId, String ownerId, String currency) {}

    public record FundsTransferred(
        String fromAccountId,
        String toAccountId,
        BigDecimal amount,
        String currency,
        Instant occurredAt
    ) {}

    public record PaymentCreated(String paymentId, String accountId, BigDecimal amount) {}

    public record PaymentCompleted(String paymentId, Instant completedAt) {}

    public record PaymentFailed(String paymentId, String reason) {}
}
