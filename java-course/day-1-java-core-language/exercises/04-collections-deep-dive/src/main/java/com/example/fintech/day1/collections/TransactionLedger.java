package com.example.fintech.day1.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Exercise 04 — Collections Deep Dive
 *
 * A queryable in-memory ledger of transactions.
 * Implement all 7 TODO methods. Run: ./gradlew test
 *
 * Stream quick reference:
 *   list.stream()                              → Stream<Transaction>
 *   .filter(t -> condition)                    → keep matching elements
 *   .map(t -> t.currency())                    → transform each element
 *   .mapToLong(t -> t.amountCents())           → primitive LongStream
 *   .sum() / .count()                          → terminal: sum / count
 *   .toList()                                  → collect to List (Java 16+)
 *   .collect(Collectors.groupingBy(fn))        → Map<K, List<T>>
 *   .sorted(Comparator.comparingLong(fn))      → sort
 *   .sorted(Comparator.comparingLong(fn).reversed()) → sort descending
 *   .findFirst()                               → Optional<T>
 *
 * Import hints (add at top of file):
 *   import java.util.stream.Collectors;
 *   import java.util.Comparator;
 */
public class TransactionLedger {

    /** The underlying storage — package-visible for test inspection. */
    final List<Transaction> transactions = new ArrayList<>();

    /**
     * Stats summary returned by getStats().
     * Another record — note records can be nested inside classes.
     */
    public record LedgerStats(int count, long totalAmountCents, Set<String> uniqueCurrencies) {}

    /**
     * TODO 1 — Add a transaction to the ledger.
     *
     * Rules:
     *   - Throw IllegalArgumentException if tx is null
     *   - Throw IllegalStateException("Duplicate transaction: " + tx.id()) if
     *     a transaction with the same id already exists
     *   - Otherwise add to the internal list
     *
     * Java hint: check for existing ID using stream:
     *   transactions.stream().anyMatch(t -> t.id().equals(tx.id()))
     */
    public void add(Transaction tx) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 2 — Find a transaction by its ID.
     *
     * Returns Optional.empty() if not found (never returns null).
     *
     * Java hint:
     *   transactions.stream()
     *       .filter(t -> t.id().equals(id))
     *       .findFirst()
     */
    public Optional<Transaction> findById(String id) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 3 — Find all transactions for a given currency code.
     *
     * Returns an empty list if none found.
     * Currency comparison is case-insensitive (normalize to upper case before comparing).
     *
     * Example: findByCurrency("usd") and findByCurrency("USD") return the same results.
     */
    public List<Transaction> findByCurrency(String currency) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 4 — Sum all transaction amounts for a given currency.
     *
     * Returns 0 if no transactions exist for that currency.
     *
     * Java hint: use mapToLong + sum
     *   transactions.stream()
     *       .filter(...)
     *       .mapToLong(Transaction::amountCents)
     *       .sum()
     */
    public long totalByCurrency(String currency) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 5 — Group all transactions by currency code.
     *
     * Returns a Map where each key is a currency code and each value is the
     * list of transactions in that currency.
     *
     * Example:
     *   { "USD" → [tx1, tx3], "EUR" → [tx2], "CHF" → [tx4] }
     *
     * Java hint:
     *   import java.util.stream.Collectors;
     *   transactions.stream()
     *       .collect(Collectors.groupingBy(Transaction::currency))
     */
    public Map<String, List<Transaction>> groupByCurrency() {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 6 — Return the top N transactions by amount (descending).
     *
     * If there are fewer than N transactions, return all of them.
     *
     * Example: topNByAmount(2) on [tx(100), tx(500), tx(250)]
     *   → [tx(500), tx(250)]
     *
     * Java hint:
     *   import java.util.Comparator;
     *   transactions.stream()
     *       .sorted(Comparator.comparingLong(Transaction::amountCents).reversed())
     *       .limit(n)
     *       .toList()
     */
    public List<Transaction> topNByAmount(int n) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 7 — Return aggregate statistics for the entire ledger.
     *
     * count            — total number of transactions
     * totalAmountCents — sum of all amountCents
     * uniqueCurrencies — Set of distinct currency codes (use Collectors.toSet())
     *
     * Example for [tx(100,USD), tx(200,EUR), tx(300,USD)]:
     *   LedgerStats(3, 600L, Set.of("USD","EUR"))
     */
    public LedgerStats getStats() {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
