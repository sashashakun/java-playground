package com.example.fintech.capstone.web;

import com.example.fintech.capstone.service.AccountService;
import com.example.fintech.capstone.web.dto.AccountResponse;
import com.example.fintech.capstone.web.dto.CreateAccountRequest;
import com.example.fintech.capstone.web.dto.TransferRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

// SOLUTION J — Web: AccountController

@RestController                                         // TODO J1 ✓
@RequestMapping("/accounts")                           // TODO J1 ✓
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping                                        // TODO J2 ✓
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody @Valid CreateAccountRequest request,   // TODO J2 ✓
            UriComponentsBuilder uriBuilder) {
        var account = accountService.createAccount(
            request.ownerId(), request.currency(), request.initialBalance());
        var location = uriBuilder.path("/accounts/{id}").buildAndExpand(account.getId()).toUri();
        return ResponseEntity.created(location).body(AccountResponse.from(account));
    }

    @GetMapping                                         // TODO J3 ✓
    public List<AccountResponse> getByOwner(
            @RequestParam("ownerId") String ownerId) { // TODO J3 ✓
        return accountService.findByOwner(ownerId).stream()
            .map(AccountResponse::from)
            .toList();
    }

    @PostMapping("/transfer")                           // TODO J3 ✓
    public ResponseEntity<Void> transfer(
            @RequestBody @Valid TransferRequest request) { // TODO J3 ✓
        accountService.transfer(request.fromAccountId(), request.toAccountId(), request.amount());
        return ResponseEntity.noContent().build();
    }
}
