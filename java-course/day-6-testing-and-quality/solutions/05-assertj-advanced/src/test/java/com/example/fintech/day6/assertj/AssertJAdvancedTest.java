package com.example.fintech.day6.assertj;

import org.assertj.core.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class AssertJAdvancedTest {

    private List<Transaction> transactions;
    private LedgerReport report;

    @BeforeEach
    void setUp() {
        Instant now = Instant.now();
        transactions = List.of(
            new Transaction("t1", "alice", new BigDecimal("500.00"), "USD",
                Transaction.TransactionType.CREDIT, Transaction.TransactionStatus.COMPLETED,
                "payroll", now.minusSeconds(3600)),
            new Transaction("t2", "alice", new BigDecimal("100.00"), "USD",
                Transaction.TransactionType.DEBIT, Transaction.TransactionStatus.COMPLETED,
                "rent-pmt", now.minusSeconds(1800)),
            new Transaction("t3", "alice", new BigDecimal("50.00"), "USD",
                Transaction.TransactionType.DEBIT, Transaction.TransactionStatus.FAILED,
                "failed-attempt", now.minusSeconds(900)),
            new Transaction("t4", "alice", new BigDecimal("200.00"), "USD",
                Transaction.TransactionType.TRANSFER, Transaction.TransactionStatus.PENDING,
                "transfer-out", now),
            new Transaction("t5", "alice", new BigDecimal("1000.00"), "USD",
                Transaction.TransactionType.CREDIT, Transaction.TransactionStatus.COMPLETED,
                "bonus", now.minusSeconds(7200))
        );

        report = new LedgerReport("alice", transactions,
            new BigDecimal("1500.00"), new BigDecimal("150.00"),
            new BigDecimal("1350.00"), "USD");
    }

    @Test
    void extracting_verifiesFieldValues() {
        // a) all IDs
        assertThat(transactions).extracting("id")
            .containsExactlyInAnyOrder("t1", "t2", "t3", "t4", "t5");

        // b) all belong to alice
        assertThat(transactions).extracting("userId")
            .containsOnly("alice");

        // c) CREDIT amounts
        assertThat(transactions)
            .filteredOn(t -> t.type() == Transaction.TransactionType.CREDIT)
            .extracting("amount")
            .containsExactlyInAnyOrder(new BigDecimal("500.00"), new BigDecimal("1000.00"));
    }

    @Test
    void filteredOn_narrowsAssertionScope() {
        assertThat(transactions)
            .filteredOn("status", Transaction.TransactionStatus.COMPLETED)
            .hasSize(3);

        assertThat(transactions)
            .filteredOn(t -> t.type() == Transaction.TransactionType.DEBIT)
            .hasSize(2);

        assertThat(transactions)
            .filteredOn(t -> t.amount().compareTo(new BigDecimal("200")) > 0)
            .hasSize(2);
    }

    @Test
    void softAssertions_reportAllFailures() {
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(report.userId()).isEqualTo("alice");
            soft.assertThat(report.totalCredits()).isEqualByComparingTo("1500.00");
            soft.assertThat(report.totalDebits()).isEqualByComparingTo("150.00");
            soft.assertThat(report.netBalance()).isEqualByComparingTo("1350.00");
            soft.assertThat(report.reportCurrency()).isEqualTo("USD");
            soft.assertThat(report.transactions()).hasSize(5);
        });
    }

    @Test
    void customCondition_describesBusinessRule() {
        Condition<Transaction> completedAndPositive = new Condition<>(
            t -> t.status() == Transaction.TransactionStatus.COMPLETED
                 && t.amount().compareTo(BigDecimal.ZERO) > 0,
            "completed and positive amount"
        );

        Transaction t1 = transactions.get(0);
        Transaction t2 = transactions.get(1);
        Transaction t3 = transactions.get(2); // FAILED
        Transaction t5 = transactions.get(4);

        assertThat(t1).is(completedAndPositive);
        assertThat(t2).is(completedAndPositive);
        assertThat(t5).is(completedAndPositive);
        assertThat(t3).isNot(completedAndPositive);

        assertThat(transactions).haveAtLeast(3, completedAndPositive);
    }

    @Test
    void recursiveComparison_ignoresTimestamps() {
        Transaction t1 = transactions.get(0);

        Transaction expected = new Transaction(
            "different-id",       // different id
            "alice",
            new BigDecimal("500.00"),
            "USD",
            Transaction.TransactionType.CREDIT,
            Transaction.TransactionStatus.COMPLETED,
            "payroll",
            Instant.now()         // different timestamp
        );

        assertThat(t1)
            .usingRecursiveComparison()
            .ignoringFields("id", "timestamp")
            .isEqualTo(expected);
    }
}
