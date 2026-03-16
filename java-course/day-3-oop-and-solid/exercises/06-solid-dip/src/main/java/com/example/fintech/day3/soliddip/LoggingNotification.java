package com.example.fintech.day3.soliddip;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 06 — Logging implementation of NotificationPort
 *
 * Stores notification messages in an in-memory list (useful for testing).
 *
 * TODO: Implement notify(String eventType, Payment payment):
 *   - Build a message: "[EVENT_TYPE] Payment <id>: <amount> <currency>"
 *     e.g. "[PAYMENT_CREATED] Payment pay-1: 100.00 USD"
 *   - Add to the internal messages list
 *
 * Also provide getMessages() that returns an unmodifiable copy.
 */
public class LoggingNotification implements NotificationPort {

    private final List<String> messages = new ArrayList<>();

    @Override
    public void notify(String eventType, Payment payment) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<String> getMessages() {
        return List.copyOf(messages);
    }
}
