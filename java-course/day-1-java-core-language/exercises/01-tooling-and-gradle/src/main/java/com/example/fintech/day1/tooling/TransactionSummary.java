package com.example.fintech.day1.tooling;

/**
 * Exercise 01 — Tooling & Gradle
 *
 * Your first Java class. Implement the 5 TODO methods below.
 * Run tests with: ./gradlew test
 *
 * TypeScript reminder:
 *   function countPositive(amounts: number[]): number { ... }
 * becomes:
 *   public int countPositive(int[] amounts) { ... }
 *
 * Key differences:
 * - Return type comes BEFORE the method name (not after with ':')
 * - No 'function' keyword
 * - Array type written as 'int[]', not 'number[]'
 * - 'public' access modifier is required for test visibility
 */
public class TransactionSummary {

    /**
     * TODO 1 — Count how many amounts are strictly positive (> 0).
     *
     * TypeScript equivalent:
     *   const countPositive = (amounts: number[]): number =>
     *     amounts.filter(n => n > 0).length;
     *
     * Java hint: use a for-each loop:
     *   for (int amount : amounts) { ... }
     *
     * Edge case: empty array → return 0
     */
    public int countPositive(int[] amounts) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 2 — Sum all amounts (as a long to avoid int overflow).
     *
     * Why long? int max is ~2.1 billion. A daily sum of transactions
     * in a busy wallet can easily exceed that. long max is ~9.2 quintillion.
     *
     * Edge case: empty array → return 0
     *
     * Example: sumAmounts(new long[]{100, 200, 300}) → 600L
     */
    public long sumAmounts(long[] amounts) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 3 — Format an amount in minor units (cents) as a display string.
     *
     * Formula: divide by 100, format with 2 decimal places.
     *
     * Examples:
     *   formatAmount(1599, "USD") → "USD 15.99"
     *   formatAmount(100,  "EUR") → "EUR 1.00"
     *   formatAmount(0,    "CHF") → "CHF 0.00"
     *
     * Java hint: use String.format or .formatted():
     *   "%.2f".formatted(1599 / 100.0)  → "15.99"
     *   "%s %.2f".formatted("USD", 15.99) → "USD 15.99"
     */
    public String formatAmount(long amountCents, String currency) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 4 — Reverse a transaction description string.
     *
     * Examples:
     *   reverseDescription("Coffee")  → "eeffoC"
     *   reverseDescription("")        → ""
     *   reverseDescription(null)      → ""   (null-safe)
     *
     * Java hint: new StringBuilder(s).reverse().toString()
     */
    public String reverseDescription(String description) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 5 — Validate a currency code.
     *
     * A valid ISO 4217 currency code is exactly 3 uppercase ASCII letters.
     *
     * Examples:
     *   isValidCurrency("USD") → true
     *   isValidCurrency("BTC") → true
     *   isValidCurrency("usd") → false  (lowercase)
     *   isValidCurrency("US")  → false  (too short)
     *   isValidCurrency(null)  → false  (null)
     *   isValidCurrency("")    → false  (empty)
     *
     * Java hint: s.matches("[A-Z]{3}") uses a regex.
     * Or check s.length() == 3 and Character.isUpperCase(c) for each char.
     */
    public boolean isValidCurrency(String code) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
