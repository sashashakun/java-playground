package com.example.fintech.day3.soliddip;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Exercise 06 — DIP: High-Level Orchestrator
 *
 * This class coordinates creating, updating, and cancelling payments.
 * It depends ONLY on interfaces — never on concrete implementations.
 *
 * Notice the constructor: dependencies are INJECTED, not created here.
 * This makes the class trivially testable with InMemoryPaymentRepository
 * and LoggingNotification.
 *
 * TODO 1 — createPayment(BigDecimal amount, String currency):
 *   - Generate a UUID id
 *   - Create a Payment with status "PENDING" and Instant.now()
 *   - Save via repository
 *   - Notify with eventType "PAYMENT_CREATED"
 *   - Return the Payment
 *
 * TODO 2 — updateStatus(String paymentId, String newStatus):
 *   - Find by id; throw IllegalArgumentException("Payment not found: " + id) if absent
 *   - Create updated payment with newStatus (use payment.withStatus())
 *   - Save the updated payment
 *   - Notify with eventType "PAYMENT_UPDATED"
 *   - Return the updated Payment
 *
 * TODO 3 — cancelPayment(String paymentId):
 *   - Find by id; throw IllegalArgumentException if absent
 *   - Delete from repository
 *   - Notify with eventType "PAYMENT_CANCELLED"
 *   - Return true
 *
 * TODO 4 — getAllPayments():
 *   - Delegate to repository.findAll()
 */
public class PaymentOrchestrator {

    private final PaymentRepository repository;
    private final NotificationPort notifier;

    public PaymentOrchestrator(PaymentRepository repository, NotificationPort notifier) {
        this.repository = repository;
        this.notifier = notifier;
    }

    public Payment createPayment(BigDecimal amount, String currency) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Payment updateStatus(String paymentId, String newStatus) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean cancelPayment(String paymentId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<Payment> getAllPayments() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
