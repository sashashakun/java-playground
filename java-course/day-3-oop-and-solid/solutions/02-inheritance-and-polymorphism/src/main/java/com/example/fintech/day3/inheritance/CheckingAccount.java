package com.example.fintech.day3.inheritance;

import java.math.BigDecimal;

public class CheckingAccount extends Account {

    private final BigDecimal overdraftLimit;

    public CheckingAccount(String accountId, BigDecimal initialBalance, BigDecimal overdraftLimit) {
        super(accountId, initialBalance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Withdraw amount must be positive");
        BigDecimal newBalance = balance.subtract(amount);
        if (newBalance.compareTo(overdraftLimit.negate()) < 0)
            throw new InsufficientFundsException(balance, amount);
        balance = newBalance;
        recordTransaction("WITHDRAW %s → balance %s".formatted(amount, balance));
    }

    @Override
    public String getAccountType() { return "CHECKING"; }
}
