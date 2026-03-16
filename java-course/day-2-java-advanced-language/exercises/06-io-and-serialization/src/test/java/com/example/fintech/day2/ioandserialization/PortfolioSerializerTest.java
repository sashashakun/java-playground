package com.example.fintech.day2.ioandserialization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PortfolioSerializerTest {

    private final PortfolioSerializer s = new PortfolioSerializer();

    private PortfolioSerializer.Asset asset(String id, String cur, String val, String type) {
        return new PortfolioSerializer.Asset(id, cur, new BigDecimal(val), type, Instant.EPOCH);
    }

    private final List<PortfolioSerializer.Asset> ASSETS = List.of(
        asset("a1", "USD", "1500.00", "STOCK"),
        asset("a2", "BTC", "45000.00", "CRYPTO")
    );

    @Test
    void toJsonContainsAssetId() {
        String json = s.toJson(ASSETS);
        assertThat(json).contains("\"asset_id\"").contains("a1").contains("a2");
    }

    @Test
    void roundTripPreservesData() {
        String json = s.toJson(ASSETS);
        List<PortfolioSerializer.Asset> deserialized = s.fromJson(json);
        assertThat(deserialized).hasSize(2);
        assertThat(deserialized.get(0).id()).isEqualTo("a1");
        assertThat(deserialized.get(0).currency()).isEqualTo("USD");
        assertThat(deserialized.get(0).value()).isEqualByComparingTo("1500.00");
    }

    @Test
    void writeToFileAndReadBack(@TempDir Path tmp) throws IOException {
        Path file = tmp.resolve("portfolio.json");
        String json = s.toJson(ASSETS);
        s.writeToFile(file, json);
        assertThat(Files.exists(file)).isTrue();

        List<PortfolioSerializer.Asset> loaded = s.readFromFile(file);
        assertThat(loaded).hasSize(2).extracting(PortfolioSerializer.Asset::id)
            .containsExactlyInAnyOrder("a1", "a2");
    }

    @Test
    void writeToFileCreatesParentDirectories(@TempDir Path tmp) throws IOException {
        Path deep = tmp.resolve("subdir/nested/portfolio.json");
        s.writeToFile(deep, s.toJson(ASSETS));
        assertThat(Files.exists(deep)).isTrue();
    }

    @Test
    void fromCsvParsesCorrectly() {
        String csv = """
            asset_id,currency,value,type
            a1,USD,1500.00,STOCK
            a2,BTC,45000.00,CRYPTO
            """;
        List<PortfolioSerializer.Asset> assets = s.fromCsv(csv);
        assertThat(assets).hasSize(2);
        assertThat(assets.get(0).id()).isEqualTo("a1");
        assertThat(assets.get(0).currency()).isEqualTo("USD");
        assertThat(assets.get(0).value()).isEqualByComparingTo("1500.00");
        assertThat(assets.get(0).createdAt()).isEqualTo(Instant.EPOCH);
    }

    @Test
    void fromCsvSkipsBlankLines() {
        String csv = "asset_id,currency,value,type\na1,USD,100.00,STOCK\n\na2,EUR,200.00,BOND\n";
        assertThat(s.fromCsv(csv)).hasSize(2);
    }

    @Test
    void fromCsvThrowsOnBadFieldCount() {
        String csv = "asset_id,currency,value,type\na1,USD\n";
        assertThatThrownBy(() -> s.fromCsv(csv))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void fromCsvThrowsOnInvalidNumber() {
        String csv = "asset_id,currency,value,type\na1,USD,NOT_A_NUMBER,STOCK\n";
        assertThatThrownBy(() -> s.fromCsv(csv))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
