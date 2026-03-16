package com.example.fintech.day5.datajpatest;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Exercise 07 — DataJpaTest
 *
 * The implementation is provided. Your task is to write tests in WalletRepositoryTest.java.
 *
 * A Wallet represents a user's currency balance (like a Coinbase wallet).
 */
@Entity
@Table(name = "wallets",
    indexes = @Index(name = "idx_wallet_owner", columnList = "owner_id"))
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(length = 3, nullable = false)
    private String currency;

    @Column(precision = 19, scale = 8, nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    protected Wallet() {}

    public Wallet(String ownerId, String currency, BigDecimal initialBalance) {
        this.ownerId = ownerId;
        this.currency = currency;
        this.balance = initialBalance;
        this.status = WalletStatus.ACTIVE;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void credit(BigDecimal amount) {
        if (status != WalletStatus.ACTIVE) {
            throw new IllegalStateException("Cannot credit a " + status + " wallet");
        }
        this.balance = this.balance.add(amount);
        this.updatedAt = Instant.now();
    }

    public void debit(BigDecimal amount) {
        if (status != WalletStatus.ACTIVE) {
            throw new IllegalStateException("Cannot debit a " + status + " wallet");
        }
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException(balance, amount);
        }
        this.balance = this.balance.subtract(amount);
        this.updatedAt = Instant.now();
    }

    public void freeze() { this.status = WalletStatus.FROZEN; }
    public void close()  { this.status = WalletStatus.CLOSED; }

    public String getId()           { return id; }
    public String getOwnerId()      { return ownerId; }
    public String getCurrency()     { return currency; }
    public BigDecimal getBalance()  { return balance; }
    public WalletStatus getStatus() { return status; }
    public Instant getCreatedAt()   { return createdAt; }
    public Instant getUpdatedAt()   { return updatedAt; }

    public static class InsufficientFundsException extends RuntimeException {
        public InsufficientFundsException(BigDecimal balance, BigDecimal requested) {
            super("Insufficient funds: balance=" + balance + ", requested=" + requested);
        }
    }
}
