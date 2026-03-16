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

/**
 * Capstone Exercise B — Domain: Payment entity
 *
 * A Payment represents a debit from one Account (or null for top-ups/credits).
 * Multiple payments can reference the same account — hence @ManyToOne.
 *
 * TODO B1: Add @Entity and @Table(name = "payments") on this class.
 *          Add @Id and @GeneratedValue(strategy = GenerationType.UUID) on `id`.
 *
 * TODO B2: Add JPA relationship to `account`:
 *          @ManyToOne(fetch = FetchType.LAZY)
 *          @JoinColumn(name = "account_id", nullable = false)
 *
 * TODO B3: Add @Enumerated(EnumType.STRING) + @Column(nullable = false) on `status`.
 *          Add @Column(nullable = false) on `amount`, `currency`, `description`, `createdAt`.
 */
// TODO B1: @Entity @Table(name = "payments")
public class Payment {

    // TODO B1: @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // TODO B2: relationship annotations
    private Account account;

    // TODO B3: @Column
    private BigDecimal amount;

    // TODO B3: @Column(length = 3)
    private String currency;

    // TODO B3: @Column
    private String description;

    // TODO B3: @Enumerated @Column
    private PaymentStatus status;

    // TODO B3: @Column(updatable = false)
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
