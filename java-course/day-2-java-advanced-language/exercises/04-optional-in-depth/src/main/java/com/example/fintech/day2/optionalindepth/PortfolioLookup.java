package com.example.fintech.day2.optionalindepth;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Exercise 04 — Optional In Depth
 *
 * Navigate a nested portfolio structure using Optional chains.
 * No null checks — only Optional API methods.
 *
 * Data model (all provided, no changes needed):
 *   User → Portfolio → List<Wallet> → List<Asset>
 */
public class PortfolioLookup {

    public record Asset(String id, String currency, BigDecimal value) {}
    public record Wallet(String id, String currency, List<Asset> assets) {}
    public record Portfolio(String id, String ownerId, List<Wallet> wallets) {}
    public record User(String id, String name, String email, Portfolio portfolio) {}

    /**
     * TODO 1 — Get the user's email, or "unknown@example.com" if user is null.
     *
     * Use Optional.ofNullable(user).map(...).orElse(...)
     */
    public String getEmailOrDefault(User user) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 2 — Find a wallet by id within a user's portfolio.
     *
     * Return Optional.empty() if:
     *   - user is null
     *   - user.portfolio() is null
     *   - no wallet with that id exists
     *
     * Use Optional.ofNullable → map → map → flatMap (or filter + findFirst)
     *
     * Hint:
     *   Optional.ofNullable(user)
     *       .map(User::portfolio)
     *       .map(Portfolio::wallets)
     *       .flatMap(wallets -> wallets.stream().filter(w -> w.id().equals(walletId)).findFirst())
     */
    public Optional<Wallet> findWallet(User user, String walletId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 3 — Get the total value of a wallet, or BigDecimal.ZERO if wallet is absent.
     *
     * Chain: findWallet → map to sum of assets → orElse(ZERO)
     *
     * The sum: wallet.assets().stream().map(Asset::value).reduce(BigDecimal.ZERO, BigDecimal::add)
     */
    public BigDecimal getWalletTotal(User user, String walletId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 4 — Get the primary wallet (first wallet) currency, lazily falling back to "USD".
     *
     * "Lazily" means: only compute the fallback if needed.
     * Use orElseGet (not orElse) to demonstrate the difference.
     *
     * If the user has no wallets, return "USD".
     */
    public String getPrimaryWalletCurrency(User user) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 5 — Look up an exchange rate from a map, then apply it to a value.
     *
     * If the rate is missing from the map, return Optional.empty().
     * Otherwise return Optional.of(value.multiply(rate)).
     *
     * Use Optional.ofNullable(rates.get(currency)).map(rate -> value.multiply(rate))
     */
    public Optional<BigDecimal> convertValue(BigDecimal value, String currency,
                                              Map<String, BigDecimal> rates) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 6 — Find the highest-value asset across ALL wallets, or empty if no assets exist.
     *
     * Steps:
     *   1. Get portfolio wallets (null-safe)
     *   2. flatMap wallets to assets
     *   3. max by asset value
     *
     * Full chain with Optional + stream:
     *   Optional.ofNullable(user)
     *       .map(User::portfolio)
     *       .map(Portfolio::wallets)
     *       .stream()                        ← Optional.stream() (Java 9+) turns Optional<List> into Stream<List>
     *       .flatMap(List::stream)           ← Stream<Wallet>
     *       .flatMap(w -> w.assets().stream()) ← Stream<Asset>
     *       .max(Comparator.comparing(Asset::value))
     */
    public Optional<Asset> highestValueAsset(User user) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
