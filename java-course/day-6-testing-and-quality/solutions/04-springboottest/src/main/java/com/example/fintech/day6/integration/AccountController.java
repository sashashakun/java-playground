package com.example.fintech.day6.integration;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    public record OpenRequest(
        @NotBlank String ownerId,
        @NotBlank String currency,
        @NotNull @DecimalMin("0") BigDecimal initialDeposit
    ) {}

    public record TransferRequest(
        @NotBlank String fromOwnerId,
        @NotBlank String toOwnerId,
        @NotNull @DecimalMin("0.01") BigDecimal amount
    ) {}

    @PostMapping
    public ResponseEntity<Account> open(@Valid @RequestBody OpenRequest req) {
        Account account = service.openAccount(req.ownerId(), req.currency(), req.initialDeposit());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}").buildAndExpand(account.getId()).toUri();
        return ResponseEntity.created(location).body(account);
    }

    @GetMapping("/{ownerId}")
    public ResponseEntity<Account> get(@PathVariable String ownerId) {
        return ResponseEntity.ok(service.getByOwnerId(ownerId));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequest req) {
        service.transfer(req.fromOwnerId(), req.toOwnerId(), req.amount());
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(AccountService.AccountNotFoundException.class)
    ResponseEntity<ProblemDetail> notFound(AccountService.AccountNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(404);
        pd.setDetail(ex.getMessage());
        return ResponseEntity.status(404).body(pd);
    }

    @ExceptionHandler(AccountService.DuplicateAccountException.class)
    ResponseEntity<ProblemDetail> conflict(AccountService.DuplicateAccountException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(409);
        pd.setDetail(ex.getMessage());
        return ResponseEntity.status(409).body(pd);
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<ProblemDetail> unprocessable(IllegalStateException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(422);
        pd.setDetail(ex.getMessage());
        return ResponseEntity.status(422).body(pd);
    }
}
