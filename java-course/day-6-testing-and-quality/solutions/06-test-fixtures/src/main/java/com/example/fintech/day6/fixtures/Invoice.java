package com.example.fintech.day6.fixtures;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(nullable = false)
    private String merchantId;

    @Column(nullable = false)
    private String customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus status;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, updatable = false)
    private Instant issuedAt;

    private Instant paidAt;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineItem> lineItems = new ArrayList<>();

    protected Invoice() {}

    public Invoice(String merchantId, String customerId, String currency) {
        this.merchantId = merchantId;
        this.customerId = customerId;
        this.currency = currency;
        this.status = InvoiceStatus.DRAFT;
        this.issuedAt = Instant.now();
    }

    public void addLineItem(LineItem item) {
        lineItems.add(item);
        item.setInvoice(this);
    }

    public void markSent()  { this.status = InvoiceStatus.SENT; }
    public void markPaid()  { this.status = InvoiceStatus.PAID; this.paidAt = Instant.now(); }
    public void markVoid()  { this.status = InvoiceStatus.VOID; }

    public BigDecimal totalAmount() {
        return lineItems.stream()
            .map(li -> li.getUnitPrice().multiply(new BigDecimal(li.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getId()            { return id; }
    public String getMerchantId()    { return merchantId; }
    public String getCustomerId()    { return customerId; }
    public InvoiceStatus getStatus() { return status; }
    public String getCurrency()      { return currency; }
    public Instant getIssuedAt()     { return issuedAt; }
    public Instant getPaidAt()       { return paidAt; }
    public List<LineItem> getLineItems() { return lineItems; }

    public enum InvoiceStatus { DRAFT, SENT, PAID, VOID }
}
