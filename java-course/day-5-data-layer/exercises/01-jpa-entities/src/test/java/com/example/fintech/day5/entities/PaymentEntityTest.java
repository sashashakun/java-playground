package com.example.fintech.day5.entities;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @DataJpaTest is a test slice that:
 *   - Spins up an in-memory H2 database
 *   - Configures Hibernate / JPA
 *   - Does NOT load web layer beans (@RestController, etc.)
 *   - Rolls back each test automatically (no dirty state between tests)
 *
 * TypeScript analogy: vitest with a per-test transaction rollback.
 */
@DataJpaTest
class PaymentEntityTest {

    @Autowired
    private EntityManager em;

    @Test
    void paymentIsPersisted() {
        Money money = new Money(new BigDecimal("250.00"), "USD");
        Payment payment = new Payment(money, "merchant-001", "Crypto purchase", "key-abc-001");

        em.persist(payment);
        em.flush();
        em.clear(); // detach so next find hits the database

        Payment found = em.find(Payment.class, payment.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isNotNull();
        assertThat(found.getStatus()).isEqualTo(PaymentStatus.PENDING);
        assertThat(found.getMerchantId()).isEqualTo("merchant-001");
    }

    @Test
    void moneyColumnsAreEmbeddedOnPaymentsTable() {
        Money money = new Money(new BigDecimal("100.50"), "EUR");
        Payment payment = new Payment(money, "merchant-002", "Wire transfer", "key-abc-002");

        em.persist(payment);
        em.flush();
        em.clear();

        Payment found = em.find(Payment.class, payment.getId());

        assertThat(found.getAmount().getValue()).isEqualByComparingTo("100.50");
        assertThat(found.getAmount().getCurrency()).isEqualTo("EUR");
    }

    @Test
    void statusIsStoredAsString() {
        Money money = new Money(new BigDecimal("500.00"), "GBP");
        Payment payment = new Payment(money, "merchant-003", "FX trade", "key-abc-003");

        em.persist(payment);
        em.flush();

        // Check raw column value via native query
        Object statusValue = em.createNativeQuery(
                "SELECT status FROM payments WHERE id = :id")
            .setParameter("id", payment.getId())
            .getSingleResult();

        assertThat(statusValue.toString()).isEqualTo("PENDING");
    }

    @Test
    void idempotencyKeyIsUnique() {
        Money money = new Money(new BigDecimal("10.00"), "USD");
        Payment p1 = new Payment(money, "m1", "desc", "UNIQUE-KEY");
        Payment p2 = new Payment(money, "m2", "desc", "UNIQUE-KEY"); // same key!

        em.persist(p1);
        em.flush();

        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            em.persist(p2);
            em.flush();
        });
    }
}
