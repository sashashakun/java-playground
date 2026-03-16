package com.example.fintech.day6.fixtures;

import java.math.BigDecimal;

/**
 * Exercise 06 — Test Fixtures: TestDataBuilder
 *
 * The TestDataBuilder pattern provides a fluent API for building domain objects
 * in tests. It reduces noise and makes test intent clear.
 *
 * TypeScript analogy: a factory function with optional overrides:
 *   function makeInvoice(overrides = {}) { return { merchantId: 'default', ...overrides }; }
 *
 * This builder starts with sensible defaults so you only configure what matters
 * for each specific test.
 *
 * TODO 1: Implement all `with*` methods to return `this` (fluent builder pattern).
 *
 * TODO 2: Implement `build()` to create an Invoice and add all configured lineItems.
 *
 * TODO 3: Implement `asSent()` convenience method: calls build() then markSent().
 *
 * TODO 4: Implement `asPaid()` convenience method: calls build() then markSent() + markPaid().
 */
public class InvoiceTestDataBuilder {

    private String merchantId = "merchant-default";
    private String customerId = "customer-default";
    private String currency = "USD";
    private LineItem[] lineItems = {
        new LineItem("Default service", new BigDecimal("100.00"), 1)
    };

    public static InvoiceTestDataBuilder anInvoice() {
        return new InvoiceTestDataBuilder();
    }

    // TODO 1a: return this after setting merchantId
    public InvoiceTestDataBuilder withMerchantId(String merchantId) {
        throw new UnsupportedOperationException("TODO 1a");
    }

    // TODO 1b: return this after setting customerId
    public InvoiceTestDataBuilder withCustomerId(String customerId) {
        throw new UnsupportedOperationException("TODO 1b");
    }

    // TODO 1c: return this after setting currency
    public InvoiceTestDataBuilder withCurrency(String currency) {
        throw new UnsupportedOperationException("TODO 1c");
    }

    // TODO 1d: return this after setting lineItems (varargs)
    public InvoiceTestDataBuilder withLineItems(LineItem... items) {
        throw new UnsupportedOperationException("TODO 1d");
    }

    // TODO 2: create Invoice, add all lineItems, return invoice
    public Invoice build() {
        throw new UnsupportedOperationException("TODO 2");
    }

    // TODO 3: build() then markSent()
    public Invoice asSent() {
        throw new UnsupportedOperationException("TODO 3");
    }

    // TODO 4: build() then markSent() + markPaid()
    public Invoice asPaid() {
        throw new UnsupportedOperationException("TODO 4");
    }
}
