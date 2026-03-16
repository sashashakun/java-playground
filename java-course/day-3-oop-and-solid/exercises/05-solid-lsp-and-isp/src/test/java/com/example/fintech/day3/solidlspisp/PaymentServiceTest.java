package com.example.fintech.day3.solidlspisp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.example.fintech.day3.solidlspisp.PaymentInterfaces.*;
import static org.assertj.core.api.Assertions.*;

class PaymentServiceTest {

    private FullPaymentService service;

    @BeforeEach
    void setUp() { service = new FullPaymentService(); }

    // ─── FullPaymentService — Chargeable ─────────────────────────────────────

    @Test
    void chargeReturnsSuccessResult() {
        var req = new PaymentRequest("r1", new BigDecimal("100"), "USD");
        var result = service.charge(req);
        assertThat(result.success()).isTrue();
        assertThat(result.transactionId()).isNotBlank();
    }

    @Test
    void chargeRecordsAuditEntry() {
        service.charge(new PaymentRequest("r1", new BigDecimal("100"), "USD"));
        var log = service.getAuditLog(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        assertThat(log).hasSize(1);
    }

    // ─── FullPaymentService — Refundable ─────────────────────────────────────

    @Test
    void refundSucceeds() {
        var result = service.refund("tx-1", new BigDecimal("50"));
        assertThat(result.success()).isTrue();
    }

    @Test
    void refundRejectsZeroAmount() {
        assertThatThrownBy(() -> service.refund("tx-1", BigDecimal.ZERO))
            .isInstanceOf(IllegalArgumentException.class);
    }

    // ─── FullPaymentService — Auditable ──────────────────────────────────────

    @Test
    void auditLogFiltersOutOfRange() {
        service.charge(new PaymentRequest("r1", new BigDecimal("100"), "USD"));
        // future range — no records
        var log = service.getAuditLog(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        assertThat(log).isEmpty();
    }

    @Test
    void auditLogIncludesDateBoundaries() {
        service.charge(new PaymentRequest("r1", new BigDecimal("100"), "USD"));
        var log = service.getAuditLog(LocalDate.now(), LocalDate.now());
        assertThat(log).hasSize(1);
    }

    // ─── ISP: ReadOnlyAuditService ────────────────────────────────────────────

    @Test
    void readOnlyAuditServiceDelegatesToUnderlying() {
        // FullPaymentService is also Auditable — Liskov holds
        service.charge(new PaymentRequest("r1", new BigDecimal("100"), "USD"));
        ReadOnlyAuditService auditSvc = new ReadOnlyAuditService(service);
        var log = auditSvc.getAuditLog(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        assertThat(log).hasSize(1);
    }

    @Test
    void readOnlyAuditServiceImplementsOnlyAuditable() {
        ReadOnlyAuditService auditSvc = new ReadOnlyAuditService(service);
        assertThat(auditSvc).isInstanceOf(Auditable.class);
        assertThat(auditSvc).isNotInstanceOf(Chargeable.class);
        assertThat(auditSvc).isNotInstanceOf(Refundable.class);
    }

    // ─── LSP: Code using interface works with any implementation ─────────────

    @Test
    void chargeableCanBeUsedPolymorphically() {
        List<Chargeable> chargeables = List.of(service);
        for (Chargeable c : chargeables) {
            var result = c.charge(new PaymentRequest("r1", new BigDecimal("200"), "EUR"));
            assertThat(result.success()).isTrue();
        }
    }
}
