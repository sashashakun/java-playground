package com.example.fintech.day5.pagination;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PaginationTest {

    @Autowired
    private PaymentRepository repo;

    @BeforeEach
    void setUp() {
        // Seed 10 PENDING + 5 COMPLETED payments for merchant-A
        IntStream.rangeClosed(1, 10).forEach(i ->
            repo.save(new Payment(
                new Money(new BigDecimal(i * 10 + ".00"), "USD"),
                "merchant-A", "Payment " + i, "pend-" + i)));

        IntStream.rangeClosed(1, 5).forEach(i -> {
            Payment p = new Payment(
                new Money(new BigDecimal(i * 100 + ".00"), "USD"),
                "merchant-A", "Big payment " + i, "big-" + i);
            p.setStatus(PaymentStatus.COMPLETED);
            repo.save(p);
        });

        // 3 more from a different merchant
        IntStream.rangeClosed(1, 3).forEach(i ->
            repo.save(new Payment(
                new Money(new BigDecimal("50.00"), "EUR"),
                "merchant-B", "B payment " + i, "b-" + i)));
    }

    @Test
    void findByStatus_paginationReturnsCorrectPage() {
        // Request page 0 with 3 items, sorted by createdAt ascending
        Pageable pageable = PageRequest.of(0, 3, Sort.by("createdAt").ascending());
        Page<Payment> page = repo.findByStatus(PaymentStatus.PENDING, pageable);

        assertThat(page.getContent()).hasSize(3);
        assertThat(page.getTotalElements()).isEqualTo(13); // 10 merchant-A + 3 merchant-B
        assertThat(page.getTotalPages()).isEqualTo(5);     // ceil(13/3)
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    void findByStatus_lastPageHasFewerElements() {
        Pageable pageable = PageRequest.of(4, 3, Sort.by("createdAt").ascending());
        Page<Payment> lastPage = repo.findByStatus(PaymentStatus.PENDING, pageable);

        assertThat(lastPage.getContent()).hasSize(1); // 13 total, page 4 = element 13
        assertThat(lastPage.isLast()).isTrue();
    }

    @Test
    void findByMerchantId_sliceDoesNotCountTotal() {
        Pageable pageable = PageRequest.of(0, 4);
        Slice<Payment> slice = repo.findByMerchantId("merchant-A", pageable);

        assertThat(slice.getContent()).hasSize(4);
        assertThat(slice.hasNext()).isTrue();
        // Slice does NOT have getTotalElements() — that's the point
    }

    @Test
    void findHighValuePayments_returnsAboveThresholdSortedDesc() {
        // COMPLETED payments: 100, 200, 300, 400, 500
        // PENDING payments: 10..100 (some overlap at 100)
        // Threshold: 150 — should return 200, 300, 400, 500 (4 COMPLETED)
        Pageable pageable = PageRequest.of(0, 10);
        Page<Payment> page = repo.findHighValuePayments(new BigDecimal("150.00"), pageable);

        assertThat(page.getContent()).isNotEmpty();
        assertThat(page.getContent()).allMatch(
            p -> p.getAmount().getValue().compareTo(new BigDecimal("150.00")) > 0);
        // verify descending order
        BigDecimal prev = new BigDecimal("999999");
        for (Payment p : page.getContent()) {
            assertThat(p.getAmount().getValue()).isLessThanOrEqualTo(prev);
            prev = p.getAmount().getValue();
        }
    }

    @Test
    void pageRequest_withSort_ordersCorrectly() {
        // Sort all payments by amount descending, take top 3
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "amount.value"));
        Page<Payment> page = repo.findByStatus(PaymentStatus.COMPLETED, pageable);

        assertThat(page.getContent()).hasSize(3);
        // 500, 400, 300 — descending
        BigDecimal first = page.getContent().get(0).getAmount().getValue();
        BigDecimal second = page.getContent().get(1).getAmount().getValue();
        assertThat(first).isGreaterThanOrEqualTo(second);
    }
}
