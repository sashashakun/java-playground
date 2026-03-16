package com.example.fintech.day3.soliddip;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class PaymentOrchestrator {

    private final PaymentRepository repository;
    private final NotificationPort notifier;

    public PaymentOrchestrator(PaymentRepository repository, NotificationPort notifier) {
        this.repository = repository;
        this.notifier = notifier;
    }

    public Payment createPayment(BigDecimal amount, String currency) {
        var payment = new Payment(UUID.randomUUID().toString(), amount, currency, "PENDING", Instant.now());
        repository.save(payment);
        notifier.notify("PAYMENT_CREATED", payment);
        return payment;
    }

    public Payment updateStatus(String paymentId, String newStatus) {
        var payment = repository.findById(paymentId)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));
        var updated = payment.withStatus(newStatus);
        repository.save(updated);
        notifier.notify("PAYMENT_UPDATED", updated);
        return updated;
    }

    public boolean cancelPayment(String paymentId) {
        var payment = repository.findById(paymentId)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));
        repository.delete(paymentId);
        notifier.notify("PAYMENT_CANCELLED", payment);
        return true;
    }

    public List<Payment> getAllPayments() {
        return repository.findAll();
    }
}
