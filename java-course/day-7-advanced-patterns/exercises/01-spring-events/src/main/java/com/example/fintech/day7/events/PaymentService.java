package com.example.fintech.day7.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Exercise 01 — Spring Events
 *
 * PaymentService publishes domain events via ApplicationEventPublisher.
 * Listeners are decoupled — the service doesn't know who handles events.
 *
 * TypeScript analogy: eventBus.emit("payment.created", payload) — but type-safe
 * and Spring-managed (listeners are Spring beans, so they get injected dependencies).
 *
 * This class is fully implemented. Do NOT modify it.
 */
@Service
public class PaymentService {

    private final ApplicationEventPublisher publisher;

    public PaymentService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Transactional
    public String createPayment(String userId, BigDecimal amount, String currency) {
        String paymentId = "pay-" + UUID.randomUUID().toString().substring(0, 8);

        // Publish event — all @EventListeners for PaymentCreatedEvent will be called
        publisher.publishEvent(new PaymentEvents.PaymentCreatedEvent(
            paymentId, userId, amount, currency, Instant.now()));

        return paymentId;
    }

    @Transactional
    public void completePayment(String paymentId, String userId, BigDecimal amount, String currency) {
        publisher.publishEvent(new PaymentEvents.PaymentCompletedEvent(
            paymentId, userId, amount, currency, Instant.now()));
    }

    @Transactional
    public void failPayment(String paymentId, String userId, String reason) {
        publisher.publishEvent(new PaymentEvents.PaymentFailedEvent(
            paymentId, userId, reason, Instant.now()));
    }
}
