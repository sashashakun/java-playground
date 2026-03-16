package com.example.fintech.day6.fixtures;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "line_items")
public class LineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @Column(nullable = false)
    private String description;

    @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private int quantity;

    protected LineItem() {}

    public LineItem(String description, BigDecimal unitPrice, int quantity) {
        this.description = description;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getId()            { return id; }
    public Invoice getInvoice()      { return invoice; }
    public String getDescription()   { return description; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public int getQuantity()         { return quantity; }

    public void setInvoice(Invoice invoice) { this.invoice = invoice; }
}
