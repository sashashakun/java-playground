package com.example.fintech.day3.inheritance;

import java.math.BigDecimal;

/**
 * Exercise 02 — Checking Account
 *
 * Allows overdraft up to overdraftLimit.
 *
 * withdraw(amount) rules:
 *   - amount must be > 0
 *   - balance can go negative, but not below -overdraftLimit
 *   - if (balance - amount) < -overdraftLimit → throw InsufficientFundsException(balance, amount)
 *   - otherwise: balance -= amount
 *   - call recordTransaction("WITHDRAW %s → balance %s".formatted(amount, balance))
 *
 * getAccountType() returns "CHECKING"
 */
public class CheckingAccount extends Account {

    private final BigDecimal overdraftLimit;

    public CheckingAccount(String accountId, BigDecimal initialBalance, BigDecimal overdraftLimit) {
        super(accountId, initialBalance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        // TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getAccountType() {
        // TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
