package com.example.fintech.day1.exceptions;

/**
 * Exercise 07 — Exception Handling (Custom Exception 4 of 4)
 *
 * A CHECKED exception thrown when a CSV line cannot be parsed.
 *
 * This is a checked exception (extends Exception, not RuntimeException).
 * Callers MUST either catch it or declare 'throws TransactionParseException'.
 *
 * Use checked exceptions when:
 *   - The failure is expected and recoverable (malformed user input, bad file)
 *   - You want to FORCE callers to handle the error
 *
 * TODO: Implement this exception class.
 *   - Extend Exception (checked!)
 *   - Store the raw line that failed to parse: private final String rawLine
 *   - Store the line number (1-indexed): private final int lineNumber
 *   - Constructor: (int lineNumber, String rawLine, String reason)
 *     Message: "Parse error at line %d: %s — input was: %s"
 *       .formatted(lineNumber, reason, rawLine)
 *   - Two-arg constructor with cause: (int lineNumber, String rawLine, Throwable cause)
 *     Message: "Parse error at line %d — input was: %s"
 *       .formatted(lineNumber, rawLine)
 *     Call: super(message, cause)
 *   - Provide getters: getRawLine(), getLineNumber()
 */
public class TransactionParseException extends Exception {

    // TODO: add private final String rawLine;
    // TODO: add private final int lineNumber;

    // TODO: constructor(int lineNumber, String rawLine, String reason)

    // TODO: constructor(int lineNumber, String rawLine, Throwable cause)

    // TODO: getRawLine()

    // TODO: getLineNumber()
}
