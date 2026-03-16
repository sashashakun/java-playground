package com.example.fintech.day6.mockito;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Exercise 02 — Mockito
 *
 * This service orchestrates a payment: fraud check → process → audit → notify.
 * All dependencies are injected via the constructor (easy to mock in tests).
 *
 * Your task is to write tests in PaymentProcessorTest.java.
 * This class is provided in full — do NOT modify it.
 */
@Service
public class PaymentProcessor {

    private final FraudDetector fraudDetector;
    private final AuditLog auditLog;
    private final NotificationPort notification;

    public PaymentProcessor(FraudDetector fraudDetector,
                            AuditLog auditLog,
                            NotificationPort notification) {
        this.fraudDetector = fraudDetector;
        this.auditLog = auditLog;
        this.notification = notification;
    }

    public PaymentResult process(String userId, BigDecimal amount, String currency) {
        if (fraudDetector.isSuspicious(userId, amount, currency)) {
            auditLog.record("FRAUD_BLOCKED", userId,
                "Blocked transaction: " + amount + " " + currency);
            throw new FraudException("Transaction blocked by fraud detector for user: " + userId);
        }

        String txId = "txn-" + UUID.randomUUID().toString().substring(0, 8);
        PaymentResult result = new PaymentResult(txId, userId, amount, currency, "COMPLETED", Instant.now());

        auditLog.record("PAYMENT_PROCESSED", userId,
            "txId=" + txId + " amount=" + amount + " currency=" + currency);

        notification.notify(userId,
            "Payment processed",
            "Your payment of " + amount + " " + currency + " was successful. Ref: " + txId);

        return result;
    }

    public static class FraudException extends RuntimeException {
        public FraudException(String message) { super(message); }
    }
}
