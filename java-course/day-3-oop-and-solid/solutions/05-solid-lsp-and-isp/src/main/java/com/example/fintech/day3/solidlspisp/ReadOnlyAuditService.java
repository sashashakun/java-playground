package com.example.fintech.day3.solidlspisp;

import java.time.LocalDate;
import java.util.List;

import static com.example.fintech.day3.solidlspisp.PaymentInterfaces.*;

public class ReadOnlyAuditService implements Auditable {

    private final Auditable source;

    public ReadOnlyAuditService(Auditable source) {
        this.source = source;
    }

    @Override
    public List<PaymentRecord> getAuditLog(LocalDate from, LocalDate to) {
        return source.getAuditLog(from, to);
    }
}
