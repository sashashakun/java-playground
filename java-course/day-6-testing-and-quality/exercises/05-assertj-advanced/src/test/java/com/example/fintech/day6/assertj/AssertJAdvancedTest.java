package com.example.fintech.day6.assertj;

import org.assertj.core.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Exercise 05 — AssertJ Advanced
 *
 * AssertJ comes bundled with spring-boot-starter-test. These exercises cover
 * features that go beyond the basics (assertThat(x).isEqualTo(y)).
 *
 * TypeScript analogy:
 *   SoftAssertions            ≈  expect.soft() (Playwright) or jest-extended soft matchers
 *   extracting("field")       ≈  items.map(x => x.field)
 *   filteredOn(predicate)     ≈  items.filter(predicate)
 *   Condition<T>              ≈  a custom expect.extend matcher
 */
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

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 1 — extracting: assert specific field values across a list
    //
    // Without extracting you'd loop manually. AssertJ lets you project a list:
    //   assertThat(transactions).extracting("id")
    //                           .containsExactlyInAnyOrder("t1","t2","t3","t4","t5")
    //
    // Write assertions that verify:
    //   a) All transaction IDs are present (use extracting("id") + containsExactlyInAnyOrder)
    //   b) All transactions belong to "alice" (use extracting("userId") + containsOnly("alice"))
    //   c) The two CREDIT amounts are 500 and 1000 (filter first, then extracting("amount"))
    //
    // TypeScript: transactions.map(t => t.id)
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO1_extracting_verifiesFieldValues() {
        throw new UnsupportedOperationException("TODO 1");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 2 — filteredOn: filter in-assertion before asserting
    //
    // Without filteredOn you'd filter the list before passing it to assertThat.
    //
    // Write assertions that verify:
    //   a) COMPLETED transactions: exactly 3 (t1, t2, t5)
    //      assertThat(transactions).filteredOn("status", Transaction.TransactionStatus.COMPLETED).hasSize(3)
    //   b) DEBIT transactions: 2
    //      assertThat(transactions).filteredOn(t -> t.type() == DEBIT).hasSize(2)
    //   c) Transactions with amount > 200: 2 (t1=500, t5=1000)
    //      assertThat(transactions).filteredOn(t -> t.amount().compareTo(new BigDecimal("200")) > 0).hasSize(2)
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO2_filteredOn_narrowsAssertionScope() {
        throw new UnsupportedOperationException("TODO 2");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 3 — SoftAssertions: collect all failures, not just the first
    //
    // Normal assertThat fails fast (stops at first failure).
    // SoftAssertions collects ALL failures and reports them together.
    //
    // Use SoftAssertions.assertSoftly(soft -> { ... }) to assert all of these
    // on the `report` object at once:
    //   - report.userId() equals "alice"
    //   - report.totalCredits() == 1500
    //   - report.totalDebits() == 150
    //   - report.netBalance() == 1350
    //   - report.reportCurrency() equals "USD"
    //   - report.transactions() has size 5
    //
    // TypeScript: expect.soft() (Playwright) or running all expects before throwing
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO3_softAssertions_reportAllFailures() {
        throw new UnsupportedOperationException("TODO 3");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 4 — Custom Condition<T>
    //
    // Create a Condition<Transaction> called `completedAndPositive` that is true
    // when a transaction is COMPLETED and has amount > 0.
    //
    //   Condition<Transaction> completedAndPositive = new Condition<>(
    //       t -> t.status() == COMPLETED && t.amount().compareTo(ZERO) > 0,
    //       "completed and positive"
    //   );
    //
    // Then assert:
    //   - t1, t2, t5 satisfy the condition (use assertThat(tx).is(completedAndPositive))
    //   - t3 (FAILED) does NOT satisfy it (use assertThat(tx).isNot(completedAndPositive))
    //   - assertThat(transactions).haveAtLeast(3, completedAndPositive)
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO4_customCondition_describesBusinessRule() {
        throw new UnsupportedOperationException("TODO 4");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 5 — usingRecursiveComparison (ignore timestamps)
    //
    // When comparing records/objects that have fields you want to ignore
    // (e.g., generated IDs, timestamps), use recursive comparison:
    //
    //   assertThat(actual)
    //       .usingRecursiveComparison()
    //       .ignoringFields("timestamp", "id")
    //       .isEqualTo(expected)
    //
    // Create an `expected` Transaction that matches t1 except with different id
    // and timestamp, then verify usingRecursiveComparison().ignoringFields("id","timestamp")
    // considers them equal.
    //
    // This is much cleaner than overriding equals() just for tests.
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO5_recursiveComparison_ignoresTimestamps() {
        throw new UnsupportedOperationException("TODO 5");
    }
}
