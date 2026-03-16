package com.example.fintech.day3.solidlspisp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.fintech.day3.solidlspisp.PaymentInterfaces.*;

/**
 * Exercise 05 — Full Payment Service
 *
 * Implements all three interfaces: Chargeable, Refundable, Auditable.
 *
 * TODO 1 — charge(PaymentRequest request):
 *   - Generate a UUID transactionId
 *   - Record a PaymentRecord(transactionId, amount, currency, LocalDate.now())
 *   - Return PaymentResult(transactionId, true, "Payment accepted")
 *
 * TODO 2 — refund(String transactionId, BigDecimal amount):
 *   - If amount <= 0 → throw IllegalArgumentException("Refund amount must be positive")
 *   - Return RefundResult(true, "Refund processed for " + transactionId)
 *
 * TODO 3 — getAuditLog(LocalDate from, LocalDate to):
 *   - Return all PaymentRecords where record.date() is within [from, to] inclusive
 *   - Filter the internal list; return as a new List
 */
public class FullPaymentService implements Chargeable, Refundable, Auditable {

    private final List<PaymentRecord> records = new ArrayList<>();

    @Override
    public PaymentResult charge(PaymentRequest request) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public RefundResult refund(String transactionId, BigDecimal amount) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public List<PaymentRecord> getAuditLog(LocalDate from, LocalDate to) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
