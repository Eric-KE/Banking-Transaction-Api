package com.ericdevke.corebankingtransactionprocessingapi.controller;


import com.ericdevke.corebankingtransactionprocessingapi.entity.Account;
import com.ericdevke.corebankingtransactionprocessingapi.entity.Currency;
import com.ericdevke.corebankingtransactionprocessingapi.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public Account openAccount(@Valid @RequestBody OpenAccountRequest request) {
        return accountService.openAccount(request.userId(), request.currency());
    }

    @GetMapping("/user/{userId}")
    public List<Account> getAccountsForUser(@PathVariable long userId) {
        return accountService.getAccountsForUser(userId);
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable long id){
        return accountService.getAccountById(id);
    }

    public record OpenAccountRequest(
            @NotNull(message = "UserId is required")
            long userId,

            @NotNull(message = "currency is required")
            Currency currency
    ) {}
}
