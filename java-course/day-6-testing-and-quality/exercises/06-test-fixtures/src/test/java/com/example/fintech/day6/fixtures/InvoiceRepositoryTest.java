package com.example.fintech.day6.fixtures;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static com.example.fintech.day6.fixtures.InvoiceTestDataBuilder.anInvoice;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Exercise 06 — Test Fixtures
 *
 * Demonstrates:
 *   1. Using the TestDataBuilder to create test data concisely
 *   2. @TestInstance(PER_CLASS) + @BeforeAll to share setup across tests
 *      without static methods
 *   3. How the builder pattern makes test intent obvious at a glance
 *
 * Once you implement InvoiceTestDataBuilder, all tests in this class should pass.
 */
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)   // allows @BeforeAll on non-static method
class InvoiceRepositoryTest {

    @Autowired
    private InvoiceRepository repo;

    /**
     * @BeforeAll runs once before all tests in this class.
     * With @TestInstance(PER_CLASS) it doesn't need to be static — `this` is available.
     *
     * TypeScript analogy: beforeAll(() => { ... }) in Jest.
     */
    @BeforeAll
    void seedDatabase() {
        // Merchant A: 2 DRAFT, 1 SENT, 1 PAID
        repo.save(anInvoice().withMerchantId("merchant-A").withCustomerId("cust-1").build());
        repo.save(anInvoice().withMerchantId("merchant-A").withCustomerId("cust-2").build());
        repo.save(anInvoice().withMerchantId("merchant-A").withCustomerId("cust-3").asSent());
        repo.save(anInvoice().withMerchantId("merchant-A").withCustomerId("cust-4").asPaid());

        // Merchant B: 1 PAID invoice with 3 line items
        repo.save(anInvoice()
            .withMerchantId("merchant-B")
            .withCustomerId("cust-5")
            .withLineItems(
                new LineItem("Widget A", new BigDecimal("25.00"), 4),
                new LineItem("Widget B", new BigDecimal("15.00"), 2),
                new LineItem("Service fee", new BigDecimal("10.00"), 1)
            )
            .asPaid());
    }

    @Test
    void findByMerchantId_returnsAllMerchantAInvoices() {
        List<Invoice> result = repo.findByMerchantId("merchant-A");
        assertThat(result).hasSize(4);
    }

    @Test
    void findByStatus_returnsPaidInvoices() {
        List<Invoice> paid = repo.findByStatus(Invoice.InvoiceStatus.PAID);
        assertThat(paid).hasSize(2);
    }

    @Test
    void asPaid_createsInvoiceWithPaidStatus() {
        Invoice paidInvoice = repo.findByMerchantId("merchant-A").stream()
            .filter(i -> i.getStatus() == Invoice.InvoiceStatus.PAID)
            .findFirst().orElseThrow();

        assertThat(paidInvoice.getPaidAt()).isNotNull();
        assertThat(paidInvoice.getStatus()).isEqualTo(Invoice.InvoiceStatus.PAID);
    }

    @Test
    void withLineItems_multipleItemsArePersistedCorrectly() {
        Invoice merchantB = repo.findByMerchantId("merchant-B").get(0);
        assertThat(merchantB.getLineItems()).hasSize(3);

        // Total: (25*4) + (15*2) + (10*1) = 100 + 30 + 10 = 140
        assertThat(merchantB.totalAmount()).isEqualByComparingTo("140.00");
    }

    @Test
    void defaultBuilder_createsValidDraftInvoice() {
        Invoice invoice = anInvoice().build();

        assertThat(invoice.getStatus()).isEqualTo(Invoice.InvoiceStatus.DRAFT);
        assertThat(invoice.getMerchantId()).isEqualTo("merchant-default");
        assertThat(invoice.getLineItems()).hasSize(1);
    }
}
