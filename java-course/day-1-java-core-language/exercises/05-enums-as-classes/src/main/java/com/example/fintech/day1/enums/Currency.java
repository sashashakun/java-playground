package com.example.fintech.day1.enums;

/**
 * Exercise 05 — Enums as Classes (Part 2 of 3)
 *
 * Currency enum with symbol, decimal places (minorUnits), and display name.
 * Implement the 3 TODO methods.
 *
 * minorUnits = number of decimal places in the smallest unit:
 *   USD → 2 (cents: $1.00 = 100 cents)
 *   JPY → 0 (no subunit: ¥100 = ¥100)
 *   BTC → 8 (satoshis: ₿1.00000000 = 100,000,000 satoshis)
 *   ETH → 18 (wei: 1 ETH = 10^18 wei)
 */
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

    /**
     * TODO 1 — Format a minor-unit amount as a display string.
     *
     * Formula: divide minorUnitAmount by 10^minorUnits, then format.
     *
     * Examples:
     *   USD.formatAmount(1599)          → "$15.99"
     *   EUR.formatAmount(100)           → "€1.00"
     *   JPY.formatAmount(1000)          → "¥1000"    (0 decimal places)
     *   BTC.formatAmount(100_000_000L)  → "₿1.00000000"  (8 decimal places)
     *
     * Java hints:
     *   Math.pow(10, minorUnits)               → 10^n as double
     *   double value = amount / Math.pow(10, minorUnits)
     *   "%s%." + minorUnits + "f"              → build format string dynamically
     *   ("%s%." + minorUnits + "f").formatted(symbol, value)
     */
    public String formatAmount(long minorUnitAmount) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 2 — Look up a Currency by its ISO code string.
     *
     * Examples:
     *   Currency.fromCode("USD") → Currency.USD
     *   Currency.fromCode("btc") → Currency.BTC   (case-insensitive)
     *   Currency.fromCode("XYZ") → throws IllegalArgumentException("Unknown currency: XYZ")
     *
     * Java hint: Enum.name() returns the enum constant name (e.g., "USD")
     *   for (Currency c : values()) {
     *       if (c.name().equalsIgnoreCase(code)) return c;
     *   }
     */
    public static Currency fromCode(String code) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 3 — Returns true if this currency is a cryptocurrency.
     *
     * Cryptocurrencies in this system: BTC and ETH.
     *
     * Use a switch expression (Java 14+):
     *   return switch (this) {
     *       case BTC, ETH -> true;
     *       default -> false;
     *   };
     */
    public boolean isCrypto() {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
