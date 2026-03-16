package com.example.fintech.day3.inheritance;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CryptoAccount extends Account {

    private static final BigDecimal FEE_RATE = new BigDecimal("0.005");

    public CryptoAccount(String accountId, BigDecimal initialBalance) {
        super(accountId, initialBalance);
    }

    @Override
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Withdraw amount must be positive");
        BigDecimal networkFee = amount.multiply(FEE_RATE).setScale(8, RoundingMode.HALF_EVEN);
        BigDecimal total = amount.add(networkFee);
        if (total.compareTo(balance) > 0)
            throw new InsufficientFundsException(balance, total);
        balance = balance.subtract(total);
        recordTransaction("WITHDRAW %s (fee: %s) → balance %s".formatted(amount, networkFee, balance));
    }

    @Override
    public String getAccountType() { return "CRYPTO"; }
}
