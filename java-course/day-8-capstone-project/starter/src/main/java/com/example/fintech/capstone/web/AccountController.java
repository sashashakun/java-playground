package com.example.fintech.capstone.web;

import com.example.fintech.capstone.service.AccountService;
import com.example.fintech.capstone.web.dto.AccountResponse;
import com.example.fintech.capstone.web.dto.CreateAccountRequest;
import com.example.fintech.capstone.web.dto.TransferRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Capstone Exercise J — Web: AccountController
 *
 * TODO J1: Add @RestController and @RequestMapping("/accounts") to this class.
 *
 * TODO J2: Annotate createAccount:
 *          - @PostMapping → 201 Created with Location header pointing to /accounts/{id}
 *          - @RequestBody @Valid on the parameter
 *          Use UriComponentsBuilder to build the Location URI.
 *
 * TODO J3: Annotate getByOwner:
 *          - @GetMapping with @RequestParam("ownerId")
 *          - Returns 200 with List<AccountResponse>
 *
 *         Annotate transfer:
 *          - @PostMapping("/transfer")
 *          - @RequestBody @Valid on the parameter
 *          - Returns 204 No Content on success
 */
// TODO J1: @RestController @RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // TODO J2: @PostMapping — return 201 Created
    public ResponseEntity<AccountResponse> createAccount(
            // TODO J2: @RequestBody @Valid
            CreateAccountRequest request,
            UriComponentsBuilder uriBuilder) {
        var account = accountService.createAccount(
            request.ownerId(), request.currency(), request.initialBalance());
        var location = uriBuilder.path("/accounts/{id}").buildAndExpand(account.getId()).toUri();
        return ResponseEntity.created(location).body(AccountResponse.from(account));
    }

    // TODO J3: @GetMapping — return 200 with list
    public List<AccountResponse> getByOwner(
            // TODO J3: @RequestParam("ownerId")
            String ownerId) {
        return accountService.findByOwner(ownerId).stream()
            .map(AccountResponse::from)
            .toList();
    }

    // TODO J3: @PostMapping("/transfer") — return 204
    public ResponseEntity<Void> transfer(
            // TODO J3: @RequestBody @Valid
            TransferRequest request) {
        accountService.transfer(request.fromAccountId(), request.toAccountId(), request.amount());
        return ResponseEntity.noContent().build();
    }
}
