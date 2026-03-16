package com.example.fintech.day7.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SpringEventsTest {

    @Autowired PaymentService paymentService;
    @Autowired AuditListener auditListener;
    @Autowired NotificationListener notificationListener;

    @BeforeEach
    void reset() {
        auditListener.clear();
        notificationListener.clear();
    }

    @Test
    void auditListener_receivesCreatedEvent() {
        paymentService.createPayment("user-1", new BigDecimal("100.00"), "USD");

        assertThat(auditListener.getAuditLog())
            .hasSize(1)
            .allMatch(entry -> entry.startsWith("CREATED:"));
    }

    @Test
    void auditListener_receivesAllThreeEventTypes() {
        String id = paymentService.createPayment("user-2", new BigDecimal("200.00"), "EUR");
        paymentService.completePayment(id, "user-2", new BigDecimal("200.00"), "EUR");
        paymentService.failPayment("pay-fail", "user-3", "insufficient funds");

        assertThat(auditListener.getAuditLog()).hasSize(3);
        assertThat(auditListener.getAuditLog().get(0)).startsWith("CREATED:");
        assertThat(auditListener.getAuditLog().get(1)).startsWith("COMPLETED:");
        assertThat(auditListener.getAuditLog().get(2)).startsWith("FAILED:");
    }

    @Test
    void notificationListener_firesAfterCommit() {
        paymentService.createPayment("user-4", new BigDecimal("50.00"), "GBP");

        // @TransactionalEventListener(AFTER_COMMIT) fires after the transaction wrapping
        // createPayment() commits — in a @SpringBootTest the transaction commits normally.
        assertThat(notificationListener.getNotifications())
            .hasSize(1)
            .allMatch(n -> n.startsWith("NOTIFY_CREATED:"));
    }

    @Test
    void notificationListener_doesNotFireForNonCreatedEvents() {
        String id = paymentService.createPayment("user-5", new BigDecimal("75.00"), "USD");
        paymentService.completePayment(id, "user-5", new BigDecimal("75.00"), "USD");

        // Only onCreated has @TransactionalEventListener for notifications — 1 notification total
        assertThat(notificationListener.getNotifications()).hasSize(1);
    }
}
