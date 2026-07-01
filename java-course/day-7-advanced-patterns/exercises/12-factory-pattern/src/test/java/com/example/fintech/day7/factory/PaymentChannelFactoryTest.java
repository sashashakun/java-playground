package com.example.fintech.day7.factory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentChannelFactoryTest {

    // ---- CARD channel tests ----

    @Test
    void cardChannelValidatorAcceptsExactly16Digits() {
        PaymentChannel channel = PaymentChannelFactory.create(ChannelType.CARD);
        assertTrue(channel.validator().validate("4111111111111111"));
        assertTrue(channel.validator().validate("5500005555555559"));
    }

    @Test
    void cardChannelValidatorRejects15Digits() {
        PaymentChannel channel = PaymentChannelFactory.create(ChannelType.CARD);
        assertFalse(channel.validator().validate("411111111111111"));
    }

    @Test
    void cardChannelValidatorRejects17Digits() {
        PaymentChannel channel = PaymentChannelFactory.create(ChannelType.CARD);
        assertFalse(channel.validator().validate("41111111111111111"));
    }

    @Test
    void cardChannelValidatorRejectsNonDigits() {
        PaymentChannel channel = PaymentChannelFactory.create(ChannelType.CARD);
        assertFalse(channel.validator().validate("411111111111111X"));
    }

    @Test
    void cardChannelValidatorRejectsNull() {
        PaymentChannel channel = PaymentChannelFactory.create(ChannelType.CARD);
        assertFalse(channel.validator().validate(null));
    }

    @Test
    void cardChannelProcessorReturnsLastFourDigits() {
        PaymentChannel channel = PaymentChannelFactory.create(ChannelType.CARD);
        String result = channel.processor().process("4111111111111111");
        assertTrue(result.contains("1111"));
        assertTrue(result.startsWith("Card payment processed:"));
    }

    // ---- BANK_TRANSFER channel tests ----

    @Test
    void bankChannelValidatorAcceptsValidIbanFormat() {
        PaymentChannel channel = PaymentChannelFactory.create(ChannelType.BANK_TRANSFER);
        assertTrue(channel.validator().validate("DE89370400440532013000"));
        assertTrue(channel.validator().validate("GB29NWBK60161331926819"));
    }

    @Test
    void bankChannelValidatorRejectsLowercaseCountryCode() {
        PaymentChannel channel = PaymentChannelFactory.create(ChannelType.BANK_TRANSFER);
        assertFalse(channel.validator().validate("de89370400440532013000"));
    }

    @Test
    void bankChannelValidatorRejectsMissingDigits() {
        PaymentChannel channel = PaymentChannelFactory.create(ChannelType.BANK_TRANSFER);
        assertFalse(channel.validator().validate("DE"));
    }

    @Test
    void bankChannelValidatorRejectsOnlyDigits() {
        PaymentChannel channel = PaymentChannelFactory.create(ChannelType.BANK_TRANSFER);
        assertFalse(channel.validator().validate("1234567890"));
    }

    @Test
    void bankChannelValidatorRejectsNull() {
        PaymentChannel channel = PaymentChannelFactory.create(ChannelType.BANK_TRANSFER);
        assertFalse(channel.validator().validate(null));
    }

    @Test
    void bankChannelProcessorReturnsExpectedFormat() {
        PaymentChannel channel = PaymentChannelFactory.create(ChannelType.BANK_TRANSFER);
        String result = channel.processor().process("DE89370400440532013000");
        assertEquals("Bank transfer processed: DE89370400440532013000", result);
    }

    @Test
    void factoryReturnsDifferentChannelTypesForDifferentEnumValues() {
        PaymentChannel card = PaymentChannelFactory.create(ChannelType.CARD);
        PaymentChannel bank = PaymentChannelFactory.create(ChannelType.BANK_TRANSFER);

        // Card rejects an IBAN-format payload
        assertFalse(card.validator().validate("DE89370400440532013000"));
        // Bank rejects a card-number payload
        assertFalse(bank.validator().validate("4111111111111111"));
    }
}
