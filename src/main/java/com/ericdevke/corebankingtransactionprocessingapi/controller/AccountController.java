package com.ericdevke.corebankingtransactionprocessingapi.controller;


import com.ericdevke.corebankingtransactionprocessingapi.entity.Account;
import com.ericdevke.corebankingtransactionprocessingapi.entity.Currency;
import com.ericdevke.corebankingtransactionprocessingapi.service.AccountService;
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
    public Account openAccount(@RequestBody OpenAccountRequest request) {
        return accountService.openAccount(request.userId(), request.currency());
    }

    @GetMapping("/user/{userId}")
    public List<Account> getAccountsForUser(@PathVariable long userId) {
        return accountService.getAccountsForUser(userId);
    }

    public record OpenAccountRequest(long userId, Currency currency) {}
}
