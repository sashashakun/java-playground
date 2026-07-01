package com.example.fintech.day7.openapi;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Request/response DTOs. {@code @Schema} annotations feed the generated OpenAPI
 * document — descriptions and examples show up in Swagger UI and in {@code /v3/api-docs}.
 */
public final class PaymentDtos {

    private PaymentDtos() {}

    public record CreatePaymentRequest(
            @Schema(description = "Payment amount in major currency units", example = "99.95")
            BigDecimal amount,

            @Schema(description = "ISO 4217 currency code", example = "USD")
            String currency,

            @Schema(description = "Identifier of the merchant receiving the payment", example = "merch-42")
            String merchantId
    ) {}

    public record PaymentResponse(
            @Schema(description = "Unique payment identifier", example = "pay-7f3a91c2")
            String id,

            @Schema(description = "Payment amount in major currency units", example = "99.95")
            BigDecimal amount,

            @Schema(description = "ISO 4217 currency code", example = "USD")
            String currency,

            @Schema(description = "Identifier of the merchant receiving the payment", example = "merch-42")
            String merchantId,

            @Schema(description = "Current payment status", example = "PENDING")
            String status
    ) {}
}
