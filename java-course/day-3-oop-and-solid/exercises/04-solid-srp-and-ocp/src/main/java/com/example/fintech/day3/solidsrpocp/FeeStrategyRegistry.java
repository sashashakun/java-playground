package com.example.fintech.day3.solidsrpocp;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Exercise 04 — Fee Strategy Registry (OCP in action)
 *
 * Maps transaction type strings to FeeStrategy implementations.
 *
 * Adding a new transaction type = register a new strategy.
 * No if/else chains needed. No existing code changes.
 *
 * TODO 1 — register(String transactionType, FeeStrategy strategy):
 *   Store the mapping in an internal map.
 *
 * TODO 2 — getStrategy(String transactionType) → Optional<FeeStrategy>:
 *   Return the strategy for the type, or Optional.empty() if unknown.
 *
 * TODO 3 — calculateFee(String transactionType, BigDecimal amount):
 *   Get the strategy (or throw IllegalArgumentException if unknown)
 *   and delegate to strategy.calculate(amount).
 *
 * TODO 4 — static defaultRegistry():
 *   Return a registry pre-loaded with:
 *     "PURCHASE"  → PurchaseFeeStrategy
 *     "TRANSFER"  → TransferFeeStrategy
 *     "CRYPTO"    → CryptoFeeStrategy
 *     "FEE"       → ZeroFeeStrategy
 *     "REFUND"    → ZeroFeeStrategy
 */
public class FeeStrategyRegistry {

    private final Map<String, FeeStrategy> strategies = new HashMap<>();

    public void register(String transactionType, FeeStrategy strategy) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Optional<FeeStrategy> getStrategy(String transactionType) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public java.math.BigDecimal calculateFee(String transactionType, java.math.BigDecimal amount) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static FeeStrategyRegistry defaultRegistry() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
