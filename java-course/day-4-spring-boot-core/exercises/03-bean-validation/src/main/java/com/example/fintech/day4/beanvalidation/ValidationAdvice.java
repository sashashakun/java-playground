package com.example.fintech.day4.beanvalidation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Exercise 03 — Validation Error Handler
 *
 * Catches MethodArgumentNotValidException (thrown when @Valid fails)
 * and returns a 400 Bad Request with a structured error body.
 *
 * TODO: Implement handleValidationErrors(MethodArgumentNotValidException ex):
 *
 *   Steps:
 *   1. Extract field errors from ex.getBindingResult().getFieldErrors()
 *   2. Map each to ValidationErrorResponse.FieldError(field, defaultMessage)
 *   3. Build a ValidationErrorResponse("Validation failed", fieldErrors)
 *   4. Return ResponseEntity.badRequest().body(response)
 *
 * Hint:
 *   List<ValidationErrorResponse.FieldError> errors = ex.getBindingResult()
 *       .getFieldErrors().stream()
 *       .map(fe -> new ValidationErrorResponse.FieldError(
 *           fe.getField(), fe.getDefaultMessage()))
 *       .toList();
 */
@RestControllerAdvice
public class ValidationAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
