package com.example.fintech.day1.enums;

import java.util.Arrays;
import java.util.List;

/** Solution for Exercise 05 — TransactionType enum */
public enum TransactionType {
    DEPOSIT("DEP", true,  "Funds added to account"),
    WITHDRAWAL("WDR", false, "Funds removed from account"),
    TRANSFER_IN("TRI", true,  "Incoming transfer from another account"),
    TRANSFER_OUT("TRO", false, "Outgoing transfer to another account"),
    PURCHASE("PUR", false, "Merchant purchase"),
    REFUND("REF", true,  "Merchant refund"),
    FEE("FEE", false, "Service or network fee");

    private final String code;
    private final boolean credit;
    private final String description;

    TransactionType(String code, boolean credit, String description) {
        this.code = code;
        this.credit = credit;
        this.description = description;
    }

    public String getCode()        { return code; }
    public boolean isCredit()      { return credit; }
    public String getDescription() { return description; }

    public static TransactionType fromCode(String code) {
        if (code == null) throw new IllegalArgumentException("code must not be null");
        for (TransactionType type : values()) {
            if (type.code.equals(code)) return type;
        }
        throw new IllegalArgumentException("Unknown transaction code: " + code);
    }

    public static List<TransactionType> creditTypes() {
        return Arrays.stream(values())
            .filter(TransactionType::isCredit)
            .toList();
    }

    public String toLabel() {
        return "[%s] %s".formatted(code, description);
    }
}
