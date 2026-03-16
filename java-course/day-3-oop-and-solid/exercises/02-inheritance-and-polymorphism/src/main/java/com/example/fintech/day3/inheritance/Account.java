package com.example.fintech.day3.inheritance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 02 — Inheritance & Polymorphism
 *
 * Abstract base class for all bank accounts.
 *
 * Concrete subclasses must implement:
 *   - withdraw(amount)     — each account type has different overdraft rules
 *   - getAccountType()     — e.g. "CHECKING", "SAVINGS", "CRYPTO"
 *
 * deposit() and getStatement() are shared and concrete (do NOT override).
 */
public abstract class Account {

    /** Thrown when a withdrawal would violate the account's balance rules. */
    public static class InsufficientFundsException extends RuntimeException {
        private final BigDecimal available;
        private final BigDecimal requested;

        public InsufficientFundsException(BigDecimal available, BigDecimal requested) {
            super("Insufficient funds: available=%s, requested=%s".formatted(available, requested));
            this.available = available;
            this.requested = requested;
        }

        public BigDecimal getAvailable() { return available; }
        public BigDecimal getRequested() { return requested; }
    }

    private final String accountId;
    protected BigDecimal balance;
    private final List<String> transactions = new ArrayList<>();

    protected Account(String accountId, BigDecimal initialBalance) {
        if (initialBalance.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Initial balance cannot be negative");
        this.accountId = accountId;
        this.balance = initialBalance;
    }

    // ─── Shared concrete methods ─────────────────────────────────────────────

    /**
     * Deposit amount into the account.
     * Validates amount > 0, updates balance, records transaction.
     * Do NOT override this.
     */
    public final void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Deposit amount must be positive");
        balance = balance.add(amount);
        transactions.add("DEPOSIT  %s → balance %s".formatted(amount, balance));
    }

    /** Returns an immutable snapshot of the transaction log. */
    public final List<String> getTransactionLog() {
        return List.copyOf(transactions);
    }

    /** Template method: builds a formatted account statement. */
    public final String getStatement() {
        return """
            === %s Account [%s] ===
            Balance: %s
            Transactions: %d
            """.formatted(getAccountType(), accountId, balance, transactions.size());
    }

    public BigDecimal getBalance() { return balance; }
    public String getAccountId()   { return accountId; }

    /** Records a transaction in the log — subclasses call this from withdraw(). */
    protected void recordTransaction(String entry) {
        transactions.add(entry);
    }

    // ─── Abstract methods — subclasses must implement ────────────────────────

    /**
     * TODO: Withdraw amount from the account.
     *
     * Rules vary by account type:
     *   CheckingAccount  — allows overdraft up to overdraftLimit
     *   SavingsAccount   — throws InsufficientFundsException if balance insufficient
     *   CryptoAccount    — no overdraft; deducts a 0.5% network fee on top of amount
     *
     * All implementations must:
     *   1. Validate amount > 0
     *   2. Apply their overdraft/fee logic
     *   3. Deduct from balance
     *   4. Call recordTransaction("WITHDRAW ...")
     */
    public abstract void withdraw(BigDecimal amount);

    /**
     * TODO: Return a short string identifying the account type.
     * Examples: "CHECKING", "SAVINGS", "CRYPTO"
     */
    public abstract String getAccountType();
}
