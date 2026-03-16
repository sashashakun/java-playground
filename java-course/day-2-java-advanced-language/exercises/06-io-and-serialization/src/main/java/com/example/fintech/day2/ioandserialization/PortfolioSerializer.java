package com.example.fintech.day2.ioandserialization;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

/**
 * Exercise 06 — I/O & Serialization
 *
 * Serialize and deserialize portfolio data using Jackson,
 * and read/write CSV files using the Files API.
 *
 * Implement the 5 TODO methods.
 */
public class PortfolioSerializer {

    /**
     * JSON-serializable asset.
     * @JsonProperty maps the Java field name to the JSON key.
     */
    public record Asset(
        @JsonProperty("asset_id") String id,
        String currency,
        BigDecimal value,
        String type,
        Instant createdAt
    ) {}

    /** Pre-configured Jackson ObjectMapper for the whole exercise. */
    private final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .enable(SerializationFeature.INDENT_OUTPUT);

    /**
     * TODO 1 — Serialize a list of assets to a JSON string.
     *
     * Use mapper.writeValueAsString(assets).
     * Wrap any IOException in a RuntimeException("Serialization failed", e).
     *
     * Example output (pretty-printed):
     *   [
     *     {
     *       "asset_id" : "a1",
     *       "currency" : "USD",
     *       ...
     *     }
     *   ]
     */
    public String toJson(List<Asset> assets) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 2 — Deserialize a JSON string back to a list of assets.
     *
     * Use mapper.readValue(json, new TypeReference<List<Asset>>() {}).
     * Wrap any IOException in a RuntimeException("Deserialization failed", e).
     */
    public List<Asset> fromJson(String json) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 3 — Write a JSON string to a file, creating parent directories if needed.
     *
     * Steps:
     *   1. Files.createDirectories(path.getParent()) — ensure directory exists
     *      (skip if path has no parent: path.getParent() == null)
     *   2. Files.writeString(path, json) — write the content
     *
     * Propagate IOException (don't wrap it — the caller should handle it).
     */
    public void writeToFile(Path path, String json) throws IOException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 4 — Read a JSON file and deserialize it to a list of assets.
     *
     * Steps:
     *   1. Files.readString(path) — read file content
     *   2. fromJson(content) — deserialize
     *
     * Propagate IOException.
     */
    public List<Asset> readFromFile(Path path) throws IOException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 5 — Parse assets from a CSV string (no file I/O — just String parsing).
     *
     * CSV format (header on first line):
     *   asset_id,currency,value,type
     *   a1,USD,1500.00,STOCK
     *   a2,BTC,45000.00,CRYPTO
     *
     * Use createdAt = Instant.EPOCH for all parsed assets.
     *
     * Skip the header line. Skip blank lines. Parse each data line.
     * Throw IllegalArgumentException if a line doesn't have exactly 4 fields.
     * Throw IllegalArgumentException (wrapping NumberFormatException) if value is not a valid decimal.
     */
    public List<Asset> fromCsv(String csv) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
