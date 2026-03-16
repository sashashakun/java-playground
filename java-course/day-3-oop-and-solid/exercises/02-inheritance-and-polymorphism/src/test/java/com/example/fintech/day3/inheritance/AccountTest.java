package com.example.fintech.day3.inheritance;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class AccountTest {

    // ─── CheckingAccount ──────────────────────────────────────────────────────

    @Test
    void checkingAccountTypeIsChecking() {
        var acc = new CheckingAccount("c1", new BigDecimal("500"), new BigDecimal("200"));
        assertThat(acc.getAccountType()).isEqualTo("CHECKING");
    }

    @Test
    void checkingDepositIncreasesBalance() {
        var acc = new CheckingAccount("c1", new BigDecimal("500"), new BigDecimal("200"));
        acc.deposit(new BigDecimal("100"));
        assertThat(acc.getBalance()).isEqualByComparingTo("600");
    }

    @Test
    void checkingWithdrawDecreasesBalance() {
        var acc = new CheckingAccount("c1", new BigDecimal("500"), new BigDecimal("200"));
        acc.withdraw(new BigDecimal("300"));
        assertThat(acc.getBalance()).isEqualByComparingTo("200");
    }

    @Test
    void checkingAllowsOverdraftWithinLimit() {
        var acc = new CheckingAccount("c1", new BigDecimal("100"), new BigDecimal("200"));
        acc.withdraw(new BigDecimal("250")); // 100 - 250 = -150, limit is -200 → OK
        assertThat(acc.getBalance()).isEqualByComparingTo("-150");
    }

    @Test
    void checkingThrowsWhenOverdraftExceedsLimit() {
        var acc = new CheckingAccount("c1", new BigDecimal("100"), new BigDecimal("200"));
        assertThatThrownBy(() -> acc.withdraw(new BigDecimal("400")))
            .isInstanceOf(Account.InsufficientFundsException.class);
    }

    // ─── SavingsAccount ───────────────────────────────────────────────────────

    @Test
    void savingsAccountTypeIsSavings() {
        var acc = new SavingsAccount("s1", new BigDecimal("1000"));
        assertThat(acc.getAccountType()).isEqualTo("SAVINGS");
    }

    @Test
    void savingsWithdrawSucceedsWhenBalanceSufficient() {
        var acc = new SavingsAccount("s1", new BigDecimal("1000"));
        acc.withdraw(new BigDecimal("400"));
        assertThat(acc.getBalance()).isEqualByComparingTo("600");
    }

    @Test
    void savingsThrowsOnOverdraft() {
        var acc = new SavingsAccount("s1", new BigDecimal("100"));
        assertThatThrownBy(() -> acc.withdraw(new BigDecimal("200")))
            .isInstanceOf(Account.InsufficientFundsException.class)
            .hasMessageContaining("Insufficient funds");
    }

    // ─── CryptoAccount ────────────────────────────────────────────────────────

    @Test
    void cryptoAccountTypeIsCrypto() {
        var acc = new CryptoAccount("btc1", new BigDecimal("1.00000000"));
        assertThat(acc.getAccountType()).isEqualTo("CRYPTO");
    }

    @Test
    void cryptoWithdrawDeductsAmountPlusFee() {
        // withdraw 1000; fee = 1000 * 0.005 = 5.00; total deducted = 1005
        var acc = new CryptoAccount("btc1", new BigDecimal("2000"));
        acc.withdraw(new BigDecimal("1000"));
        assertThat(acc.getBalance()).isEqualByComparingTo("995.00000000");
    }

    @Test
    void cryptoThrowsWhenInsufficientForAmountPlusFee() {
        var acc = new CryptoAccount("btc1", new BigDecimal("100"));
        // withdraw 100; fee = 0.5; total = 100.5 > 100 → exception
        assertThatThrownBy(() -> acc.withdraw(new BigDecimal("100")))
            .isInstanceOf(Account.InsufficientFundsException.class);
    }

    // ─── Polymorphism ─────────────────────────────────────────────────────────

    @Test
    void depositWorksSameForAllAccountTypes() {
        List<Account> accounts = List.of(
            new CheckingAccount("c1", new BigDecimal("500"), new BigDecimal("200")),
            new SavingsAccount("s1", new BigDecimal("500")),
            new CryptoAccount("btc1", new BigDecimal("500"))
        );
        accounts.forEach(a -> a.deposit(new BigDecimal("100")));
        accounts.forEach(a -> assertThat(a.getBalance()).isEqualByComparingTo("600"));
    }

    @Test
    void statementContainsAccountType() {
        var acc = new SavingsAccount("s1", new BigDecimal("1000"));
        assertThat(acc.getStatement()).contains("SAVINGS").contains("s1");
    }

    @Test
    void transactionLogRecordsDeposit() {
        var acc = new SavingsAccount("s1", new BigDecimal("500"));
        acc.deposit(new BigDecimal("100"));
        assertThat(acc.getTransactionLog()).hasSize(1)
            .first().asString().contains("DEPOSIT");
    }
}
