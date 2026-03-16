package com.example.fintech.day3.inheritance;

import java.math.BigDecimal;

public class SavingsAccount extends Account {

    public SavingsAccount(String accountId, BigDecimal initialBalance) {
        super(accountId, initialBalance);
    }

    @Override
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Withdraw amount must be positive");
        if (amount.compareTo(balance) > 0)
            throw new InsufficientFundsException(balance, amount);
        balance = balance.subtract(amount);
        recordTransaction("WITHDRAW %s → balance %s".formatted(amount, balance));
    }

    @Override
    public String getAccountType() { return "SAVINGS"; }
}
