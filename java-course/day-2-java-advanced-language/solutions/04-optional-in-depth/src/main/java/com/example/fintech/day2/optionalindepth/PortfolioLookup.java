package com.example.fintech.day2.optionalindepth;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PortfolioLookup {

    public record Asset(String id, String currency, BigDecimal value) {}
    public record Wallet(String id, String currency, List<Asset> assets) {}
    public record Portfolio(String id, String ownerId, List<Wallet> wallets) {}
    public record User(String id, String name, String email, Portfolio portfolio) {}

    public String getEmailOrDefault(User user) {
        return Optional.ofNullable(user)
            .map(User::email)
            .orElse("unknown@example.com");
    }

    public Optional<Wallet> findWallet(User user, String walletId) {
        return Optional.ofNullable(user)
            .map(User::portfolio)
            .map(Portfolio::wallets)
            .flatMap(wallets -> wallets.stream()
                .filter(w -> w.id().equals(walletId))
                .findFirst());
    }

    public BigDecimal getWalletTotal(User user, String walletId) {
        return findWallet(user, walletId)
            .map(w -> w.assets().stream()
                .map(Asset::value)
                .reduce(BigDecimal.ZERO, BigDecimal::add))
            .orElse(BigDecimal.ZERO);
    }

    public String getPrimaryWalletCurrency(User user) {
        return Optional.ofNullable(user)
            .map(User::portfolio)
            .map(Portfolio::wallets)
            .flatMap(wallets -> wallets.isEmpty() ? Optional.empty() : Optional.of(wallets.get(0)))
            .map(Wallet::currency)
            .orElseGet(() -> "USD");
    }

    public Optional<BigDecimal> convertValue(BigDecimal value, String currency,
                                              Map<String, BigDecimal> rates) {
        return Optional.ofNullable(rates.get(currency))
            .map(rate -> value.multiply(rate));
    }

    public Optional<Asset> highestValueAsset(User user) {
        return Optional.ofNullable(user)
            .map(User::portfolio)
            .map(Portfolio::wallets)
            .stream()
            .flatMap(List::stream)
            .flatMap(w -> w.assets().stream())
            .max(Comparator.comparing(Asset::value));
    }
}
