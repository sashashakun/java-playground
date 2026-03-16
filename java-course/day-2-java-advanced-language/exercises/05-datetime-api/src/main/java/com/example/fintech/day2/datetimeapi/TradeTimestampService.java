package com.example.fintech.day2.datetimeapi;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Exercise 05 — DateTime API
 *
 * Financial timestamp operations using java.time.
 * Implement the 6 TODO methods.
 *
 * Key rule for fintech: store Instant (UTC epoch) in the database.
 * Convert to ZonedDateTime only for display.
 */
public class TradeTimestampService {

    /** ISO-8601 with zone offset, e.g. "2024-01-15T10:30:00+01:00[Europe/Zurich]" */
    private static final DateTimeFormatter DISPLAY_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

    /**
     * TODO 1 — Add a settlement delay to a trade timestamp.
     *
     * Standard equity settlement is T+2 (trade date + 2 business days).
     * For simplicity here, just add the given number of calendar days.
     *
     * Return the new Instant.
     *
     * Example:
     *   addSettlementDays(Instant.parse("2024-01-15T10:00:00Z"), 2)
     *   → Instant.parse("2024-01-17T10:00:00Z")
     *
     * Hint: tradeTime.plus(Duration.ofDays(days))
     */
    public Instant addSettlementDays(Instant tradeTime, int days) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 2 — Format an Instant for display in a given timezone.
     *
     * Use DISPLAY_FORMATTER ("yyyy-MM-dd HH:mm:ss z").
     *
     * Example:
     *   formatForZone(Instant.parse("2024-01-15T09:30:00Z"), "Europe/Zurich")
     *   → "2024-01-15 10:30:00 CET"
     *
     * Steps:
     *   1. ZoneId zone = ZoneId.of(zoneId)
     *   2. ZonedDateTime zdt = instant.atZone(zone)
     *   3. return zdt.format(DISPLAY_FORMATTER)
     */
    public String formatForZone(Instant instant, String zoneId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 3 — Compute the duration between two instants as a human-readable string.
     *
     * Format: "Xh Ym" (hours and minutes), e.g. "2h 30m"
     * If less than 1 hour: "0h 45m"
     *
     * Example:
     *   durationBetween(
     *       Instant.parse("2024-01-15T10:00:00Z"),
     *       Instant.parse("2024-01-15T12:30:00Z"))
     *   → "2h 30m"
     *
     * Hint:
     *   Duration d = Duration.between(start, end);
     *   long hours = d.toHours();
     *   long minutes = d.toMinutesPart();  // Java 9+: minutes within the hour
     */
    public String durationBetween(Instant start, Instant end) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 4 — Check if a trade timestamp falls within market hours.
     *
     * NYSE market hours: 09:30–16:00 Eastern Time (America/New_York).
     * Return true if the instant falls within this window (inclusive of start, exclusive of end).
     *
     * Steps:
     *   1. Convert Instant to ZonedDateTime in "America/New_York"
     *   2. Extract LocalTime from the ZonedDateTime
     *   3. Check if time is >= 09:30 and < 16:00
     *
     * Hint:
     *   ZonedDateTime nyTime = instant.atZone(ZoneId.of("America/New_York"));
     *   java.time.LocalTime time = nyTime.toLocalTime();
     *   time.compareTo(java.time.LocalTime.of(9, 30)) >= 0
     */
    public boolean isWithinMarketHours(Instant instant) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 5 — Parse an ISO-8601 date string and return the start of that day in UTC.
     *
     * Example:
     *   startOfDayUtc("2024-01-15") → Instant.parse("2024-01-15T00:00:00Z")
     *
     * Hint:
     *   LocalDate date = LocalDate.parse(dateStr);
     *   date.atStartOfDay(ZoneOffset.UTC).toInstant()
     *   // ZoneOffset.UTC is a ZoneId so atStartOfDay() works
     */
    public Instant startOfDayUtc(String dateStr) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 6 — Calculate how many calendar days a position has been held.
     *
     * A position is "held" from openDate (inclusive) to today (exclusive).
     *
     * Example: opened 2024-01-01, today is 2024-01-15 → 14 days held
     *
     * Hint:
     *   LocalDate today = LocalDate.now(ZoneId.of("UTC"));
     *   return ChronoUnit.DAYS.between(openDate, today);
     */
    public long daysHeld(LocalDate openDate) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
