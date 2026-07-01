package com.example.fintech.day7.factory;

public class PaymentChannelFactory {

    public static PaymentChannel create(ChannelType type) {
        return switch (type) {
            case CARD -> new CardChannel();
            case BANK_TRANSFER -> new BankTransferChannel();
        };
    }

    // --- CARD channel ---
    private static class CardChannel implements PaymentChannel {
        @Override
        public PaymentValidator validator() {
            // Card number must be exactly 16 digits
            return payload -> payload != null && payload.matches("\\d{16}");
        }

        @Override
        public PaymentProcessor processor() {
            return payload -> {
                String lastFour = payload.substring(payload.length() - 4);
                return "Card payment processed: " + lastFour;
            };
        }
    }

    // --- BANK_TRANSFER channel ---
    private static class BankTransferChannel implements PaymentChannel {
        @Override
        public PaymentValidator validator() {
            // IBAN-style: starts with exactly 2 uppercase letters, followed by digits
            return payload -> payload != null && payload.matches("[A-Z]{2}\\d+");
        }

        @Override
        public PaymentProcessor processor() {
            return payload -> "Bank transfer processed: " + payload;
        }
    }
}
