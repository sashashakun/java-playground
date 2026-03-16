package com.example.fintech.day3.soliddip;

/**
 * Exercise 06 — DIP: Notification abstraction ("port")
 *
 * The PaymentOrchestrator sends notifications through this port.
 * It doesn't know (or care) whether the implementation sends email,
 * posts to Slack, or just logs to stdout.
 *
 * Provided — no changes needed.
 */
public interface NotificationPort {

    /**
     * Send a notification about a payment event.
     *
     * @param eventType e.g. "PAYMENT_CREATED", "PAYMENT_UPDATED", "PAYMENT_DELETED"
     * @param payment   the payment that triggered the event
     */
    void notify(String eventType, Payment payment);
}
