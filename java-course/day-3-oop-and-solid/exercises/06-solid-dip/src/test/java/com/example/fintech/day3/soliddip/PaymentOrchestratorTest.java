package com.example.fintech.day3.soliddip;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class PaymentOrchestratorTest {

    private InMemoryPaymentRepository repo;
    private LoggingNotification notifier;
    private PaymentOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        repo = new InMemoryPaymentRepository();
        notifier = new LoggingNotification();
        orchestrator = new PaymentOrchestrator(repo, notifier);
    }

    // ─── Repository ───────────────────────────────────────────────────────────

    @Test
    void saveAndFindById() {
        var p = new Payment("p1", new BigDecimal("100"), "USD", "PENDING", java.time.Instant.now());
        repo.save(p);
        assertThat(repo.findById("p1")).isPresent().contains(p);
    }

    @Test
    void findByIdAbsentReturnsEmpty() {
        assertThat(repo.findById("ghost")).isEmpty();
    }

    @Test
    void findAllReturnsAll() {
        repo.save(new Payment("p1", new BigDecimal("100"), "USD", "PENDING", java.time.Instant.now()));
        repo.save(new Payment("p2", new BigDecimal("200"), "EUR", "PENDING", java.time.Instant.now()));
        assertThat(repo.findAll()).hasSize(2);
    }

    @Test
    void deleteReturnsTrueAndRemoves() {
        repo.save(new Payment("p1", new BigDecimal("100"), "USD", "PENDING", java.time.Instant.now()));
        assertThat(repo.delete("p1")).isTrue();
        assertThat(repo.findById("p1")).isEmpty();
    }

    @Test
    void deleteMissingReturnsFalse() {
        assertThat(repo.delete("ghost")).isFalse();
    }

    // ─── Notification ─────────────────────────────────────────────────────────

    @Test
    void loggingNotificationRecordsMessages() {
        var p = new Payment("p1", new BigDecimal("100"), "USD", "PENDING", java.time.Instant.now());
        notifier.notify("PAYMENT_CREATED", p);
        assertThat(notifier.getMessages()).hasSize(1)
            .first().asString().contains("PAYMENT_CREATED").contains("p1");
    }

    // ─── Orchestrator ─────────────────────────────────────────────────────────

    @Test
    void createPaymentPersistsAndNotifies() {
        Payment p = orchestrator.createPayment(new BigDecimal("150"), "USD");
        assertThat(p.status()).isEqualTo("PENDING");
        assertThat(p.amount()).isEqualByComparingTo("150");
        assertThat(repo.findById(p.id())).isPresent();
        assertThat(notifier.getMessages()).hasSize(1)
            .first().asString().contains("PAYMENT_CREATED");
    }

    @Test
    void updateStatusChangesStatusAndNotifies() {
        Payment created = orchestrator.createPayment(new BigDecimal("100"), "EUR");
        Payment updated = orchestrator.updateStatus(created.id(), "COMPLETED");
        assertThat(updated.status()).isEqualTo("COMPLETED");
        assertThat(repo.findById(created.id()).get().status()).isEqualTo("COMPLETED");
        assertThat(notifier.getMessages()).hasSize(2); // created + updated
    }

    @Test
    void updateStatusThrowsForUnknownPayment() {
        assertThatThrownBy(() -> orchestrator.updateStatus("ghost", "COMPLETED"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("ghost");
    }

    @Test
    void cancelPaymentDeletesAndNotifies() {
        Payment created = orchestrator.createPayment(new BigDecimal("100"), "USD");
        assertThat(orchestrator.cancelPayment(created.id())).isTrue();
        assertThat(repo.findById(created.id())).isEmpty();
        assertThat(notifier.getMessages()).hasSize(2); // created + cancelled
    }

    @Test
    void cancelPaymentThrowsForUnknownPayment() {
        assertThatThrownBy(() -> orchestrator.cancelPayment("ghost"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getAllPaymentsDelegatesToRepo() {
        orchestrator.createPayment(new BigDecimal("100"), "USD");
        orchestrator.createPayment(new BigDecimal("200"), "EUR");
        assertThat(orchestrator.getAllPayments()).hasSize(2);
    }
}
