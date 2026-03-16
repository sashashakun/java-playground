package com.example.fintech.day3.inheritance;

import java.math.BigDecimal;

/**
 * Exercise 02 — Savings Account
 *
 * NO overdraft. Throws InsufficientFundsException if amount > balance.
 *
 * withdraw(amount) rules:
 *   - amount must be > 0 (throw IllegalArgumentException otherwise)
 *   - if amount > balance → throw InsufficientFundsException(balance, amount)
 *   - otherwise: balance -= amount
 *   - call recordTransaction("WITHDRAW %s → balance %s".formatted(amount, balance))
 *
 * getAccountType() returns "SAVINGS"
 */
public class SavingsAccount extends Account {

    public SavingsAccount(String accountId, BigDecimal initialBalance) {
        super(accountId, initialBalance);
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
