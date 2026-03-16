package com.example.fintech.day3.abstractclasses;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Exercise 01 — Abstract Classes & Interfaces
 *
 * Part A: Template Method Pattern
 *
 * PaymentGateway is an abstract class that defines the ALGORITHM for processing
 * a payment (authenticate → submit charge → record log entry).
 *
 * Concrete gateways fill in the individual steps.
 *
 * Do NOT modify this class — implement the two concrete gateways below.
 */
public abstract class PaymentGateway {

    /** Immutable record representing a charge request. */
    public record ChargeRequest(String requestId, BigDecimal amount, String currency,
                                String customerId) {
        public ChargeRequest {
            if (amount.compareTo(BigDecimal.ZERO) <= 0)
                throw new IllegalArgumentException("amount must be positive");
        }

        public static ChargeRequest of(BigDecimal amount, String currency, String customerId) {
            return new ChargeRequest(UUID.randomUUID().toString(), amount, currency, customerId);
        }
    }

    /** Returned by the gateway after a charge attempt. */
    public record ChargeResult(String transactionId, boolean success, String message) {}

    /** Returned by the gateway after a refund attempt. */
    public record RefundResult(boolean success, String message) {}

    private final List<String> auditLog = new ArrayList<>();

    // ─── Abstract steps — subclasses must implement ──────────────────────────

    /**
     * TODO (StripeGateway / PayPalGateway): Authenticate with the gateway.
     * Throw RuntimeException if credentials are invalid.
     */
    protected abstract void authenticate();

    /**
     * TODO (StripeGateway / PayPalGateway): Submit the charge to the gateway.
     * Return a ChargeResult indicating success or failure.
     */
    protected abstract ChargeResult submitCharge(ChargeRequest request);

    /**
     * TODO (StripeGateway / PayPalGateway): Human-readable gateway name.
     * e.g. "Stripe", "PayPal"
     */
    public abstract String getGatewayName();

    // ─── Template method — do NOT override ───────────────────────────────────

    /**
     * Process a payment request.
     * Steps: authenticate → submit → log → return result
     *
     * This is the TEMPLATE METHOD — it defines the algorithm structure.
     * Do not override this in subclasses (it is final).
     */
    public final ChargeResult process(ChargeRequest request) {
        authenticate();
        ChargeResult result = submitCharge(request);
        String entry = "[%s] %s: %s %s %s".formatted(
            getGatewayName(),
            result.success() ? "CHARGED" : "FAILED",
            request.amount(), request.currency(),
            result.transactionId()
        );
        auditLog.add(entry);
        return result;
    }

    /** Returns an immutable view of the audit log. */
    public List<String> getAuditLog() {
        return Collections.unmodifiableList(auditLog);
    }
}
