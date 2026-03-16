package com.example.fintech.day1.strings;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/** Solution for Exercise 03 — Strings & Text */
public class TransactionCsvParser {

    public record ParsedTransaction(
        String id, String timestamp, long amountCents,
        String currency, String type, String description
    ) {}

    public ParsedTransaction parseLine(String csvLine) {
        if (csvLine == null || csvLine.isBlank())
            throw new IllegalArgumentException("CSV line must not be blank");

        String[] parts = csvLine.split(",", 6);
        if (parts.length != 6)
            throw new IllegalArgumentException(
                "Expected 6 fields, got " + parts.length + ": " + csvLine);

        String id          = parts[0].strip();
        String timestamp   = parts[1].strip();
        String amountStr   = parts[2].strip();
        String currency    = parts[3].strip();
        String type        = parts[4].strip();
        String description = parts[5].strip();

        long amountCents;
        try {
            amountCents = Long.parseLong(amountStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid amount_cents '" + amountStr + "': " + e.getMessage(), e);
        }

        return new ParsedTransaction(id, timestamp, amountCents, currency, type, description);
    }

    public String formatAmount(long amountCents, String currency) {
        return "%.2f %s".formatted(amountCents / 100.0, currency);
    }

    public String sanitizeDescription(String description) {
        if (description == null || description.isBlank()) return "";
        String sanitized = description.strip().replaceAll("[\t\r\n]+", " ");
        return sanitized.substring(0, Math.min(sanitized.length(), 50));
    }

    public Set<String> extractCurrencies(List<String> csvLines) {
        Set<String> currencies = new TreeSet<>();
        for (String line : csvLines) {
            try {
                currencies.add(parseLine(line).currency());
            } catch (Exception ignored) {
                // Skip unparseable lines
            }
        }
        return currencies;
    }

    public boolean isValidIso8601(String timestamp) {
        if (timestamp == null || timestamp.isBlank()) return false;
        if (timestamp.length() < 20) return false;
        if (!timestamp.contains("T")) return false;
        return timestamp.endsWith("Z") || timestamp.endsWith("+00:00");
    }
}
