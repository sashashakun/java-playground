package com.example.fintech.day1.strings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TransactionCsvParser")
class TransactionCsvParserTest {

    private TransactionCsvParser parser;

    @BeforeEach
    void setUp() {
        parser = new TransactionCsvParser();
    }

    @Test
    @DisplayName("parseLine parses a standard CSV line correctly")
    void parseLine_parsesStandardLine() {
        var tx = parser.parseLine("txn-001,2024-01-15T10:30:00Z,15099,USD,PURCHASE,Coffee at Starbucks");

        assertThat(tx.id()).isEqualTo("txn-001");
        assertThat(tx.timestamp()).isEqualTo("2024-01-15T10:30:00Z");
        assertThat(tx.amountCents()).isEqualTo(15099L);
        assertThat(tx.currency()).isEqualTo("USD");
        assertThat(tx.type()).isEqualTo("PURCHASE");
        assertThat(tx.description()).isEqualTo("Coffee at Starbucks");
    }

    @Test
    @DisplayName("parseLine strips whitespace from all fields")
    void parseLine_stripsWhitespace() {
        var tx = parser.parseLine("  txn-002 , 2024-01-16T09:00:00Z , 500 , EUR , FEE , Monthly fee ");

        assertThat(tx.id()).isEqualTo("txn-002");
        assertThat(tx.amountCents()).isEqualTo(500L);
        assertThat(tx.currency()).isEqualTo("EUR");
        assertThat(tx.description()).isEqualTo("Monthly fee");
    }

    @Test
    @DisplayName("parseLine handles description with commas (limit=6 split)")
    void parseLine_descriptionWithCommas() {
        var tx = parser.parseLine("txn-003,2024-01-17T08:00:00Z,2500,GBP,PURCHASE,Coffee, cake, and tea");

        assertThat(tx.description()).isEqualTo("Coffee, cake, and tea");
        assertThat(tx.amountCents()).isEqualTo(2500L);
    }

    @Test
    @DisplayName("parseLine throws IllegalArgumentException for blank input")
    void parseLine_throwsForBlankInput() {
        assertThatThrownBy(() -> parser.parseLine(null))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> parser.parseLine(""))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> parser.parseLine("   "))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("parseLine throws IllegalArgumentException for wrong number of fields")
    void parseLine_throwsForWrongFieldCount() {
        assertThatThrownBy(() -> parser.parseLine("txn-001,2024-01-15T10:30:00Z,15099,USD"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("parseLine throws IllegalArgumentException for non-numeric amount")
    void parseLine_throwsForBadAmount() {
        assertThatThrownBy(() -> parser.parseLine("txn-001,2024-01-15T10:30:00Z,abc,USD,PURCHASE,Coffee"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("formatAmount formats cents with 2 decimal places and currency suffix")
    void formatAmount_formatsCorrectly() {
        assertThat(parser.formatAmount(15099L, "USD")).isEqualTo("150.99 USD");
        assertThat(parser.formatAmount(100L,   "EUR")).isEqualTo("1.00 EUR");
        assertThat(parser.formatAmount(0L,     "CHF")).isEqualTo("0.00 CHF");
        assertThat(parser.formatAmount(1L,     "GBP")).isEqualTo("0.01 GBP");
    }

    @Test
    @DisplayName("sanitizeDescription trims, replaces control chars, truncates to 50")
    void sanitizeDescription_sanitizesInput() {
        assertThat(parser.sanitizeDescription("Coffee at Starbucks"))
            .isEqualTo("Coffee at Starbucks");

        assertThat(parser.sanitizeDescription("  \t Messy\nInput  "))
            .isEqualTo("Messy Input");

        // Truncation at 50 chars
        String long60 = "A".repeat(60);
        assertThat(parser.sanitizeDescription(long60)).hasSize(50);

        // Null and blank → empty
        assertThat(parser.sanitizeDescription(null)).isEqualTo("");
        assertThat(parser.sanitizeDescription("   ")).isEqualTo("");
    }

    @Test
    @DisplayName("extractCurrencies returns sorted unique set of currencies")
    void extractCurrencies_returnsSortedUniqueCurrencies() {
        List<String> lines = List.of(
            "txn-1,2024-01-15T10:30:00Z,100,USD,PURCHASE,Coffee",
            "txn-2,2024-01-15T10:31:00Z,200,EUR,DEP,Deposit",
            "txn-3,2024-01-15T10:32:00Z,300,USD,PURCHASE,Lunch",
            "txn-4,2024-01-15T10:33:00Z,50,CHF,FEE,Service fee",
            "INVALID LINE"  // should be skipped without crashing
        );

        Set<String> currencies = parser.extractCurrencies(lines);
        assertThat(currencies).containsExactly("CHF", "EUR", "USD"); // sorted
        assertThat(currencies).doesNotContain("GBP");
    }

    @Test
    @DisplayName("isValidIso8601 accepts Z-terminated timestamps with T separator")
    void isValidIso8601_validatesTimestamps() {
        assertThat(parser.isValidIso8601("2024-01-15T10:30:00Z")).isTrue();
        assertThat(parser.isValidIso8601("2024-01-15T10:30:00.000Z")).isTrue();
        assertThat(parser.isValidIso8601("2024-01-15T10:30:00.123456Z")).isTrue();

        assertThat(parser.isValidIso8601("2024-01-15")).isFalse();       // no time
        assertThat(parser.isValidIso8601("2024-01-15 10:30:00")).isFalse(); // space not T
        assertThat(parser.isValidIso8601("")).isFalse();
        assertThat(parser.isValidIso8601(null)).isFalse();
        assertThat(parser.isValidIso8601("short")).isFalse();
    }
}
