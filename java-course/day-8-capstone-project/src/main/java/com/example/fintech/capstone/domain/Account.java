package com.example.fintech.capstone.domain;

import com.example.fintech.capstone.exception.InsufficientFundsException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Capstone Exercise A — Domain: Account entity
 *
 * An Account holds funds in a single currency for one customer.
 * It enforces business invariants: no negative balance, no debit on suspended/closed accounts.
 *
 * TODO A1: Add @Entity and @Table(name = "accounts") on this class.
 *
 * TODO A2: Add @Id and @GeneratedValue(strategy = GenerationType.UUID) on the `id` field.
 *
 * TODO A3: Add @Column(nullable = false) on: ownerId, currency, balance, status, createdAt.
 *          Add @Enumerated(EnumType.STRING) on the `status` field.
 *
 * TODO A4: Implement debit(BigDecimal amount):
 *          - throw InsufficientFundsException if balance < amount
 *          - subtract amount from balance
 *         Implement credit(BigDecimal amount):
 *          - add amount to balance
 */
// TODO A1: @Entity @Table(name = "accounts")
public class Account {

    // TODO A2: @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // TODO A3: @Column(nullable = false)
    private String ownerId;

    // TODO A3: @Column(nullable = false, length = 3)
    private String currency;

    // TODO A3: @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    // TODO A3: @Enumerated(EnumType.STRING) @Column(nullable = false)
    private AccountStatus status;

    // TODO A3: @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected Account() {}

    public Account(String ownerId, String currency, BigDecimal initialBalance) {
        this.ownerId = ownerId;
        this.currency = currency;
        this.balance = initialBalance;
        this.status = AccountStatus.ACTIVE;
        this.createdAt = Instant.now();
    }

    // TODO A4: debit — throw InsufficientFundsException if balance < amount, else balance -= amount
    public void debit(BigDecimal amount) {
        throw new UnsupportedOperationException("TODO A4: implement debit");
    }

    // TODO A4: credit — balance += amount
    public void credit(BigDecimal amount) {
        throw new UnsupportedOperationException("TODO A4: implement credit");
    }

    public void suspend() { this.status = AccountStatus.SUSPENDED; }
    public void close() { this.status = AccountStatus.CLOSED; }

    public String getId() { return id; }
    public String getOwnerId() { return ownerId; }
    public String getCurrency() { return currency; }
    public BigDecimal getBalance() { return balance; }
    public AccountStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
