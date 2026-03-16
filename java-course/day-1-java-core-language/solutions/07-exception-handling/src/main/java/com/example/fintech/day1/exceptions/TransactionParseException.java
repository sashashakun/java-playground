package com.example.fintech.day1.exceptions;

/** Solution for Exercise 07 */
public class TransactionParseException extends Exception {

    private final String rawLine;
    private final int lineNumber;

    public TransactionParseException(int lineNumber, String rawLine, String reason) {
        super("Parse error at line %d: %s — input was: %s".formatted(lineNumber, reason, rawLine));
        this.lineNumber = lineNumber;
        this.rawLine = rawLine;
    }

    public TransactionParseException(int lineNumber, String rawLine, Throwable cause) {
        super("Parse error at line %d — input was: %s".formatted(lineNumber, rawLine), cause);
        this.lineNumber = lineNumber;
        this.rawLine = rawLine;
    }

    public String getRawLine()  { return rawLine; }
    public int getLineNumber()  { return lineNumber; }
}
