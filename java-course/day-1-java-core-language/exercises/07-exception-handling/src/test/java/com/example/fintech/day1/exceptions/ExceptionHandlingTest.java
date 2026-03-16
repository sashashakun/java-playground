package com.example.fintech.day1.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Exception Handling")
class ExceptionHandlingTest {

    @Nested
    @DisplayName("Custom Exceptions")
    class CustomExceptionTests {

        @Test
        @DisplayName("InsufficientFundsException carries available and requested amounts")
        void insufficientFundsException_carriesContext() {
            var ex = new InsufficientFundsException(1000L, 2500L);

            assertThat(ex.getAvailableCents()).isEqualTo(1000L);
            assertThat(ex.getRequestedCents()).isEqualTo(2500L);
            assertThat(ex.getMessage()).contains("1000").contains("2500");
            assertThat(ex).isInstanceOf(RuntimeException.class); // unchecked
        }

        @Test
        @DisplayName("TransactionValidationException carries field name and message")
        void transactionValidationException_carriesFieldName() {
            var ex = new TransactionValidationException("amount", "must be positive");

            assertThat(ex.getFieldName()).isEqualTo("amount");
            assertThat(ex.getMessage()).contains("amount").contains("must be positive");
            assertThat(ex).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("DuplicateTransactionException carries transaction ID")
        void duplicateTransactionException_carriesId() {
            var ex = new DuplicateTransactionException("txn-001");

            assertThat(ex.getTransactionId()).isEqualTo("txn-001");
            assertThat(ex.getMessage()).contains("txn-001");
            assertThat(ex).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("TransactionParseException is checked and carries line info")
        void transactionParseException_isChecked() {
            // Just constructing it verifies the class compiles and stores fields
            var ex = new TransactionParseException(5, "bad,line", "expected 4 fields");

            assertThat(ex.getLineNumber()).isEqualTo(5);
            assertThat(ex.getRawLine()).isEqualTo("bad,line");
            assertThat(ex.getMessage()).contains("5").contains("bad,line");
            assertThat(ex).isInstanceOf(Exception.class);          // checked
            assertThat(ex).isNotInstanceOf(RuntimeException.class); // NOT unchecked

            // Constructor with cause
            var cause = new NumberFormatException("not a number");
            var ex2 = new TransactionParseException(3, "x,y,z,notanumber", cause);
            assertThat(ex2.getCause()).isEqualTo(cause);
            assertThat(ex2.getLineNumber()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("TransactionValidator")
    class TransactionValidatorTests {

        private TransactionValidator validator;

        @BeforeEach
        void setUp() {
            validator = new TransactionValidator();
        }

        @Test
        @DisplayName("validateAmount accepts valid positive amounts")
        void validateAmount_acceptsValid() {
            assertThatNoException().isThrownBy(() -> validator.validateAmount(1L));
            assertThatNoException().isThrownBy(() -> validator.validateAmount(100_00L));
            assertThatNoException().isThrownBy(() -> validator.validateAmount(9_999_999_00L));
        }

        @Test
        @DisplayName("validateAmount rejects zero and negative amounts")
        void validateAmount_rejectsNonPositive() {
            assertThatThrownBy(() -> validator.validateAmount(0L))
                .isInstanceOf(TransactionValidationException.class)
                .extracting("fieldName").isEqualTo("amount");

            assertThatThrownBy(() -> validator.validateAmount(-100L))
                .isInstanceOf(TransactionValidationException.class);
        }

        @Test
        @DisplayName("validateAmount rejects amounts exceeding maximum")
        void validateAmount_rejectsOverMaximum() {
            assertThatThrownBy(() -> validator.validateAmount(10_000_001_00L))
                .isInstanceOf(TransactionValidationException.class)
                .extracting("fieldName").isEqualTo("amount");
        }

        @Test
        @DisplayName("validateCurrency accepts known currencies")
        void validateCurrency_acceptsKnown() {
            assertThatNoException().isThrownBy(() -> validator.validateCurrency("USD"));
            assertThatNoException().isThrownBy(() -> validator.validateCurrency("BTC"));
        }

        @Test
        @DisplayName("validateCurrency rejects blank and unknown currencies")
        void validateCurrency_rejectsUnknown() {
            assertThatThrownBy(() -> validator.validateCurrency(null))
                .isInstanceOf(TransactionValidationException.class);
            assertThatThrownBy(() -> validator.validateCurrency(""))
                .isInstanceOf(TransactionValidationException.class);
            assertThatThrownBy(() -> validator.validateCurrency("XYZ"))
                .isInstanceOf(TransactionValidationException.class);
        }

        @Test
        @DisplayName("validateSufficientFunds passes when balance covers amount")
        void validateSufficientFunds_passesWhenCovered() {
            assertThatNoException().isThrownBy(() -> validator.validateSufficientFunds(500L, 1000L));
            assertThatNoException().isThrownBy(() -> validator.validateSufficientFunds(1000L, 1000L));
        }

        @Test
        @DisplayName("validateSufficientFunds throws InsufficientFundsException when balance too low")
        void validateSufficientFunds_throwsWhenInsufficient() {
            assertThatThrownBy(() -> validator.validateSufficientFunds(1500L, 1000L))
                .isInstanceOf(InsufficientFundsException.class)
                .satisfies(e -> {
                    InsufficientFundsException ife = (InsufficientFundsException) e;
                    assertThat(ife.getAvailableCents()).isEqualTo(1000L);
                    assertThat(ife.getRequestedCents()).isEqualTo(1500L);
                });
        }

        @Test
        @DisplayName("validateNoDuplicate passes for new IDs")
        void validateNoDuplicate_passesForNewId() {
            Set<String> existing = Set.of("txn-001", "txn-002");
            assertThatNoException()
                .isThrownBy(() -> validator.validateNoDuplicate("txn-003", existing));
        }

        @Test
        @DisplayName("validateNoDuplicate throws DuplicateTransactionException for known IDs")
        void validateNoDuplicate_throwsForDuplicate() {
            Set<String> existing = Set.of("txn-001", "txn-002");
            assertThatThrownBy(() -> validator.validateNoDuplicate("txn-001", existing))
                .isInstanceOf(DuplicateTransactionException.class)
                .satisfies(e -> {
                    DuplicateTransactionException dte = (DuplicateTransactionException) e;
                    assertThat(dte.getTransactionId()).isEqualTo("txn-001");
                });
        }
    }

    @Nested
    @DisplayName("TransactionFileReader")
    class TransactionFileReaderTests {

        private TransactionFileReader reader;

        @BeforeEach
        void setUp() {
            reader = new TransactionFileReader();
        }

        @Test
        @DisplayName("readAll parses valid CSV content successfully")
        void readAll_parsesValidContent() {
            String csv = """
                id,amount_cents,currency,type
                txn-001,1599,USD,PUR
                txn-002,500,EUR,DEP
                txn-003,2500,GBP,WDR
                """;

            TransactionFileReader.ParseResult result = reader.readAll(csv);

            assertThat(result.successCount()).isEqualTo(3);
            assertThat(result.errorCount()).isEqualTo(0);
            assertThat(result.hasErrors()).isFalse();

            assertThat(result.rows()).extracting(TransactionFileReader.TransactionRow::id)
                .containsExactly("txn-001", "txn-002", "txn-003");
        }

        @Test
        @DisplayName("readAll collects parse errors without aborting")
        void readAll_collectsErrorsWithoutAborting() {
            String csv = """
                id,amount_cents,currency,type
                txn-001,1599,USD,PUR
                INVALID LINE
                txn-003,not_a_number,GBP,WDR
                txn-004,100,CHF,FEE
                """;

            TransactionFileReader.ParseResult result = reader.readAll(csv);

            assertThat(result.successCount()).isEqualTo(2); // txn-001 and txn-004
            assertThat(result.errorCount()).isEqualTo(2);   // invalid line and bad amount
            assertThat(result.hasErrors()).isTrue();
        }

        @Test
        @DisplayName("parseLine parses a valid CSV line into a TransactionRow")
        void parseLine_parsesValidLine() throws TransactionParseException {
            var row = reader.parseLine(1, "txn-001,1599,USD,PUR");

            assertThat(row.id()).isEqualTo("txn-001");
            assertThat(row.amountCents()).isEqualTo(1599L);
            assertThat(row.currency()).isEqualTo("USD");
            assertThat(row.type()).isEqualTo("PUR");
        }

        @Test
        @DisplayName("parseLine throws TransactionParseException for wrong field count")
        void parseLine_throwsForWrongFieldCount() {
            assertThatThrownBy(() -> reader.parseLine(2, "txn-001,1599,USD"))
                .isInstanceOf(TransactionParseException.class)
                .satisfies(e -> {
                    TransactionParseException tpe = (TransactionParseException) e;
                    assertThat(tpe.getLineNumber()).isEqualTo(2);
                    assertThat(tpe.getRawLine()).isEqualTo("txn-001,1599,USD");
                });
        }

        @Test
        @DisplayName("parseLine throws TransactionParseException for non-numeric amount")
        void parseLine_throwsForBadAmount() {
            assertThatThrownBy(() -> reader.parseLine(5, "txn-001,not_a_number,USD,PUR"))
                .isInstanceOf(TransactionParseException.class)
                .satisfies(e -> {
                    TransactionParseException tpe = (TransactionParseException) e;
                    assertThat(tpe.getLineNumber()).isEqualTo(5);
                    assertThat(tpe.getCause()).isInstanceOf(NumberFormatException.class);
                });
        }
    }
}
