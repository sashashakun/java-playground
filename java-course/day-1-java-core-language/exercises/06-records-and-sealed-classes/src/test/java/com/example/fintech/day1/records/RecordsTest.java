package com.example.fintech.day1.records;

import com.example.fintech.day1.records.PaymentEvent.PaymentFailed;
import com.example.fintech.day1.records.PaymentEvent.PaymentInitiated;
import com.example.fintech.day1.records.PaymentEvent.PaymentProcessing;
import com.example.fintech.day1.records.PaymentEvent.PaymentSettled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Records & Sealed Classes")
class RecordsTest {

    @Nested
    @DisplayName("Money record")
    class MoneyTests {

        @Test
        @DisplayName("constructor normalizes scale to 2 decimal places")
        void constructor_normalizesScale() {
            Money m = new Money(new BigDecimal("10.5"), "USD");
            assertThat(m.amount().scale()).isEqualTo(2);
            assertThat(m.amount()).isEqualByComparingTo(new BigDecimal("10.50"));
        }

        @Test
        @DisplayName("constructor rejects null amount")
        void constructor_rejectsNullAmount() {
            assertThatNullPointerException()
                .isThrownBy(() -> new Money(null, "USD"))
                .withMessageContaining("amount");
        }

        @Test
        @DisplayName("constructor rejects null or blank currency")
        void constructor_rejectsBlankCurrency() {
            assertThatThrownBy(() -> new Money(BigDecimal.TEN, null))
                .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> new Money(BigDecimal.TEN, ""))
                .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> new Money(BigDecimal.TEN, "  "))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("constructor rejects invalid currency code format")
        void constructor_rejectsInvalidCurrencyFormat() {
            assertThatThrownBy(() -> new Money(BigDecimal.TEN, "usd"))  // lowercase
                .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> new Money(BigDecimal.TEN, "US"))   // too short
                .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> new Money(BigDecimal.TEN, "USDC")) // too long
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("constructor accepts negative amounts (represent debits/overdrafts)")
        void constructor_acceptsNegativeAmount() {
            // Money can be negative — a negative balance represents an overdraft.
            // Use isPositive() to check the sign.
            Money debt = new Money(new BigDecimal("-5.00"), "USD");
            assertThat(debt.amount()).isEqualByComparingTo(new BigDecimal("-5.00"));
            assertThat(debt.isPositive()).isFalse();
        }

        @Test
        @DisplayName("add sums two same-currency amounts")
        void add_samesCurrency() {
            Money a = new Money(new BigDecimal("10.50"), "USD");
            Money b = new Money(new BigDecimal("5.75"), "USD");
            assertThat(a.add(b).amount()).isEqualByComparingTo(new BigDecimal("16.25"));
            assertThat(a.add(b).currency()).isEqualTo("USD");
        }

        @Test
        @DisplayName("add throws for different currencies")
        void add_throwsForDifferentCurrencies() {
            Money usd = new Money(new BigDecimal("10.00"), "USD");
            Money eur = new Money(new BigDecimal("10.00"), "EUR");
            assertThatThrownBy(() -> usd.add(eur))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("subtract produces possibly-negative result")
        void subtract_canGoNegative() {
            Money a = new Money(new BigDecimal("10.00"), "USD");
            Money b = new Money(new BigDecimal("15.00"), "USD");
            // We can't construct a negative Money, but subtract can produce negative BigDecimal
            // Adjust: the implementation should allow the result to bypass the negative check
            // (subtract creates the result with raw BigDecimal, not through constructor validation)
            // OR if your constructor allows it: this test verifies the result
            Money result = a.subtract(b);
            assertThat(result.amount()).isEqualByComparingTo(new BigDecimal("-5.00"));
        }

        @Test
        @DisplayName("isPositive returns true only for amount > 0")
        void isPositive_trueForPositiveAmounts() {
            assertThat(new Money(new BigDecimal("0.01"), "USD").isPositive()).isTrue();
            assertThat(new Money(new BigDecimal("100.00"), "USD").isPositive()).isTrue();
            assertThat(new Money(BigDecimal.ZERO, "USD").isPositive()).isFalse();
        }

        @Test
        @DisplayName("formatted returns 'CURRENCY AMOUNT' string")
        void formatted_correctOutput() {
            assertThat(new Money(new BigDecimal("15.99"), "USD").formatted()).isEqualTo("USD 15.99");
            assertThat(new Money(BigDecimal.ZERO, "EUR").formatted()).isEqualTo("EUR 0.00");
        }

        @Test
        @DisplayName("records auto-generate equals and hashCode based on fields")
        void recordEquality_byValue() {
            Money m1 = new Money(new BigDecimal("10.00"), "USD");
            Money m2 = new Money(new BigDecimal("10.00"), "USD");
            Money m3 = new Money(new BigDecimal("10.00"), "EUR");

            assertThat(m1).isEqualTo(m2);
            assertThat(m1).isNotEqualTo(m3);
            assertThat(m1.hashCode()).isEqualTo(m2.hashCode());
        }
    }

    @Nested
    @DisplayName("PaymentEventProcessor")
    class PaymentEventProcessorTests {

        private final PaymentEventProcessor processor = new PaymentEventProcessor();
        private final Money fiveHundred = new Money(new BigDecimal("500.00"), "USD");

        @Test
        @DisplayName("describe produces human-readable description for all event types")
        void describe_allEventTypes() {
            Instant now = Instant.parse("2024-01-15T10:30:00Z");

            assertThat(processor.describe(new PaymentInitiated("pay-1", fiveHundred, "user-42")))
                .contains("pay-1").contains("500.00").contains("user-42");

            assertThat(processor.describe(new PaymentProcessing("pay-1", "GW-XYZ")))
                .contains("pay-1").contains("GW-XYZ");

            assertThat(processor.describe(new PaymentSettled("pay-1", fiveHundred, now)))
                .contains("pay-1").contains("500.00").contains("2024-01-15T10:30:00Z");

            assertThat(processor.describe(new PaymentFailed("pay-1", "Card declined", 4001)))
                .contains("pay-1").contains("Card declined").contains("4001");
        }

        @Test
        @DisplayName("extractPaymentId returns ID from any event type")
        void extractPaymentId_allTypes() {
            assertThat(processor.extractPaymentId(
                new PaymentInitiated("pay-42", fiveHundred, "user-1"))).isEqualTo("pay-42");
            assertThat(processor.extractPaymentId(
                new PaymentProcessing("pay-42", "GW"))).isEqualTo("pay-42");
            assertThat(processor.extractPaymentId(
                new PaymentSettled("pay-42", fiveHundred, Instant.now()))).isEqualTo("pay-42");
            assertThat(processor.extractPaymentId(
                new PaymentFailed("pay-42", "Error", 500))).isEqualTo("pay-42");
        }

        @Test
        @DisplayName("isTerminal returns true only for SETTLED and FAILED")
        void isTerminal_terminalStates() {
            Instant now = Instant.now();

            assertThat(processor.isTerminal(new PaymentSettled("p", fiveHundred, now))).isTrue();
            assertThat(processor.isTerminal(new PaymentFailed("p", "err", 500))).isTrue();
            assertThat(processor.isTerminal(new PaymentInitiated("p", fiveHundred, "u"))).isFalse();
            assertThat(processor.isTerminal(new PaymentProcessing("p", "gw"))).isFalse();
        }

        @Test
        @DisplayName("getAmount returns present for INITIATED and SETTLED, empty for others")
        void getAmount_presenceByType() {
            Instant now = Instant.now();

            assertThat(processor.getAmount(
                new PaymentInitiated("p", fiveHundred, "u"))).contains(fiveHundred);
            assertThat(processor.getAmount(
                new PaymentSettled("p", fiveHundred, now))).contains(fiveHundred);
            assertThat(processor.getAmount(
                new PaymentProcessing("p", "gw"))).isEmpty();
            assertThat(processor.getAmount(
                new PaymentFailed("p", "err", 500))).isEmpty();
        }
    }
}
