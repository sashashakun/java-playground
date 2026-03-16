package com.example.fintech.day3.inheritance;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Exercise 02 — Crypto Account
 *
 * No overdraft. On each withdrawal, a 0.5% network fee is charged ON TOP.
 *
 * withdraw(amount) rules:
 *   - amount must be > 0
 *   - networkFee = amount * 0.005, scaled to 8 decimal places (HALF_EVEN)
 *   - totalDeducted = amount + networkFee
 *   - if totalDeducted > balance → throw InsufficientFundsException(balance, totalDeducted)
 *   - otherwise: balance -= totalDeducted
 *   - call recordTransaction(
 *       "WITHDRAW %s (fee: %s) → balance %s".formatted(amount, networkFee, balance))
 *
 * getAccountType() returns "CRYPTO"
 */
public class CryptoAccount extends Account {

    private static final BigDecimal FEE_RATE = new BigDecimal("0.005");

    public CryptoAccount(String accountId, BigDecimal initialBalance) {
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
