package com.example.fintech.day6.mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentProcessorTest {

    @Mock private FraudDetector fraudDetector;
    @Mock private AuditLog auditLog;
    @Mock private NotificationPort notification;
    @InjectMocks private PaymentProcessor processor;

    @Test
    void happyPath_returnsCompletedResult() {
        when(fraudDetector.isSuspicious("user-1", new BigDecimal("100.00"), "USD"))
            .thenReturn(false);

        PaymentResult result = processor.process("user-1", new BigDecimal("100.00"), "USD");

        assertThat(result.status()).isEqualTo("COMPLETED");
        assertThat(result.transactionId()).isNotNull().startsWith("txn-");
        assertThat(result.userId()).isEqualTo("user-1");
        assertThat(result.amount()).isEqualByComparingTo("100.00");
        assertThat(result.currency()).isEqualTo("USD");
    }

    @Test
    void fraudDetected_throwsAndDoesNotNotify() {
        when(fraudDetector.isSuspicious(any(), any(), any())).thenReturn(true);

        assertThatThrownBy(() -> processor.process("user-2", new BigDecimal("50000"), "USD"))
            .isInstanceOf(PaymentProcessor.FraudException.class)
            .hasMessageContaining("user-2");

        verify(notification, never()).notify(anyString(), anyString(), anyString());
    }

    @Test
    void argumentCaptor_verifiesNotificationContent() {
        when(fraudDetector.isSuspicious(any(), any(), any())).thenReturn(false);

        processor.process("user-3", new BigDecimal("250.00"), "EUR");

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(notification).notify(captor.capture(), captor.capture(), captor.capture());

        List<String> args = captor.getAllValues();
        assertThat(args.get(0)).isEqualTo("user-3");                      // userId
        assertThat(args.get(1)).isEqualTo("Payment processed");           // subject
        assertThat(args.get(2)).contains("250.00").contains("EUR");       // body
    }

    @Test
    void auditLog_recordedWithCorrectEventType() {
        when(fraudDetector.isSuspicious(any(), any(), any())).thenReturn(false);
        processor.process("user-4", new BigDecimal("100"), "GBP");

        verify(auditLog).record(eq("PAYMENT_PROCESSED"), eq("user-4"), anyString());
    }

    @Test
    void auditLog_fraudBlocked_recordsCorrectEvent() {
        when(fraudDetector.isSuspicious(any(), any(), any())).thenReturn(true);
        assertThatThrownBy(() -> processor.process("user-5", new BigDecimal("999999"), "USD"))
            .isInstanceOf(PaymentProcessor.FraudException.class);

        verify(auditLog).record(eq("FRAUD_BLOCKED"), eq("user-5"), anyString());
    }

    @Test
    void notificationFailure_propagatesException() {
        when(fraudDetector.isSuspicious(any(), any(), any())).thenReturn(false);
        doThrow(new RuntimeException("email server down"))
            .when(notification).notify(anyString(), anyString(), anyString());

        assertThatThrownBy(() -> processor.process("user-6", new BigDecimal("100"), "USD"))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("email server down");
    }
}
