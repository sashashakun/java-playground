package com.example.fintech.day1.tooling;

/** Solution for Exercise 01 — Tooling & Gradle */
public class TransactionSummary {

    public int countPositive(int[] amounts) {
        int count = 0;
        for (int amount : amounts) {
            if (amount > 0) count++;
        }
        return count;
    }

    public long sumAmounts(long[] amounts) {
        long sum = 0L;
        for (long amount : amounts) {
            sum += amount;
        }
        return sum;
    }

    public String formatAmount(long amountCents, String currency) {
        return "%s %.2f".formatted(currency, amountCents / 100.0);
    }

    public String reverseDescription(String description) {
        if (description == null) return "";
        return new StringBuilder(description).reverse().toString();
    }

    public boolean isValidCurrency(String code) {
        if (code == null) return false;
        return code.matches("[A-Z]{3}");
    }
}
