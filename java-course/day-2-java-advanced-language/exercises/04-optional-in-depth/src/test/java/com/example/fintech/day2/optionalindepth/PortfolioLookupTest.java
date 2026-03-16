package com.example.fintech.day2.optionalindepth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class PortfolioLookupTest {

    private PortfolioLookup lookup;

    private PortfolioLookup.Asset a(String id, String cur, String val) {
        return new PortfolioLookup.Asset(id, cur, new BigDecimal(val));
    }

    private PortfolioLookup.Wallet wallet(String id, String cur, PortfolioLookup.Asset... assets) {
        return new PortfolioLookup.Wallet(id, cur, List.of(assets));
    }

    private PortfolioLookup.Portfolio portfolio(String id, String owner, PortfolioLookup.Wallet... wallets) {
        return new PortfolioLookup.Portfolio(id, owner, List.of(wallets));
    }

    private PortfolioLookup.User user(String id, String name, String email, PortfolioLookup.Portfolio p) {
        return new PortfolioLookup.User(id, name, email, p);
    }

    @BeforeEach
    void setUp() { lookup = new PortfolioLookup(); }

    @Test
    void getEmailOrDefaultReturnsEmail() {
        var u = user("u1", "Alice", "alice@example.com", null);
        assertThat(lookup.getEmailOrDefault(u)).isEqualTo("alice@example.com");
    }

    @Test
    void getEmailOrDefaultHandlesNull() {
        assertThat(lookup.getEmailOrDefault(null)).isEqualTo("unknown@example.com");
    }

    @Test
    void findWalletFindsById() {
        var w = wallet("w1", "USD", a("a1", "USD", "100"));
        var p = portfolio("p1", "u1", w);
        var u = user("u1", "Bob", "bob@example.com", p);
        assertThat(lookup.findWallet(u, "w1")).isPresent().get()
            .extracting(PortfolioLookup.Wallet::id).isEqualTo("w1");
    }

    @Test
    void findWalletReturnsEmptyWhenMissing() {
        var p = portfolio("p1", "u1", wallet("w1", "USD"));
        var u = user("u1", "Bob", "bob@example.com", p);
        assertThat(lookup.findWallet(u, "w999")).isEmpty();
    }

    @Test
    void findWalletHandlesNullUser() {
        assertThat(lookup.findWallet(null, "w1")).isEmpty();
    }

    @Test
    void getWalletTotalSumsAssets() {
        var w = wallet("w1", "USD", a("a1", "USD", "100"), a("a2", "USD", "250"));
        var u = user("u1", "Carol", "carol@example.com", portfolio("p1", "u1", w));
        assertThat(lookup.getWalletTotal(u, "w1")).isEqualByComparingTo("350");
    }

    @Test
    void getWalletTotalReturnsZeroWhenMissing() {
        var u = user("u1", "Carol", "carol@example.com", portfolio("p1", "u1"));
        assertThat(lookup.getWalletTotal(u, "w99")).isEqualByComparingTo("0");
    }

    @Test
    void getPrimaryWalletCurrencyReturnsFirst() {
        var w1 = wallet("w1", "EUR");
        var w2 = wallet("w2", "USD");
        var u = user("u1", "Dave", "dave@example.com", portfolio("p1", "u1", w1, w2));
        assertThat(lookup.getPrimaryWalletCurrency(u)).isEqualTo("EUR");
    }

    @Test
    void getPrimaryWalletCurrencyFallsBackToUsd() {
        var u = user("u1", "Dave", "dave@example.com", portfolio("p1", "u1"));
        assertThat(lookup.getPrimaryWalletCurrency(u)).isEqualTo("USD");
    }

    @Test
    void convertValueAppliesRate() {
        var rates = Map.of("EUR", new BigDecimal("1.10"));
        assertThat(lookup.convertValue(new BigDecimal("100"), "EUR", rates))
            .isPresent().get().extracting(Object::toString)
            .asString().startsWith("110");
    }

    @Test
    void convertValueReturnsEmptyForMissingRate() {
        assertThat(lookup.convertValue(new BigDecimal("100"), "JPY", Map.of())).isEmpty();
    }

    @Test
    void highestValueAssetFindsMax() {
        var w1 = wallet("w1", "USD", a("a1", "USD", "500"), a("a2", "USD", "1500"));
        var w2 = wallet("w2", "EUR", a("a3", "EUR", "800"));
        var u = user("u1", "Eve", "eve@example.com", portfolio("p1", "u1", w1, w2));
        assertThat(lookup.highestValueAsset(u)).isPresent()
            .map(PortfolioLookup.Asset::id).contains("a2");
    }

    @Test
    void highestValueAssetEmptyForNullUser() {
        assertThat(lookup.highestValueAsset(null)).isEmpty();
    }
}
