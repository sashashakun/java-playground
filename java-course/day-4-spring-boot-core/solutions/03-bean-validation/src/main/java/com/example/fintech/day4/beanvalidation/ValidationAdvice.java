package com.example.fintech.day4.beanvalidation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ValidationAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        List<ValidationErrorResponse.FieldError> errors = ex.getBindingResult()
            .getFieldErrors().stream()
            .map(fe -> new ValidationErrorResponse.FieldError(fe.getField(), fe.getDefaultMessage()))
            .toList();
        return ResponseEntity.badRequest()
            .body(new ValidationErrorResponse("Validation failed", errors));
    }
}
