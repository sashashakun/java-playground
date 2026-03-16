package com.example.fintech.day1.enums;

import java.util.List;

/**
 * Exercise 05 — Enums as Classes (Part 1 of 3)
 *
 * Java enums are full classes. This enum models all transaction types
 * in our fintech system, each with a code, a credit flag, and a description.
 *
 * The skeleton is provided — implement the TODO methods.
 */
public enum TransactionType {

    DEPOSIT("DEP", true,  "Funds added to account"),
    WITHDRAWAL("WDR", false, "Funds removed from account"),
    TRANSFER_IN("TRI", true,  "Incoming transfer from another account"),
    TRANSFER_OUT("TRO", false, "Outgoing transfer to another account"),
    PURCHASE("PUR", false, "Merchant purchase"),
    REFUND("REF", true,  "Merchant refund"),
    FEE("FEE", false, "Service or network fee");

    // Enum fields — stored per-constant, like instance fields on a class
    private final String code;
    private final boolean credit;
    private final String description;

    // Enum constructor — called once per constant at class-load time
    TransactionType(String code, boolean credit, String description) {
        this.code = code;
        this.credit = credit;
        this.description = description;
    }

    // Provided getters:
    public String getCode()        { return code; }
    public boolean isCredit()      { return credit; }
    public String getDescription() { return description; }

    /**
     * TODO 1 — Look up a TransactionType by its 3-letter code.
     *
     * Examples:
     *   fromCode("DEP") → DEPOSIT
     *   fromCode("PUR") → PURCHASE
     *   fromCode("XXX") → throws IllegalArgumentException("Unknown transaction code: XXX")
     *   fromCode(null)  → throws IllegalArgumentException
     *
     * Java hint: iterate over values() — the built-in array of all enum constants:
     *   for (TransactionType type : values()) {
     *       if (type.code.equals(code)) return type;
     *   }
     */
    public static TransactionType fromCode(String code) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 2 — Return all transaction types that represent a credit (money coming in).
     *
     * A credit type has isCredit() == true.
     * Return them as an unmodifiable List in the order they appear in the enum.
     *
     * Expected: [DEPOSIT, TRANSFER_IN, REFUND]
     *
     * Java hints:
     *   import java.util.Arrays;
     *   Arrays.stream(values())
     *       .filter(TransactionType::isCredit)
     *       .toList()
     */
    public static List<TransactionType> creditTypes() {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 3 — Return a human-readable label for display in reports.
     *
     * Format: "[code] description"
     *
     * Examples:
     *   DEPOSIT.toLabel()    → "[DEP] Funds added to account"
     *   PURCHASE.toLabel()   → "[PUR] Merchant purchase"
     *
     * Java hint: "[%s] %s".formatted(code, description)
     */
    public String toLabel() {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
