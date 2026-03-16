package com.example.fintech.day1.enums;

/** Solution for Exercise 05 — Currency enum */
public enum Currency {
    USD("$",  2,  "US Dollar"),
    EUR("€",  2,  "Euro"),
    GBP("£",  2,  "British Pound"),
    CHF("Fr", 2,  "Swiss Franc"),
    JPY("¥",  0,  "Japanese Yen"),
    BTC("₿",  8,  "Bitcoin"),
    ETH("Ξ",  18, "Ethereum");

    private final String symbol;
    private final int minorUnits;
    private final String displayName;

    Currency(String symbol, int minorUnits, String displayName) {
        this.symbol = symbol;
        this.minorUnits = minorUnits;
        this.displayName = displayName;
    }

    public String getSymbol()      { return symbol; }
    public int getMinorUnits()     { return minorUnits; }
    public String getDisplayName() { return displayName; }

    public String formatAmount(long minorUnitAmount) {
        double value = minorUnitAmount / Math.pow(10, minorUnits);
        String fmt = "%s%." + minorUnits + "f";
        return fmt.formatted(symbol, value);
    }

    public static Currency fromCode(String code) {
        if (code == null) throw new IllegalArgumentException("code must not be null");
        for (Currency c : values()) {
            if (c.name().equalsIgnoreCase(code)) return c;
        }
        throw new IllegalArgumentException("Unknown currency: " + code);
    }

    public boolean isCrypto() {
        return switch (this) {
            case BTC, ETH -> true;
            default -> false;
        };
    }
}
