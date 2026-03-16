package com.example.fintech.capstone.web;

import com.example.fintech.capstone.exception.AccountNotFoundException;
import com.example.fintech.capstone.exception.InsufficientFundsException;
import com.example.fintech.capstone.exception.PaymentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

/**
 * Capstone Exercise L — Web: GlobalExceptionHandler
 *
 * TODO L1: Add @ControllerAdvice to this class.
 *
 * TODO L2: Implement handleNotFound — handles AccountNotFoundException and
 *          PaymentNotFoundException → 404 ProblemDetail.
 *          Use @ExceptionHandler({AccountNotFoundException.class, PaymentNotFoundException.class})
 *          Return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage())
 *
 * TODO L3: Implement handleInsufficientFunds — handles InsufficientFundsException → 422.
 *          Return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage())
 *
 * TODO L4: Implement handleValidation — handles MethodArgumentNotValidException → 400.
 *          Collect all field error messages and join them.
 *          Return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, combinedMessage)
 */
// TODO L1: @ControllerAdvice
public class GlobalExceptionHandler {

    // TODO L2: @ExceptionHandler({AccountNotFoundException.class, PaymentNotFoundException.class})
    public ProblemDetail handleNotFound(RuntimeException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // TODO L3: @ExceptionHandler(InsufficientFundsException.class)
    public ProblemDetail handleInsufficientFunds(InsufficientFundsException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    // TODO L4: @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
            .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
            .collect(Collectors.joining("; "));
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
    }
}
