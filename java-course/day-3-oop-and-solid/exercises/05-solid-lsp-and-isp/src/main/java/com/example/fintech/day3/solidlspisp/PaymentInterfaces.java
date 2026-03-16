package com.example.fintech.day3.solidlspisp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Exercise 05 — ISP: Segregated Payment Interfaces
 *
 * Instead of one fat interface, we have three focused ones.
 * Clients depend only on what they need.
 *
 * Provided — no changes needed.
 */
public final class PaymentInterfaces {

    private PaymentInterfaces() {}

    public record PaymentRequest(String id, BigDecimal amount, String currency) {}
    public record PaymentResult(String transactionId, boolean success, String message) {}
    public record RefundResult(boolean success, String message) {}
    public record PaymentRecord(String transactionId, BigDecimal amount, String currency,
                                LocalDate date) {}

    /** Interface 1: can initiate a charge */
    public interface Chargeable {
        PaymentResult charge(PaymentRequest request);
    }

    /** Interface 2: can issue refunds */
    public interface Refundable {
        RefundResult refund(String transactionId, BigDecimal amount);
    }

    /** Interface 3: can provide an audit trail */
    public interface Auditable {
        List<PaymentRecord> getAuditLog(LocalDate from, LocalDate to);
    }
}
