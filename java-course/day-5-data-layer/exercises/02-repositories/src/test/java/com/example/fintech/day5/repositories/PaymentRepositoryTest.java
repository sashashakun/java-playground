package com.example.fintech.day5.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository repo;

    private Payment p1, p2, p3;

    @BeforeEach
    void setUp() {
        p1 = repo.save(new Payment(
            new Money(new BigDecimal("100.00"), "USD"),
            "merchant-A", "Payment 1", "idem-001"));

        p2 = repo.save(new Payment(
            new Money(new BigDecimal("250.00"), "EUR"),
            "merchant-A", "Payment 2", "idem-002"));
        p2.setStatus(PaymentStatus.COMPLETED);
        p2 = repo.save(p2);

        p3 = repo.save(new Payment(
            new Money(new BigDecimal("50.00"), "GBP"),
            "merchant-B", "Payment 3", "idem-003"));
    }

    @Test
    void crudIsAvailableFromJpaRepository() {
        // free from JpaRepository
        Optional<Payment> found = repo.findById(p1.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getMerchantId()).isEqualTo("merchant-A");

        repo.deleteById(p3.getId());
        assertThat(repo.count()).isEqualTo(2);
    }

    @Test
    void findByStatus_returnsPendingPayments() {
        List<Payment> pending = repo.findByStatus(PaymentStatus.PENDING);
        assertThat(pending).hasSize(2);
        assertThat(pending).allMatch(p -> p.getStatus() == PaymentStatus.PENDING);
    }

    @Test
    void findByMerchantId_returnsCorrectPayments() {
        List<Payment> merchantA = repo.findByMerchantId("merchant-A");
        assertThat(merchantA).hasSize(2);
    }

    @Test
    void findByAmountValueGreaterThan_filtersCorrectly() {
        List<Payment> expensive = repo.findByAmount_ValueGreaterThan(new BigDecimal("99.99"));
        // p1 (100), p2 (250) — p3 (50) excluded
        assertThat(expensive).hasSize(2);
        assertThat(expensive).noneMatch(p -> p.getMerchantId().equals("merchant-B"));
    }

    @Test
    void existsByIdempotencyKey_returnsTrueWhenExists() {
        assertThat(repo.existsByIdempotencyKey("idem-001")).isTrue();
        assertThat(repo.existsByIdempotencyKey("idem-999")).isFalse();
    }

    @Test
    void findByMerchantIdOrderByCreatedAtDesc_returnsOrdered() {
        List<Payment> ordered = repo.findByMerchantIdOrderByCreatedAtDesc("merchant-A");
        assertThat(ordered).hasSize(2);
        // most recent first — p2 was saved after p1
        assertThat(ordered.get(0).getId()).isEqualTo(p2.getId());
    }
}
