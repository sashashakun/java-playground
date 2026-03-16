package com.example.fintech.day5.transactions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @Import(PaymentService.class) loads the service bean alongside the JPA slice.
 * @DataJpaTest does not load @Service beans automatically.
 */
@DataJpaTest
@Import(PaymentService.class)
class PaymentServiceTransactionTest {

    @Autowired
    private PaymentService service;

    @Autowired
    private PaymentRepository repo;

    @Test
    void createPayment_persistsToDatabase() {
        Payment p = service.createPayment(
            new BigDecimal("100.00"), "USD", "m1", "desc", "key-001");

        assertThat(p.getId()).isNotNull();
        assertThat(repo.findById(p.getId())).isPresent();
    }

    @Test
    void createPayment_rollsBackOnDuplicateKey() {
        service.createPayment(new BigDecimal("50.00"), "USD", "m1", "first", "dup-key");

        assertThatThrownBy(() ->
            service.createPayment(new BigDecimal("75.00"), "EUR", "m2", "second", "dup-key"))
            .isInstanceOf(PaymentService.DuplicateKeyException.class);

        // Only the first payment should exist
        assertThat(repo.count()).isEqualTo(1);
    }

    @Test
    void completePayment_updatesStatusToCompleted() {
        Payment created = service.createPayment(
            new BigDecimal("200.00"), "USD", "m1", "desc", "key-002");

        Payment completed = service.completePayment(created.getId());

        assertThat(completed.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
    }

    @Test
    void completePayment_throwsIfNotFound() {
        assertThatThrownBy(() -> service.completePayment("ghost-id"))
            .isInstanceOf(PaymentService.PaymentNotFoundException.class);
    }

    @Test
    void batchApprove_rollsBackAllIfOneNotFound() {
        Payment p1 = service.createPayment(
            new BigDecimal("10.00"), "USD", "m1", "d1", "key-003");
        Payment p2 = service.createPayment(
            new BigDecimal("20.00"), "USD", "m1", "d2", "key-004");

        assertThatThrownBy(() ->
            service.batchApprove(List.of(p1.getId(), p2.getId(), "does-not-exist")))
            .isInstanceOf(PaymentService.PaymentNotFoundException.class);

        // All payments should still be PENDING — batch rolled back
        assertThat(repo.findById(p1.getId()).get().getStatus()).isEqualTo(PaymentStatus.PENDING);
        assertThat(repo.findById(p2.getId()).get().getStatus()).isEqualTo(PaymentStatus.PENDING);
    }

    @Test
    void batchApprove_updatesAllWhenAllExist() {
        Payment p1 = service.createPayment(
            new BigDecimal("10.00"), "USD", "m1", "d1", "key-005");
        Payment p2 = service.createPayment(
            new BigDecimal("20.00"), "USD", "m1", "d2", "key-006");

        List<Payment> result = service.batchApprove(List.of(p1.getId(), p2.getId()));

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(p -> p.getStatus() == PaymentStatus.PROCESSING);
    }
}
