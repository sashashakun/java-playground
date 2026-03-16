package com.example.fintech.day1.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Enums as Classes")
class EnumsTest {

    @Nested
    @DisplayName("TransactionType")
    class TransactionTypeTests {

        @Test
        @DisplayName("fromCode looks up by 3-letter code")
        void fromCode_findsCorrectType() {
            assertThat(TransactionType.fromCode("DEP")).isEqualTo(TransactionType.DEPOSIT);
            assertThat(TransactionType.fromCode("WDR")).isEqualTo(TransactionType.WITHDRAWAL);
            assertThat(TransactionType.fromCode("PUR")).isEqualTo(TransactionType.PURCHASE);
            assertThat(TransactionType.fromCode("REF")).isEqualTo(TransactionType.REFUND);
            assertThat(TransactionType.fromCode("FEE")).isEqualTo(TransactionType.FEE);
            assertThat(TransactionType.fromCode("TRI")).isEqualTo(TransactionType.TRANSFER_IN);
            assertThat(TransactionType.fromCode("TRO")).isEqualTo(TransactionType.TRANSFER_OUT);
        }

        @Test
        @DisplayName("fromCode throws for unknown code")
        void fromCode_throwsForUnknownCode() {
            assertThatThrownBy(() -> TransactionType.fromCode("XXX"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("XXX");
        }

        @Test
        @DisplayName("creditTypes returns only credit transaction types")
        void creditTypes_returnsOnlyCredits() {
            var credits = TransactionType.creditTypes();

            assertThat(credits).containsExactlyInAnyOrder(
                TransactionType.DEPOSIT,
                TransactionType.TRANSFER_IN,
                TransactionType.REFUND
            );

            // All returned types must be credits
            assertThat(credits).allMatch(TransactionType::isCredit);

            // Debits must NOT be included
            assertThat(credits).doesNotContain(
                TransactionType.WITHDRAWAL,
                TransactionType.PURCHASE,
                TransactionType.TRANSFER_OUT,
                TransactionType.FEE
            );
        }

        @Test
        @DisplayName("toLabel returns formatted label with code and description")
        void toLabel_formatsCorrectly() {
            assertThat(TransactionType.DEPOSIT.toLabel())
                .isEqualTo("[DEP] Funds added to account");
            assertThat(TransactionType.PURCHASE.toLabel())
                .isEqualTo("[PUR] Merchant purchase");
            assertThat(TransactionType.FEE.toLabel())
                .isEqualTo("[FEE] Service or network fee");
        }

        @Test
        @DisplayName("built-in enum fields have correct values")
        void builtInFields_areCorrect() {
            assertThat(TransactionType.DEPOSIT.isCredit()).isTrue();
            assertThat(TransactionType.WITHDRAWAL.isCredit()).isFalse();
            assertThat(TransactionType.PURCHASE.getCode()).isEqualTo("PUR");
        }
    }

    @Nested
    @DisplayName("Currency")
    class CurrencyTests {

        @Test
        @DisplayName("formatAmount formats with correct symbol and decimal places")
        void formatAmount_formatsPerCurrency() {
            assertThat(Currency.USD.formatAmount(1599L)).isEqualTo("$15.99");
            assertThat(Currency.EUR.formatAmount(100L)).isEqualTo("€1.00");
            assertThat(Currency.JPY.formatAmount(1000L)).isEqualTo("¥1000"); // 0 decimals
            assertThat(Currency.BTC.formatAmount(100_000_000L)).isEqualTo("₿1.00000000"); // 8 decimals
        }

        @Test
        @DisplayName("fromCode is case-insensitive")
        void fromCode_caseInsensitive() {
            assertThat(Currency.fromCode("USD")).isEqualTo(Currency.USD);
            assertThat(Currency.fromCode("eur")).isEqualTo(Currency.EUR);
            assertThat(Currency.fromCode("BtC")).isEqualTo(Currency.BTC);
        }

        @Test
        @DisplayName("fromCode throws for unknown currency")
        void fromCode_throwsForUnknown() {
            assertThatThrownBy(() -> Currency.fromCode("XYZ"))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("isCrypto correctly identifies crypto currencies")
        void isCrypto_identifiesCrypto() {
            assertThat(Currency.BTC.isCrypto()).isTrue();
            assertThat(Currency.ETH.isCrypto()).isTrue();

            assertThat(Currency.USD.isCrypto()).isFalse();
            assertThat(Currency.EUR.isCrypto()).isFalse();
            assertThat(Currency.CHF.isCrypto()).isFalse();
        }
    }

    @Nested
    @DisplayName("FeeCalculator")
    class FeeCalculatorTests {

        private final FeeCalculator calc = new FeeCalculator();

        @Test
        @DisplayName("PURCHASE type incurs 1.5% fee")
        void calculateFee_purchaseFee() {
            // 1.5% of 10000 cents = 150 cents
            assertThat(calc.calculateFee(10000L, TransactionType.PURCHASE, Currency.USD))
                .isEqualTo(150L);
        }

        @Test
        @DisplayName("crypto currencies incur 0.5% fee with minimum 50 cents")
        void calculateFee_cryptoFee() {
            // 0.5% of 10000 = 50 cents — exactly at minimum
            assertThat(calc.calculateFee(10000L, TransactionType.DEPOSIT, Currency.BTC))
                .isEqualTo(50L);

            // 0.5% of 100000 = 500 cents — above minimum
            assertThat(calc.calculateFee(100000L, TransactionType.DEPOSIT, Currency.BTC))
                .isEqualTo(500L);

            // Small amount: 0.5% of 100 = 0.5 → rounds to 1 OR stays 0, but min is 50
            assertThat(calc.calculateFee(100L, TransactionType.DEPOSIT, Currency.ETH))
                .isEqualTo(50L); // minimum applies
        }

        @Test
        @DisplayName("TRANSFER_OUT incurs flat 25 cent fee")
        void calculateFee_transferOutFlatFee() {
            assertThat(calc.calculateFee(5000L, TransactionType.TRANSFER_OUT, Currency.EUR))
                .isEqualTo(25L);
            // Same fee regardless of amount
            assertThat(calc.calculateFee(1000000L, TransactionType.TRANSFER_OUT, Currency.USD))
                .isEqualTo(25L);
        }

        @Test
        @DisplayName("FEE type never incurs an additional fee")
        void calculateFee_feeTypeHasNoFee() {
            assertThat(calc.calculateFee(5000L, TransactionType.FEE, Currency.USD)).isEqualTo(0L);
            assertThat(calc.calculateFee(5000L, TransactionType.FEE, Currency.BTC)).isEqualTo(0L);
        }

        @Test
        @DisplayName("DEPOSIT and other types without fee rules return 0")
        void calculateFee_noFeeTypes() {
            assertThat(calc.calculateFee(5000L, TransactionType.DEPOSIT, Currency.USD)).isEqualTo(0L);
            assertThat(calc.calculateFee(5000L, TransactionType.TRANSFER_IN, Currency.EUR)).isEqualTo(0L);
            assertThat(calc.calculateFee(5000L, TransactionType.REFUND, Currency.CHF)).isEqualTo(0L);
        }

        @Test
        @DisplayName("hasFee returns true only when fee is non-zero")
        void hasFee_matchesCalculateFee() {
            assertThat(calc.hasFee(10000L, TransactionType.PURCHASE, Currency.USD)).isTrue();
            assertThat(calc.hasFee(10000L, TransactionType.DEPOSIT,  Currency.USD)).isFalse();
            assertThat(calc.hasFee(10000L, TransactionType.DEPOSIT,  Currency.BTC)).isTrue();
        }
    }
}
