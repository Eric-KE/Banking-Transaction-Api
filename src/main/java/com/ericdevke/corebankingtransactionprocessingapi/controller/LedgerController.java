package com.ericdevke.corebankingtransactionprocessingapi.controller;

import com.ericdevke.corebankingtransactionprocessingapi.entity.Transaction;
import com.ericdevke.corebankingtransactionprocessingapi.service.LedgerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/ledger")
public class LedgerController {
    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService){
        this.ledgerService = ledgerService;
    }

    @PostMapping("/deposit")
    public Transaction deposit(@Valid @RequestBody DepositRequest request){
        return ledgerService.deposit(request.accountId(), request.amount());
    }

    @PostMapping("/withdraw")
    public Transaction withdraw(@Valid @RequestBody WithdrawRequest request) {
        return ledgerService.withdraw(request.accountId(), request.amount());
    }

    @PostMapping("/transfer")
    public Transaction transfer(@Valid @RequestBody TransferRequest request){
        return ledgerService.transfer(request.fromAccountId(), request.toAccountId(), request.amount());
    }

    public record DepositRequest(
            @NotNull(message = "accountId is required")
            long accountId,

            @NotNull(message = "amount is required")
            @Positive(message = "Amount must be greater than zero")
            BigDecimal amount) {}
    public record WithdrawRequest(
            @NotNull(message = "accountId is required")
            long accountId,

            @NotNull(message = "amount is required")
            @Positive(message = "amount must be greater than zero")
            BigDecimal amount) {}
    public record TransferRequest(
            @NotNull(message = "fromAccountId is required")
            long fromAccountId,

            @NotNull(message = "toAccountId is required")
            long toAccountId,

            @NotNull(message = "amount is required")
            @Positive(message = "amount must be greater than zero")
            BigDecimal amount) {}
}
