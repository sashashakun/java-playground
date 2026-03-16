package com.example.fintech.capstone.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

// SOLUTION B — Domain: Payment entity

@Entity                                                 // TODO B1 ✓
@Table(name = "payments")                              // TODO B1 ✓
public class Payment {

    @Id                                                 // TODO B1 ✓
    @GeneratedValue(strategy = GenerationType.UUID)     // TODO B1 ✓
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)                  // TODO B2 ✓
    @JoinColumn(name = "account_id", nullable = false)  // TODO B2 ✓
    private Account account;

    @Column(nullable = false, precision = 19, scale = 4) // TODO B3 ✓
    private BigDecimal amount;

    @Column(nullable = false, length = 3)              // TODO B3 ✓
    private String currency;

    @Column(nullable = false)                           // TODO B3 ✓
    private String description;

    @Enumerated(EnumType.STRING)                        // TODO B3 ✓
    @Column(nullable = false)                           // TODO B3 ✓
    private PaymentStatus status;

    @Column(nullable = false, updatable = false)        // TODO B3 ✓
    private Instant createdAt;

    private Instant completedAt;

    protected Payment() {}

    public Payment(Account account, BigDecimal amount, String currency, String description) {
        this.account = account;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.status = PaymentStatus.PENDING;
        this.createdAt = Instant.now();
    }

    public void complete() {
        this.status = PaymentStatus.COMPLETED;
        this.completedAt = Instant.now();
    }

    public void fail() {
        this.status = PaymentStatus.FAILED;
        this.completedAt = Instant.now();
    }

    public String getId() { return id; }
    public Account getAccount() { return account; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getDescription() { return description; }
    public PaymentStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getCompletedAt() { return completedAt; }
}
