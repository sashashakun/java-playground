package com.example.fintech.day7.decorator;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class AuditingPaymentServiceTest {

    @Test
    void decoratorDelegatesToUnderlyingService() {
        PaymentService simple = new SimplePaymentService();
        PaymentService auditing = new AuditingPaymentService(simple);

        String result = auditing.processPayment("merchant-001", 100.00, "USD");

        assertNotNull(result);
        assertTrue(result.startsWith("Payment processed: "));
    }

    @Test
    void decoratorProducesAuditOutput() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            PaymentService service = new AuditingPaymentService(new SimplePaymentService());
            service.processPayment("merchant-002", 250.00, "EUR");
        } finally {
            System.setOut(originalOut);
        }

        String output = outContent.toString();
        assertTrue(output.contains("[AUDIT] Processing payment:"));
        assertTrue(output.contains("merchant=merchant-002"));
        assertTrue(output.contains("amount=250.0"));
        assertTrue(output.contains("EUR"));
        assertTrue(output.contains("[AUDIT] Payment result:"));
    }

    @Test
    void decoratorCanStackMultipleLayers() {
        // Double-auditing decorator to verify stacking works
        PaymentService inner = new SimplePaymentService();
        PaymentService outerAudit = new AuditingPaymentService(inner);
        PaymentService doubleAudit = new AuditingPaymentService(outerAudit);

        String result = doubleAudit.processPayment("merchant-003", 75.00, "GBP");
        assertNotNull(result);
        assertTrue(result.startsWith("Payment processed: "));
    }

    @Test
    void decoratorWithStubDelegateReturnsStubResult() {
        PaymentService stub = (merchantId, amount, currency) -> "STUB-RESULT";
        PaymentService auditing = new AuditingPaymentService(stub);

        String result = auditing.processPayment("m1", 10.00, "USD");
        assertEquals("STUB-RESULT", result);
    }
}
