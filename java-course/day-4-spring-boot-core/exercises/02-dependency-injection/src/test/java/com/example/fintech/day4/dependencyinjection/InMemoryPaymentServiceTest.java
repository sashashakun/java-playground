package com.example.fintech.day4.dependencyinjection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

/**
 * Plain JUnit test for the service — no Spring context needed.
 */
class InMemoryPaymentServiceTest {

    private InMemoryPaymentService service;

    @BeforeEach
    void setUp() { service = new InMemoryPaymentService(); }

    @Test
    void createReturnsPaymentWithPendingStatus() {
        var req = new CreatePaymentRequest(new BigDecimal("100"), "USD", "Test");
        var payment = service.create(req);
        assertThat(payment.id()).isNotBlank();
        assertThat(payment.amount()).isEqualByComparingTo("100");
        assertThat(payment.currency()).isEqualTo("USD");
        assertThat(payment.status()).isEqualTo("PENDING");
    }

    @Test
    void findByIdReturnsPresentAfterCreate() {
        var req = new CreatePaymentRequest(new BigDecimal("200"), "EUR", "Invoice");
        var created = service.create(req);
        assertThat(service.findById(created.id())).isPresent().contains(created);
    }

    @Test
    void findByIdReturnsEmptyForUnknown() {
        assertThat(service.findById("ghost")).isEmpty();
    }

    @Test
    void findAllReturnsAllCreated() {
        service.create(new CreatePaymentRequest(new BigDecimal("100"), "USD", "a"));
        service.create(new CreatePaymentRequest(new BigDecimal("200"), "EUR", "b"));
        assertThat(service.findAll()).hasSize(2);
    }

    @Test
    void deleteTrueForExisting() {
        var created = service.create(new CreatePaymentRequest(new BigDecimal("100"), "USD", "x"));
        assertThat(service.delete(created.id())).isTrue();
        assertThat(service.findById(created.id())).isEmpty();
    }

    @Test
    void deleteFalseForMissing() {
        assertThat(service.delete("ghost")).isFalse();
    }
}
