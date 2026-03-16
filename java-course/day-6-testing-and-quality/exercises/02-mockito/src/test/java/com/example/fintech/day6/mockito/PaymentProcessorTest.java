package com.example.fintech.day6.mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Exercise 02 — Mockito
 *
 * The PaymentProcessor is provided. Write tests to verify its behaviour using
 * Mockito for all three dependencies (FraudDetector, AuditLog, NotificationPort).
 *
 * @ExtendWith(MockitoExtension.class) — activates Mockito annotations
 * @Mock — creates a mock object (all methods return default values by default)
 * @InjectMocks — creates the real class and injects @Mock fields into it
 *
 * TypeScript analogy:
 *   @Mock      ≈  vi.fn() / jest.fn()
 *   @InjectMocks ≈  new Service(mockFraud, mockAudit, mockNotify)
 *   when(...).thenReturn(...) ≈  mockFn.mockReturnValue(...)
 *   verify(mock).method(...)  ≈  expect(mockFn).toHaveBeenCalledWith(...)
 *   ArgumentCaptor           ≈  vi.fn() + calls[0].arguments inspection
 */
@ExtendWith(MockitoExtension.class)
class PaymentProcessorTest {

    @Mock
    private FraudDetector fraudDetector;

    @Mock
    private AuditLog auditLog;

    @Mock
    private NotificationPort notification;

    @InjectMocks
    private PaymentProcessor processor;

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 1 — Happy path
    //
    // When fraudDetector.isSuspicious returns false, process() should:
    //   a) return a PaymentResult with status "COMPLETED"
    //   b) have a non-null transactionId
    //   c) have the correct userId, amount, currency
    //
    // Stub: when(fraudDetector.isSuspicious(...)).thenReturn(false)
    // Assert: assertThat(result.status()).isEqualTo("COMPLETED");
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO1_happyPath_returnsCompletedResult() {
        throw new UnsupportedOperationException("TODO 1: not implemented");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 2 — Fraud detected
    //
    // When fraudDetector.isSuspicious returns true, process() should:
    //   a) throw PaymentProcessor.FraudException
    //   b) NOT call notification.notify at all (verify(notification, never()).notify(...))
    //
    // Stub: when(fraudDetector.isSuspicious(any(), any(), any())).thenReturn(true)
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO2_fraudDetected_throwsAndDoesNotNotify() {
        throw new UnsupportedOperationException("TODO 2: not implemented");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 3 — ArgumentCaptor: verify notification content
    //
    // For a successful payment (amount=250, currency=EUR), capture the arguments
    // passed to notification.notify and assert:
    //   - first arg (userId) equals the userId you passed to process()
    //   - second arg (subject) equals "Payment processed"
    //   - third arg (body) contains "250" and "EUR"
    //
    // Use: ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class)
    //      verify(notification).notify(captor.capture(), captor.capture(), captor.capture())
    //      then inspect captor.getAllValues()
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO3_argumentCaptor_verifiesNotificationContent() {
        throw new UnsupportedOperationException("TODO 3: not implemented");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 4 — Audit log interactions
    //
    // For a successful payment, verify that auditLog.record is called exactly
    // once with eventType "PAYMENT_PROCESSED".
    //
    // For a blocked payment (fraud=true), verify that auditLog.record is called
    // exactly once with eventType "FRAUD_BLOCKED".
    //
    // Use: verify(auditLog).record(eq("PAYMENT_PROCESSED"), anyString(), anyString())
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO4_auditLog_recordedWithCorrectEventType() {
        throw new UnsupportedOperationException("TODO 4: not implemented");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TODO 5 — doThrow: verify exception from notification doesn't corrupt result
    //
    // Simulate notification.notify throwing a RuntimeException("email server down").
    // Verify that process() propagates this exception (assertThatThrownBy).
    //
    // Use: doThrow(new RuntimeException("email server down"))
    //          .when(notification).notify(anyString(), anyString(), anyString())
    //
    // This tests that we don't swallow downstream exceptions silently.
    // ──────────────────────────────────────────────────────────────────────────
    @Test
    void TODO5_notificationFailure_propagatesException() {
        throw new UnsupportedOperationException("TODO 5: not implemented");
    }
}
