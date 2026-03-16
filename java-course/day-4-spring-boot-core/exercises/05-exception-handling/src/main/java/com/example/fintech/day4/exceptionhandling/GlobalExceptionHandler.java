package com.example.fintech.day4.exceptionhandling;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

/**
 * Exercise 05 — Global Exception Handler
 *
 * Uses ProblemDetail (RFC 9457, supported natively in Spring 6+).
 * Returns a consistent JSON error body for all error cases.
 *
 * ProblemDetail shape:
 * {
 *   "type":     "about:blank",
 *   "title":    "Payment Not Found",
 *   "status":   404,
 *   "detail":   "Payment pay-123 not found",
 *   "instance": "/api/payments/pay-123"
 * }
 *
 * TODO 1 — handleNotFound(PaymentNotFoundException ex, HttpServletRequest req):
 *   - ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage())
 *   - pd.setTitle("Payment Not Found")
 *   - pd.setInstance(URI.create(req.getRequestURI()))
 *   - return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd)
 *
 * TODO 2 — handleDuplicate(DuplicatePaymentException ex, HttpServletRequest req):
 *   - Status 409 CONFLICT
 *   - Title "Duplicate Payment"
 *
 * TODO 3 — handleLimitExceeded(PaymentLimitExceededException ex, HttpServletRequest req):
 *   - Status 422 UNPROCESSABLE_ENTITY
 *   - Title "Payment Limit Exceeded"
 *
 * TODO 4 — handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req):
 *   - Status 400 BAD_REQUEST
 *   - Title "Validation Failed"
 *   - detail: comma-joined field error messages
 *     Hint: ex.getBindingResult().getFieldErrors().stream()
 *               .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
 *               .collect(Collectors.joining(", "))
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentExceptions.PaymentNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(
            PaymentExceptions.PaymentNotFoundException ex,
            HttpServletRequest req) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @ExceptionHandler(PaymentExceptions.DuplicatePaymentException.class)
    public ResponseEntity<ProblemDetail> handleDuplicate(
            PaymentExceptions.DuplicatePaymentException ex,
            HttpServletRequest req) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @ExceptionHandler(PaymentExceptions.PaymentLimitExceededException.class)
    public ResponseEntity<ProblemDetail> handleLimitExceeded(
            PaymentExceptions.PaymentLimitExceededException ex,
            HttpServletRequest req) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest req) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
