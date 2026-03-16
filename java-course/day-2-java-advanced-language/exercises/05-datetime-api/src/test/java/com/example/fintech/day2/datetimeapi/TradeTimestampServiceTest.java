package com.example.fintech.day2.datetimeapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.*;

class TradeTimestampServiceTest {

    private TradeTimestampService svc;

    @BeforeEach
    void setUp() { svc = new TradeTimestampService(); }

    @Test
    void addSettlementDaysAddsCalendarDays() {
        Instant trade = Instant.parse("2024-01-15T10:00:00Z");
        Instant settled = svc.addSettlementDays(trade, 2);
        assertThat(settled).isEqualTo(Instant.parse("2024-01-17T10:00:00Z"));
    }

    @Test
    void addSettlementDaysZeroIsNoOp() {
        Instant trade = Instant.parse("2024-06-01T12:00:00Z");
        assertThat(svc.addSettlementDays(trade, 0)).isEqualTo(trade);
    }

    @Test
    void formatForZoneConvertsToEastern() {
        // 2024-01-15T15:30:00Z = 10:30:00 EST (UTC-5)
        String formatted = svc.formatForZone(
            Instant.parse("2024-01-15T15:30:00Z"),
            "America/New_York"
        );
        assertThat(formatted).contains("2024-01-15").contains("10:30:00");
    }

    @Test
    void formatForZoneConvertsToZurich() {
        // 2024-01-15T09:30:00Z = 10:30:00 CET (UTC+1 in winter)
        String formatted = svc.formatForZone(
            Instant.parse("2024-01-15T09:30:00Z"),
            "Europe/Zurich"
        );
        assertThat(formatted).contains("2024-01-15").contains("10:30:00");
    }

    @Test
    void durationBetweenTwoHoursThirtyMinutes() {
        Instant start = Instant.parse("2024-01-15T10:00:00Z");
        Instant end   = Instant.parse("2024-01-15T12:30:00Z");
        assertThat(svc.durationBetween(start, end)).isEqualTo("2h 30m");
    }

    @Test
    void durationBetweenLessThanHour() {
        Instant start = Instant.parse("2024-01-15T10:00:00Z");
        Instant end   = Instant.parse("2024-01-15T10:45:00Z");
        assertThat(svc.durationBetween(start, end)).isEqualTo("0h 45m");
    }

    @Test
    void isWithinMarketHoursTrueAtOpen() {
        // 09:30 Eastern = 14:30 UTC (winter, UTC-5)
        Instant atOpen = Instant.parse("2024-01-15T14:30:00Z");
        assertThat(svc.isWithinMarketHours(atOpen)).isTrue();
    }

    @Test
    void isWithinMarketHoursFalseAfterClose() {
        // 16:00 Eastern = 21:00 UTC
        Instant atClose = Instant.parse("2024-01-15T21:00:00Z");
        assertThat(svc.isWithinMarketHours(atClose)).isFalse();
    }

    @Test
    void isWithinMarketHoursFalseBeforeOpen() {
        // 09:00 Eastern = 14:00 UTC
        Instant before = Instant.parse("2024-01-15T14:00:00Z");
        assertThat(svc.isWithinMarketHours(before)).isFalse();
    }

    @Test
    void startOfDayUtcParsesDate() {
        Instant start = svc.startOfDayUtc("2024-01-15");
        assertThat(start).isEqualTo(Instant.parse("2024-01-15T00:00:00Z"));
    }

    @Test
    void daysHeldIsPositiveForPastDate() {
        LocalDate longAgo = LocalDate.of(2020, 1, 1);
        assertThat(svc.daysHeld(longAgo)).isGreaterThan(365);
    }

    @Test
    void daysHeldIsZeroForToday() {
        LocalDate today = LocalDate.now(ZoneId.of("UTC"));
        assertThat(svc.daysHeld(today)).isEqualTo(0L);
    }
}
