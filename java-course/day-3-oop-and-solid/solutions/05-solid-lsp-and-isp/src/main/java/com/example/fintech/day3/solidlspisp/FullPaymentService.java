package com.example.fintech.day3.solidlspisp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.fintech.day3.solidlspisp.PaymentInterfaces.*;

public class FullPaymentService implements Chargeable, Refundable, Auditable {

    private final List<PaymentRecord> records = new ArrayList<>();

    @Override
    public PaymentResult charge(PaymentRequest request) {
        String txId = UUID.randomUUID().toString();
        records.add(new PaymentRecord(txId, request.amount(), request.currency(), LocalDate.now()));
        return new PaymentResult(txId, true, "Payment accepted");
    }

    @Override
    public RefundResult refund(String transactionId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Refund amount must be positive");
        return new RefundResult(true, "Refund processed for " + transactionId);
    }

    @Override
    public List<PaymentRecord> getAuditLog(LocalDate from, LocalDate to) {
        return records.stream()
            .filter(r -> !r.date().isBefore(from) && !r.date().isAfter(to))
            .toList();
    }
}
