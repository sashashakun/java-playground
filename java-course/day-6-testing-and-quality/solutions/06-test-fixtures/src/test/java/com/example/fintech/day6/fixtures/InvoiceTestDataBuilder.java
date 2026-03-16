package com.example.fintech.day6.fixtures;

import java.math.BigDecimal;

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

    public InvoiceTestDataBuilder withMerchantId(String merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    public InvoiceTestDataBuilder withCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public InvoiceTestDataBuilder withCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public InvoiceTestDataBuilder withLineItems(LineItem... items) {
        this.lineItems = items;
        return this;
    }

    public Invoice build() {
        Invoice invoice = new Invoice(merchantId, customerId, currency);
        for (LineItem item : lineItems) {
            invoice.addLineItem(item);
        }
        return invoice;
    }

    public Invoice asSent() {
        Invoice invoice = build();
        invoice.markSent();
        return invoice;
    }

    public Invoice asPaid() {
        Invoice invoice = build();
        invoice.markSent();
        invoice.markPaid();
        return invoice;
    }
}
