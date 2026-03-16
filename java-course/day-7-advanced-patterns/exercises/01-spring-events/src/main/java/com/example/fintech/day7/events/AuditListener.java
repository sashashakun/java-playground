package com.example.fintech.day7.events;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 01 — Spring Events: Audit Listener
 *
 * This listener records all payment events for audit purposes.
 * It uses synchronous @EventListener — called in the same thread as the publisher,
 * within the same transaction if one is active.
 *
 * TypeScript analogy: eventBus.on("payment.*", handler)
 *
 * TODO 1: Add @EventListener to `onCreated` so it fires for PaymentCreatedEvent.
 *
 * TODO 2: Add @EventListener to `onCompleted` so it fires for PaymentCompletedEvent.
 *
 * TODO 3: Add @EventListener to `onFailed` so it fires for PaymentFailedEvent.
 *
 * The `auditLog` list is used in tests to verify events were received.
 */
@Component
public class AuditListener {

    private final List<String> auditLog = new ArrayList<>();

    // TODO 1: @EventListener
    public void onCreated(PaymentEvents.PaymentCreatedEvent event) {
        auditLog.add("CREATED:" + event.paymentId() + ":" + event.userId());
    }

    // TODO 2: @EventListener
    public void onCompleted(PaymentEvents.PaymentCompletedEvent event) {
        auditLog.add("COMPLETED:" + event.paymentId() + ":" + event.amount());
    }

    // TODO 3: @EventListener
    public void onFailed(PaymentEvents.PaymentFailedEvent event) {
        auditLog.add("FAILED:" + event.paymentId() + ":" + event.reason());
    }

    public List<String> getAuditLog() {
        return List.copyOf(auditLog);
    }

    public void clear() {
        auditLog.clear();
    }
}
