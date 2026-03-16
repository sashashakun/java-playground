package com.example.fintech.day7.specifications;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SpecificationsTest {

    @Autowired
    private TransactionRepository repo;

    private Instant now;

    @BeforeEach
    void seed() {
        now = Instant.now();

        repo.save(new Transaction("merchant-A", "cust-1", new BigDecimal("100.00"),
            "USD", TransactionStatus.COMPLETED, now.minus(5, ChronoUnit.DAYS), "US"));
        repo.save(new Transaction("merchant-A", "cust-2", new BigDecimal("250.00"),
            "USD", TransactionStatus.FAILED, now.minus(3, ChronoUnit.DAYS), "US"));
        repo.save(new Transaction("merchant-B", "cust-3", new BigDecimal("500.00"),
            "EUR", TransactionStatus.COMPLETED, now.minus(1, ChronoUnit.DAYS), "DE"));
        repo.save(new Transaction("merchant-B", "cust-4", new BigDecimal("1500.00"),
            "EUR", TransactionStatus.PENDING, now, "FR"));
        repo.save(new Transaction("merchant-C", "cust-5", new BigDecimal("75.00"),
            "GBP", TransactionStatus.COMPLETED, now.minus(10, ChronoUnit.DAYS), "GB"));
    }

    @Test
    void forMerchant_filtersCorrectly() {
        List<Transaction> results = repo.findAll(
            Specification.where(TransactionSpecs.forMerchant("merchant-A")));

        assertThat(results).hasSize(2)
            .allMatch(t -> t.getMerchantId().equals("merchant-A"));
    }

    @Test
    void withStatus_filtersCorrectly() {
        List<Transaction> results = repo.findAll(
            Specification.where(TransactionSpecs.withStatus(TransactionStatus.COMPLETED)));

        assertThat(results).hasSize(3)
            .allMatch(t -> t.getStatus() == TransactionStatus.COMPLETED);
    }

    @Test
    void amountBetween_filtersCorrectly() {
        List<Transaction> results = repo.findAll(
            Specification.where(TransactionSpecs.amountBetween(
                new BigDecimal("200"), new BigDecimal("600"))));

        assertThat(results).hasSize(2)
            .allMatch(t -> t.getAmount().compareTo(new BigDecimal("200")) >= 0
                && t.getAmount().compareTo(new BigDecimal("600")) <= 0);
    }

    @Test
    void createdAfter_filtersCorrectly() {
        List<Transaction> results = repo.findAll(
            Specification.where(TransactionSpecs.createdAfter(now.minus(2, ChronoUnit.DAYS))));

        assertThat(results).hasSize(2); // -1 day and now
    }

    @Test
    void forCountry_filtersCorrectly() {
        List<Transaction> results = repo.findAll(
            Specification.where(TransactionSpecs.forCountry("US")));

        assertThat(results).hasSize(2)
            .allMatch(t -> t.getCountry().equals("US"));
    }

    @Test
    void combinedSpecifications_merchantAndStatus() {
        List<Transaction> results = repo.findAll(
            Specification.where(TransactionSpecs.forMerchant("merchant-A"))
                .and(TransactionSpecs.withStatus(TransactionStatus.COMPLETED)));

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getMerchantId()).isEqualTo("merchant-A");
        assertThat(results.getFirst().getStatus()).isEqualTo(TransactionStatus.COMPLETED);
    }

    @Test
    void combinedSpecifications_amountAndCountry() {
        List<Transaction> results = repo.findAll(
            Specification.where(TransactionSpecs.amountBetween(
                    new BigDecimal("400"), new BigDecimal("2000")))
                .and(TransactionSpecs.forCountry("FR")));

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getCustomerId()).isEqualTo("cust-4");
    }

    @Test
    void nullSpec_returnsAll() {
        // Null specification = no filter = all records
        List<Transaction> results = repo.findAll(
            Specification.where(TransactionSpecs.forMerchant(null))
                .and(TransactionSpecs.withStatus(null)));

        assertThat(results).hasSize(5);
    }
}
