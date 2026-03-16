package com.example.fintech.day3.solidsrpocp;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FeeStrategyRegistry {

    private final Map<String, FeeStrategy> strategies = new HashMap<>();

    public void register(String transactionType, FeeStrategy strategy) {
        strategies.put(transactionType, strategy);
    }

    public Optional<FeeStrategy> getStrategy(String transactionType) {
        return Optional.ofNullable(strategies.get(transactionType));
    }

    public BigDecimal calculateFee(String transactionType, BigDecimal amount) {
        return getStrategy(transactionType)
            .orElseThrow(() -> new IllegalArgumentException("Unknown transaction type: " + transactionType))
            .calculate(amount);
    }

    public static FeeStrategyRegistry defaultRegistry() {
        var registry = new FeeStrategyRegistry();
        registry.register("PURCHASE", new FeeStrategies.PurchaseFeeStrategy());
        registry.register("TRANSFER", new FeeStrategies.TransferFeeStrategy());
        registry.register("CRYPTO",   new FeeStrategies.CryptoFeeStrategy());
        registry.register("FEE",      new FeeStrategies.ZeroFeeStrategy());
        registry.register("REFUND",   new FeeStrategies.ZeroFeeStrategy());
        return registry;
    }
}
