package com.example.fintech.day3.solidlspisp;

import java.time.LocalDate;
import java.util.List;

import static com.example.fintech.day3.solidlspisp.PaymentInterfaces.*;

/**
 * Exercise 05 — Read-Only Audit Service (ISP demo)
 *
 * This service only needs audit access — no charging or refunding.
 * It ONLY implements Auditable.
 *
 * This is the ISP payoff: you can hand a ReadOnlyAuditService to any code
 * that needs Auditable, without giving it the ability to charge or refund.
 *
 * TODO: Implement getAuditLog(LocalDate from, LocalDate to):
 *   - Delegate to the underlying Auditable (injected via constructor)
 *   - Filter records where date is within [from, to] inclusive
 *
 * LSP exercise: The constructor accepts any Auditable — including FullPaymentService.
 * Code that depends on Auditable works identically for both.
 */
public class ReadOnlyAuditService implements Auditable {

    private final Auditable source;

    public ReadOnlyAuditService(Auditable source) {
        this.source = source;
    }

    @Override
    public List<PaymentRecord> getAuditLog(LocalDate from, LocalDate to) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
