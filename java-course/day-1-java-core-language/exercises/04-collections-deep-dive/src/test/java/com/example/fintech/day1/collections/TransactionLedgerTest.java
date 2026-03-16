package com.example.fintech.day1.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TransactionLedger")
class TransactionLedgerTest {

    private TransactionLedger ledger;

    // Test fixtures
    private final Transaction tx1 = new Transaction("txn-001", 1000L, "USD", "PURCHASE", "Coffee");
    private final Transaction tx2 = new Transaction("txn-002", 5000L, "EUR", "DEPOSIT",  "Salary");
    private final Transaction tx3 = new Transaction("txn-003", 2500L, "USD", "PURCHASE", "Lunch");
    private final Transaction tx4 = new Transaction("txn-004", 100L,  "CHF", "FEE",      "Service fee");
    private final Transaction tx5 = new Transaction("txn-005", 7500L, "EUR", "DEPOSIT",  "Bonus");

    @BeforeEach
    void setUp() {
        ledger = new TransactionLedger();
    }

    @Test
    @DisplayName("add stores transactions and rejects nulls")
    void add_storesAndRejectsNull() {
        ledger.add(tx1);
        assertThat(ledger.transactions).hasSize(1);

        assertThatThrownBy(() -> ledger.add(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("add throws IllegalStateException on duplicate ID")
    void add_throwsOnDuplicateId() {
        ledger.add(tx1);
        Transaction duplicate = new Transaction("txn-001", 9999L, "USD", "PURCHASE", "Other");

        assertThatThrownBy(() -> ledger.add(duplicate))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("txn-001");
    }

    @Test
    @DisplayName("findById returns the transaction or empty Optional")
    void findById_returnsCorrectly() {
        ledger.add(tx1);
        ledger.add(tx2);

        assertThat(ledger.findById("txn-001")).contains(tx1);
        assertThat(ledger.findById("txn-002")).contains(tx2);
        assertThat(ledger.findById("txn-999")).isEmpty();
    }

    @Test
    @DisplayName("findByCurrency returns matching transactions, case-insensitive")
    void findByCurrency_caseInsensitive() {
        ledger.add(tx1); // USD
        ledger.add(tx2); // EUR
        ledger.add(tx3); // USD
        ledger.add(tx4); // CHF

        assertThat(ledger.findByCurrency("USD")).containsExactlyInAnyOrder(tx1, tx3);
        assertThat(ledger.findByCurrency("usd")).containsExactlyInAnyOrder(tx1, tx3); // lowercase
        assertThat(ledger.findByCurrency("EUR")).containsExactly(tx2);
        assertThat(ledger.findByCurrency("GBP")).isEmpty();
    }

    @Test
    @DisplayName("totalByCurrency sums amounts for given currency")
    void totalByCurrency_sumsCorrectly() {
        ledger.add(tx1); // 1000 USD
        ledger.add(tx2); // 5000 EUR
        ledger.add(tx3); // 2500 USD

        assertThat(ledger.totalByCurrency("USD")).isEqualTo(3500L); // 1000 + 2500
        assertThat(ledger.totalByCurrency("EUR")).isEqualTo(5000L);
        assertThat(ledger.totalByCurrency("GBP")).isEqualTo(0L);    // not present
    }

    @Test
    @DisplayName("groupByCurrency creates correct groups")
    void groupByCurrency_groupsCorrectly() {
        ledger.add(tx1); // USD
        ledger.add(tx2); // EUR
        ledger.add(tx3); // USD
        ledger.add(tx4); // CHF
        ledger.add(tx5); // EUR

        Map<String, List<Transaction>> grouped = ledger.groupByCurrency();

        assertThat(grouped).containsOnlyKeys("USD", "EUR", "CHF");
        assertThat(grouped.get("USD")).containsExactlyInAnyOrder(tx1, tx3);
        assertThat(grouped.get("EUR")).containsExactlyInAnyOrder(tx2, tx5);
        assertThat(grouped.get("CHF")).containsExactly(tx4);
    }

    @Test
    @DisplayName("topNByAmount returns N largest transactions in descending order")
    void topNByAmount_returnsTopN() {
        ledger.add(tx1); // 1000
        ledger.add(tx2); // 5000
        ledger.add(tx3); // 2500
        ledger.add(tx4); // 100
        ledger.add(tx5); // 7500

        List<Transaction> top3 = ledger.topNByAmount(3);
        assertThat(top3).hasSize(3);
        assertThat(top3.get(0)).isEqualTo(tx5); // 7500 — highest
        assertThat(top3.get(1)).isEqualTo(tx2); // 5000
        assertThat(top3.get(2)).isEqualTo(tx3); // 2500

        // N larger than list size → return all
        assertThat(ledger.topNByAmount(100)).hasSize(5);
    }

    @Test
    @DisplayName("getStats returns correct aggregate statistics")
    void getStats_aggregatesCorrectly() {
        ledger.add(tx1); // 1000 USD
        ledger.add(tx2); // 5000 EUR
        ledger.add(tx3); // 2500 USD
        ledger.add(tx4); // 100  CHF

        TransactionLedger.LedgerStats stats = ledger.getStats();

        assertThat(stats.count()).isEqualTo(4);
        assertThat(stats.totalAmountCents()).isEqualTo(8600L); // 1000+5000+2500+100
        assertThat(stats.uniqueCurrencies()).containsExactlyInAnyOrder("USD", "EUR", "CHF");
    }

    @Test
    @DisplayName("getStats returns zeros and empty set for empty ledger")
    void getStats_emptyLedger() {
        TransactionLedger.LedgerStats stats = ledger.getStats();

        assertThat(stats.count()).isEqualTo(0);
        assertThat(stats.totalAmountCents()).isEqualTo(0L);
        assertThat(stats.uniqueCurrencies()).isEmpty();
    }
}
