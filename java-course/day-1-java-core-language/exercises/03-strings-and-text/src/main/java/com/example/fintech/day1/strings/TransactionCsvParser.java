package com.example.fintech.day1.strings;

import java.util.List;
import java.util.Set;

/**
 * Exercise 03 — Strings & Text
 *
 * Parse CSV transaction records and perform string operations.
 *
 * CSV Format:
 *   id,timestamp,amount_cents,currency,type,description
 *   txn-001,2024-01-15T10:30:00Z,15099,USD,PURCHASE,Coffee at Starbucks
 *
 * Fields:
 *   0: id          — transaction identifier (e.g., "txn-001")
 *   1: timestamp   — ISO-8601 instant (e.g., "2024-01-15T10:30:00Z")
 *   2: amountCents — amount in minor units as a long (e.g., 15099 = $150.99)
 *   3: currency    — ISO 4217 code (e.g., "USD")
 *   4: type        — transaction type (e.g., "PURCHASE")
 *   5: description — free text (may contain spaces)
 *
 * Key String methods you'll need:
 *   s.split(",")              → String[] (split on comma)
 *   s.split(",", N)           → split into at most N parts (last part gets the rest)
 *   s.strip()                 → trim whitespace (Unicode-aware; prefer over trim())
 *   s.isEmpty() / s.isBlank() → empty / whitespace-only check
 *   Long.parseLong(s)         → parse String to long
 *   s.substring(0, 50)        → first 50 chars
 *   s.replace("\t", " ")      → replace tabs with spaces
 */
public class TransactionCsvParser {

    /**
     * Immutable record representing a parsed transaction row.
     * The record auto-generates: constructor, getters (.id(), .timestamp(), etc.),
     * equals(), hashCode(), and toString().
     */
    public record ParsedTransaction(
        String id,
        String timestamp,
        long amountCents,
        String currency,
        String type,
        String description
    ) {}

    /**
     * TODO 1 — Parse a single CSV line into a ParsedTransaction.
     *
     * Steps:
     *   1. Split on "," — but use limit=6 so description can contain commas
     *      hint: csvLine.split(",", 6)
     *   2. Strip whitespace from each field
     *   3. Parse amountCents as long: Long.parseLong(parts[2])
     *   4. Return a new ParsedTransaction
     *
     * Examples:
     *   parseLine("txn-001,2024-01-15T10:30:00Z,15099,USD,PURCHASE,Coffee")
     *   → ParsedTransaction("txn-001", "2024-01-15T10:30:00Z", 15099L, "USD", "PURCHASE", "Coffee")
     *
     *   parseLine("txn-002, 2024-01-16T09:00:00Z, 500, EUR, FEE, Monthly service fee")
     *   → ParsedTransaction("txn-002", "2024-01-16T09:00:00Z", 500L, "EUR", "FEE", "Monthly service fee")
     *
     * Throw IllegalArgumentException if:
     *   - csvLine is null or blank
     *   - does not have exactly 6 comma-separated fields
     *   - amountCents field is not a valid long
     */
    public ParsedTransaction parseLine(String csvLine) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 2 — Format an amount in cents as a human-readable string.
     *
     * Formula: divide cents by 100.0 to get decimal, format with 2 decimal places.
     *
     * Examples:
     *   formatAmount(15099, "USD") → "150.99 USD"
     *   formatAmount(100,   "EUR") → "1.00 EUR"
     *   formatAmount(0,     "CHF") → "0.00 CHF"
     *   formatAmount(1,     "BTC") → "0.01 BTC"  ← satoshis if scale matters, but for now cents
     *
     * Java hint: "%.2f %s".formatted(cents / 100.0, currency)
     */
    public String formatAmount(long amountCents, String currency) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 3 — Sanitize a transaction description for storage.
     *
     * Rules:
     *   1. If null or blank, return "" (empty string)
     *   2. Strip leading/trailing whitespace
     *   3. Replace all tab characters (\t) and newlines (\n, \r) with a single space
     *   4. If longer than 50 characters, truncate to 50
     *
     * Examples:
     *   sanitizeDescription("Coffee at Starbucks") → "Coffee at Starbucks"
     *   sanitizeDescription("  \t Messy\nInput  ") → "Messy Input"
     *   sanitizeDescription("A".repeat(60))         → "A".repeat(50)
     *   sanitizeDescription(null)                   → ""
     *
     * Java hints:
     *   s.replaceAll("[\t\r\n]+", " ")  → replace control chars (regex)
     *   s.substring(0, Math.min(s.length(), 50))  → safe truncation
     */
    public String sanitizeDescription(String description) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 4 — Extract all unique currency codes from a list of CSV lines.
     *
     * Returns a sorted Set of distinct currency codes found in valid CSV lines.
     * Skip lines that fail to parse (catch the exception and continue).
     *
     * Example:
     *   lines = ["txn-1,...,USD,...", "txn-2,...,EUR,...", "txn-3,...,USD,..."]
     *   → Set{"EUR", "USD"}  (sorted, no duplicates)
     *
     * Java hints:
     *   new TreeSet<>()           → sorted Set
     *   parseLine(line).currency() → get currency from parsed record
     */
    public Set<String> extractCurrencies(List<String> csvLines) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 5 — Check if a timestamp string looks like a valid ISO-8601 instant.
     *
     * A valid ISO-8601 instant ends with 'Z' and contains a 'T'.
     * We do a basic structural check here — not full validation.
     *
     * Rules:
     *   - Not null, not blank
     *   - Contains the character 'T'
     *   - Ends with 'Z' (UTC) or '+00:00'
     *   - Length >= 20 (minimum: "2024-01-15T10:30:00Z")
     *
     * Examples:
     *   isValidIso8601("2024-01-15T10:30:00Z")     → true
     *   isValidIso8601("2024-01-15T10:30:00.000Z") → true
     *   isValidIso8601("2024-01-15")               → false (no T or Z)
     *   isValidIso8601("")                         → false
     *   isValidIso8601(null)                       → false
     */
    public boolean isValidIso8601(String timestamp) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
