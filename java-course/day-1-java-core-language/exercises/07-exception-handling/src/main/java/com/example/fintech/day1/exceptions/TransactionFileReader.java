package com.example.fintech.day1.exceptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 07 — Exception Handling (Part 6)
 *
 * Reads and parses transaction records from a CSV string (simulating file I/O).
 * Demonstrates try-with-resources and checked exception handling.
 *
 * In production this would read from a real file, but we use a String
 * so tests don't need actual files on disk.
 */
public class TransactionFileReader {

    /**
     * A successfully parsed transaction row.
     */
    public record TransactionRow(
        String id,
        long amountCents,
        String currency,
        String type
    ) {}

    /**
     * TODO 1 — Parse all valid transaction rows from a CSV string.
     *
     * CSV format (header on first line, skip it):
     *   id,amount_cents,currency,type
     *   txn-001,1599,USD,PUR
     *   txn-002,500,EUR,DEP
     *
     * Steps:
     *   1. Use try-with-resources to open a BufferedReader on the CSV string:
     *      try (BufferedReader reader = new BufferedReader(new StringReader(csvContent))) { ... }
     *
     *   2. Read and discard the header line: reader.readLine()
     *
     *   3. For each subsequent line:
     *      a. Skip null lines (end of stream)
     *      b. Skip blank lines
     *      c. Try to parse it with parseLine(lineNumber, line)
     *      d. If TransactionParseException is thrown, add the exception to parseErrors list
     *         and continue (don't abort the whole file for one bad line)
     *      e. On IOException: wrap and rethrow as RuntimeException("I/O error reading CSV", e)
     *
     *   4. Return the result (a record) with:
     *      - rows: all successfully parsed rows
     *      - parseErrors: all TransactionParseException instances collected
     *
     * @param csvContent  the full CSV content as a String
     * @return ParseResult with successful rows and any parse errors
     */
    public ParseResult readAll(String csvContent) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Result of reading a CSV file — both successes and failures.
     * Using a record instead of throwing on partial failure lets callers
     * decide what to do with bad rows (log and continue vs abort).
     */
    public record ParseResult(
        List<TransactionRow> rows,
        List<TransactionParseException> parseErrors
    ) {
        public boolean hasErrors() { return !parseErrors.isEmpty(); }
        public int successCount()  { return rows.size(); }
        public int errorCount()    { return parseErrors.size(); }
    }

    /**
     * TODO 2 — Parse a single CSV line into a TransactionRow.
     *
     * CSV line format: id,amount_cents,currency,type
     * Example:         txn-001,1599,USD,PUR
     *
     * Validation:
     *   - Must have exactly 4 comma-separated fields;
     *     if not: throw new TransactionParseException(lineNumber, line, "expected 4 fields")
     *   - amount_cents must parse as a long using Long.parseLong();
     *     if NumberFormatException is thrown:
     *       throw new TransactionParseException(lineNumber, line, nfe)
     *        (use the constructor that takes a Throwable cause)
     *
     * @param lineNumber  1-indexed line number (for error reporting)
     * @param line        the raw CSV line
     * @throws TransactionParseException if the line cannot be parsed
     */
    TransactionRow parseLine(int lineNumber, String line) throws TransactionParseException {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
