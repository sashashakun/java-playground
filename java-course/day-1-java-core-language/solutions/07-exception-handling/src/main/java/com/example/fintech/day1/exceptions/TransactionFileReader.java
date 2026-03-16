package com.example.fintech.day1.exceptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/** Solution for Exercise 07 */
public class TransactionFileReader {

    public record TransactionRow(String id, long amountCents, String currency, String type) {}

    public record ParseResult(List<TransactionRow> rows, List<TransactionParseException> parseErrors) {
        public boolean hasErrors()  { return !parseErrors.isEmpty(); }
        public int successCount()   { return rows.size(); }
        public int errorCount()     { return parseErrors.size(); }
    }

    public ParseResult readAll(String csvContent) {
        List<TransactionRow> rows = new ArrayList<>();
        List<TransactionParseException> errors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new StringReader(csvContent))) {
            reader.readLine(); // skip header

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) continue;
                try {
                    rows.add(parseLine(lineNumber, line));
                } catch (TransactionParseException e) {
                    errors.add(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("I/O error reading CSV", e);
        }

        return new ParseResult(rows, errors);
    }

    TransactionRow parseLine(int lineNumber, String line) throws TransactionParseException {
        String[] parts = line.split(",");
        if (parts.length != 4)
            throw new TransactionParseException(lineNumber, line, "expected 4 fields");

        long amountCents;
        try {
            amountCents = Long.parseLong(parts[1].strip());
        } catch (NumberFormatException e) {
            throw new TransactionParseException(lineNumber, line, e);
        }

        return new TransactionRow(parts[0].strip(), amountCents, parts[2].strip(), parts[3].strip());
    }
}
