package com.example.fintech.day3.composition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class ValidatorTest {

    private static final Set<String> SUPPORTED = Set.of("USD", "EUR", "GBP");

    private PaymentRequest validRequest(String id) {
        return new PaymentRequest(id, new BigDecimal("100"), "USD", "merchant-1");
    }

    // ─── AmountValidator ─────────────────────────────────────────────────────

    @Test
    void amountValidatorPassesPositiveAmount() {
        var v = new AmountValidator();
        assertThat(v.validate(validRequest("r1")).isValid()).isTrue();
    }

    @Test
    void amountValidatorRejectsZeroAmount() {
        var r = new PaymentRequest("r1", BigDecimal.ZERO, "USD", "m1");
        assertThat(new AmountValidator().validate(r).isValid()).isFalse();
        assertThat(new AmountValidator().validate(r).getReason()).contains("positive");
    }

    @Test
    void amountValidatorRejectsNegativeAmount() {
        var r = new PaymentRequest("r1", new BigDecimal("-1"), "USD", "m1");
        assertThat(new AmountValidator().validate(r).isValid()).isFalse();
    }

    @Test
    void amountValidatorRejectsNullAmount() {
        var r = new PaymentRequest("r1", null, "USD", "m1");
        assertThat(new AmountValidator().validate(r).isValid()).isFalse();
        assertThat(new AmountValidator().validate(r).getReason()).contains("required");
    }

    // ─── CurrencyValidator ───────────────────────────────────────────────────

    @Test
    void currencyValidatorPassesSupportedCurrency() {
        assertThat(new CurrencyValidator(SUPPORTED).validate(validRequest("r1")).isValid()).isTrue();
    }

    @Test
    void currencyValidatorRejectsUnsupportedCurrency() {
        var r = new PaymentRequest("r1", new BigDecimal("100"), "XYZ", "m1");
        var result = new CurrencyValidator(SUPPORTED).validate(r);
        assertThat(result.isValid()).isFalse();
        assertThat(result.getReason()).contains("XYZ");
    }

    @Test
    void currencyValidatorRejectsBlankCurrency() {
        var r = new PaymentRequest("r1", new BigDecimal("100"), "", "m1");
        assertThat(new CurrencyValidator(SUPPORTED).validate(r).isValid()).isFalse();
    }

    // ─── DuplicateValidator ──────────────────────────────────────────────────

    @Test
    void duplicateValidatorPassesFirstRequest() {
        assertThat(new DuplicateValidator().validate(validRequest("r1")).isValid()).isTrue();
    }

    @Test
    void duplicateValidatorRejectsDuplicateId() {
        var v = new DuplicateValidator();
        v.validate(validRequest("r1"));
        var result = v.validate(validRequest("r1"));
        assertThat(result.isValid()).isFalse();
        assertThat(result.getReason()).contains("r1");
    }

    @Test
    void duplicateValidatorAllowsDifferentIds() {
        var v = new DuplicateValidator();
        v.validate(validRequest("r1"));
        assertThat(v.validate(validRequest("r2")).isValid()).isTrue();
    }

    // ─── Composite chain (and combinator) ────────────────────────────────────

    @Test
    void chainPassesWhenAllValid() {
        Validator<PaymentRequest> chain = new AmountValidator()
            .and(new CurrencyValidator(SUPPORTED))
            .and(new DuplicateValidator());
        assertThat(chain.validate(validRequest("r1")).isValid()).isTrue();
    }

    @Test
    void chainShortCircuitsOnFirstFailure() {
        // amount is invalid — currency and duplicate validators should NOT run
        var r = new PaymentRequest("r1", BigDecimal.ZERO, "USD", "m1");
        Validator<PaymentRequest> chain = new AmountValidator()
            .and(new CurrencyValidator(SUPPORTED))
            .and(new DuplicateValidator());
        var result = chain.validate(r);
        assertThat(result.isValid()).isFalse();
        assertThat(result.getReason()).contains("positive");
    }

    @Test
    void chainReturnsSecondValidatorFailure() {
        var r = new PaymentRequest("r1", new BigDecimal("100"), "ZZZ", "m1");
        Validator<PaymentRequest> chain = new AmountValidator()
            .and(new CurrencyValidator(SUPPORTED));
        var result = chain.validate(r);
        assertThat(result.isValid()).isFalse();
        assertThat(result.getReason()).contains("ZZZ");
    }
}
