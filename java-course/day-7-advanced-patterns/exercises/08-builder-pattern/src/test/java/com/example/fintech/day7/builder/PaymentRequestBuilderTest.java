package com.example.fintech.day7.builder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRequestBuilderTest {

    @Test
    void validBuildSucceeds() {
        PaymentRequest request = new PaymentRequestBuilder()
                .amount(100.00)
                .currency("USD")
                .merchantId("merchant-001")
                .description("Test payment")
                .idempotencyKey("idem-key-1")
                .addMetadata("region", "US")
                .build();

        assertEquals(100.00, request.getAmount());
        assertEquals("USD", request.getCurrency());
        assertEquals("merchant-001", request.getMerchantId());
        assertEquals("Test payment", request.getDescription());
        assertEquals("idem-key-1", request.getIdempotencyKey());
        assertEquals("US", request.getMetadata().get("region"));
    }

    @Test
    void validBuildWithOnlyRequiredFieldsSucceeds() {
        PaymentRequest request = new PaymentRequestBuilder()
                .amount(50.00)
                .currency("EUR")
                .merchantId("merchant-002")
                .build();

        assertEquals(50.00, request.getAmount());
        assertEquals("EUR", request.getCurrency());
        assertEquals("merchant-002", request.getMerchantId());
        assertNull(request.getDescription());
        assertNull(request.getIdempotencyKey());
        assertTrue(request.getMetadata().isEmpty());
    }

    @Test
    void missingAmountThrowsIllegalStateException() {
        PaymentRequestBuilder builder = new PaymentRequestBuilder()
                .currency("USD")
                .merchantId("merchant-001");

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().contains("amount is required"));
    }

    @Test
    void missingCurrencyThrowsIllegalStateException() {
        PaymentRequestBuilder builder = new PaymentRequestBuilder()
                .amount(100.00)
                .merchantId("merchant-001");

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().contains("currency is required"));
    }

    @Test
    void missingMerchantIdThrowsIllegalStateException() {
        PaymentRequestBuilder builder = new PaymentRequestBuilder()
                .amount(100.00)
                .currency("USD");

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().contains("merchantId is required"));
    }

    @Test
    void zeroAmountThrowsIllegalStateException() {
        PaymentRequestBuilder builder = new PaymentRequestBuilder()
                .amount(0)
                .currency("USD")
                .merchantId("merchant-001");

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().contains("amount must be greater than 0"));
    }

    @Test
    void negativeAmountThrowsIllegalStateException() {
        PaymentRequestBuilder builder = new PaymentRequestBuilder()
                .amount(-10.00)
                .currency("USD")
                .merchantId("merchant-001");

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().contains("amount must be greater than 0"));
    }

    @Test
    void invalidCurrencyLengthThrowsIllegalStateException() {
        PaymentRequestBuilder builder = new PaymentRequestBuilder()
                .amount(100.00)
                .currency("US")
                .merchantId("merchant-001");

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().contains("currency must be exactly 3 characters"));
    }

    @Test
    void builderIsReusableForMultipleBuilds() {
        PaymentRequestBuilder builder = new PaymentRequestBuilder()
                .amount(200.00)
                .currency("GBP")
                .merchantId("merchant-003");

        PaymentRequest first = builder.build();
        PaymentRequest second = builder.build();

        assertEquals(first.getMerchantId(), second.getMerchantId());
        assertEquals(first.getCurrency(), second.getCurrency());
    }
}
