package com.example.fintech.day5.customqueries;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PaymentRepositoryQueryTest {

    @Autowired
    private PaymentRepository repo;

    @BeforeEach
    void setUp() {
        repo.save(new Payment(new Money(new BigDecimal("100.00"), "USD"), "m1", "p1", "k1"));
        repo.save(new Payment(new Money(new BigDecimal("200.00"), "USD"), "m1", "p2", "k2"));
        repo.save(new Payment(new Money(new BigDecimal("500.00"), "EUR"), "m2", "p3", "k3"));

        Payment completed = new Payment(new Money(new BigDecimal("75.00"), "USD"), "m1", "p4", "k4");
        completed.setStatus(PaymentStatus.COMPLETED);
        repo.save(completed);

        repo.flush();
    }

    @Test
    void findByAmountRange_returnsPaymentsInRange() {
        List<Payment> result = repo.findByAmountRange(
            new BigDecimal("90.00"), new BigDecimal("210.00"));
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(p ->
            p.getAmount().getValue().compareTo(new BigDecimal("90.00")) >= 0 &&
            p.getAmount().getValue().compareTo(new BigDecimal("210.00")) <= 0
        );
    }

    @Test
    void findByMerchantAndStatus_returnsMatchingPayments() {
        List<Payment> result = repo.findByMerchantAndStatus("m1", PaymentStatus.PENDING);
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(p -> p.getMerchantId().equals("m1"));
        assertThat(result).allMatch(p -> p.getStatus() == PaymentStatus.PENDING);
    }

    @Test
    @Transactional
    void expireOldPendingPayments_updatesOldPendingRecords() {
        // set a future cutoff so everything qualifies as "old"
        Instant cutoff = Instant.now().plus(1, ChronoUnit.HOURS);
        int updated = repo.expireOldPendingPayments(cutoff);
        // 3 PENDING payments (p1, p2, p3); p4 is COMPLETED so excluded
        assertThat(updated).isEqualTo(3);
    }

    @Test
    void findSummariesByMerchantId_returnsProjection() {
        List<PaymentSummary> summaries = repo.findSummariesByMerchantId("m1");
        assertThat(summaries).hasSize(3); // p1, p2, p4
        assertThat(summaries).allMatch(s -> s.getMerchantId().equals("m1"));
        // Projection should NOT expose description or idempotencyKey
        assertThat(summaries.get(0).getId()).isNotNull();
        assertThat(summaries.get(0).getAmountValue()).isNotNull();
    }

    @Test
    void countByCurrency_returnsGroupedCounts() {
        List<Object[]> rows = repo.countByCurrency();
        // USD appears 3 times, EUR once
        assertThat(rows).hasSize(2);
        rows.forEach(row -> {
            String currency = (String) row[0];
            Number count = (Number) row[1];
            if ("USD".equals(currency)) {
                assertThat(count.intValue()).isEqualTo(3);
            } else if ("EUR".equals(currency)) {
                assertThat(count.intValue()).isEqualTo(1);
            }
        });
    }
}
