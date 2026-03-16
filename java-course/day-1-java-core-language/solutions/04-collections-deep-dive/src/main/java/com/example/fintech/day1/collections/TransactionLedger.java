package com.example.fintech.day1.collections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/** Solution for Exercise 04 — Collections Deep Dive */
public class TransactionLedger {

    final List<Transaction> transactions = new ArrayList<>();

    public record LedgerStats(int count, long totalAmountCents, Set<String> uniqueCurrencies) {}

    public void add(Transaction tx) {
        Objects.requireNonNull(tx, "transaction must not be null");
        if (transactions.stream().anyMatch(t -> t.id().equals(tx.id()))) {
            throw new IllegalStateException("Duplicate transaction: " + tx.id());
        }
        transactions.add(tx);
    }

    public Optional<Transaction> findById(String id) {
        return transactions.stream()
            .filter(t -> t.id().equals(id))
            .findFirst();
    }

    public List<Transaction> findByCurrency(String currency) {
        String normalized = currency.toUpperCase();
        return transactions.stream()
            .filter(t -> t.currency().equalsIgnoreCase(normalized))
            .toList();
    }

    public long totalByCurrency(String currency) {
        return transactions.stream()
            .filter(t -> t.currency().equalsIgnoreCase(currency))
            .mapToLong(Transaction::amountCents)
            .sum();
    }

    public Map<String, List<Transaction>> groupByCurrency() {
        return transactions.stream()
            .collect(Collectors.groupingBy(Transaction::currency));
    }

    public List<Transaction> topNByAmount(int n) {
        return transactions.stream()
            .sorted(Comparator.comparingLong(Transaction::amountCents).reversed())
            .limit(n)
            .toList();
    }

    public LedgerStats getStats() {
        int count = transactions.size();
        long total = transactions.stream().mapToLong(Transaction::amountCents).sum();
        Set<String> currencies = transactions.stream()
            .map(Transaction::currency)
            .collect(Collectors.toSet());
        return new LedgerStats(count, total, currencies);
    }
}
