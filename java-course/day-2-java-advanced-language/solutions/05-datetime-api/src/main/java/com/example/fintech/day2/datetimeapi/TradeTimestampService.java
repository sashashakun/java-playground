package com.example.fintech.day2.datetimeapi;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TradeTimestampService {

    private static final DateTimeFormatter DISPLAY_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

    public Instant addSettlementDays(Instant tradeTime, int days) {
        return tradeTime.plus(Duration.ofDays(days));
    }

    public String formatForZone(Instant instant, String zoneId) {
        ZoneId zone = ZoneId.of(zoneId);
        ZonedDateTime zdt = instant.atZone(zone);
        return zdt.format(DISPLAY_FORMATTER);
    }

    public String durationBetween(Instant start, Instant end) {
        Duration d = Duration.between(start, end);
        long hours = d.toHours();
        long minutes = d.toMinutesPart();
        return "%dh %dm".formatted(hours, minutes);
    }

    public boolean isWithinMarketHours(Instant instant) {
        ZonedDateTime nyTime = instant.atZone(ZoneId.of("America/New_York"));
        LocalTime time = nyTime.toLocalTime();
        return time.compareTo(LocalTime.of(9, 30)) >= 0
            && time.compareTo(LocalTime.of(16, 0)) < 0;
    }

    public Instant startOfDayUtc(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        return date.atStartOfDay(ZoneOffset.UTC).toInstant();
    }

    public long daysHeld(LocalDate openDate) {
        LocalDate today = LocalDate.now(ZoneId.of("UTC"));
        return ChronoUnit.DAYS.between(openDate, today);
    }
}
