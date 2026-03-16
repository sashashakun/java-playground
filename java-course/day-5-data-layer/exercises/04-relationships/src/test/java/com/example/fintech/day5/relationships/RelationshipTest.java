package com.example.fintech.day5.relationships;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RelationshipTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private MerchantRepository merchantRepo;

    @Autowired
    private PaymentRepository paymentRepo;

    @Test
    void cascadeAll_savingMerchantSavesPayments() {
        Merchant merchant = new Merchant("Acme Corp", "US");
        Payment p1 = new Payment(new Money(new BigDecimal("100.00"), "USD"), "desc1", "k1");
        Payment p2 = new Payment(new Money(new BigDecimal("200.00"), "EUR"), "desc2", "k2");

        merchant.addPayment(p1);
        merchant.addPayment(p2);

        merchantRepo.save(merchant); // should cascade-save payments too
        em.flush();
        em.clear();

        Merchant found = em.find(Merchant.class, merchant.getId());
        assertThat(found).isNotNull();
        // Load payments explicitly (LAZY)
        assertThat(found.getPayments()).hasSize(2);
    }

    @Test
    void manyToOne_paymentHasMerchantReference() {
        Merchant merchant = new Merchant("Beta Inc", "GB");
        Payment p = new Payment(new Money(new BigDecimal("50.00"), "GBP"), "fx trade", "k3");
        merchant.addPayment(p);
        merchantRepo.save(merchant);
        em.flush();
        em.clear();

        Payment found = paymentRepo.findById(p.getId()).orElseThrow();
        // Load the merchant (LAZY proxy will initialise)
        assertThat(found.getMerchant()).isNotNull();
        assertThat(found.getMerchant().getName()).isEqualTo("Beta Inc");
    }

    @Test
    void orphanRemoval_removingFromCollectionDeletesPayment() {
        Merchant merchant = new Merchant("Gamma Ltd", "DE");
        Payment p = new Payment(new Money(new BigDecimal("300.00"), "EUR"), "transfer", "k4");
        merchant.addPayment(p);
        merchantRepo.save(merchant);
        em.flush();
        em.clear();

        Merchant managed = em.find(Merchant.class, merchant.getId());
        Payment toRemove = managed.getPayments().get(0);
        managed.removePayment(toRemove); // orphanRemoval triggers delete
        em.flush();

        assertThat(paymentRepo.findById(toRemove.getId())).isEmpty();
    }

    @Test
    void findByMerchant_returnsOnlyThatMerchantsPayments() {
        Merchant m1 = new Merchant("M1", "US");
        Merchant m2 = new Merchant("M2", "FR");
        m1.addPayment(new Payment(new Money(new BigDecimal("10.00"), "USD"), "p1", "k5"));
        m1.addPayment(new Payment(new Money(new BigDecimal("20.00"), "USD"), "p2", "k6"));
        m2.addPayment(new Payment(new Money(new BigDecimal("30.00"), "EUR"), "p3", "k7"));
        merchantRepo.saveAll(List.of(m1, m2));
        em.flush();

        List<Payment> m1Payments = paymentRepo.findByMerchant(m1);
        assertThat(m1Payments).hasSize(2);
    }
}
