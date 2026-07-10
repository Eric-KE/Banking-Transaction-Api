package com.ericdevke.corebankingtransactionprocessingapi.controller;

import com.ericdevke.corebankingtransactionprocessingapi.entity.Transaction;
import com.ericdevke.corebankingtransactionprocessingapi.service.LedgerService;
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
    public Transaction deposit(@RequestBody DepositRequest request){
        return ledgerService.deposit(request.accountId(), request.amount());
    }

    @PostMapping("/withdraw")
    public Transaction withdraw(@RequestBody WithdrawRequest request) {
        return ledgerService.withdraw(request.accountId(), request.amount());
    }

    @PostMapping("/transfer")
    public Transaction transfer(@RequestBody TransferRequest request){
        return ledgerService.transfer(request.fromAccountId(), request.toAccountId(), request.amount());
    }

    public record DepositRequest(long accountId, BigDecimal amount) {}
    public record WithdrawRequest(long accountId, BigDecimal amount) {}
    public record TransferRequest(long fromAccountId, long toAccountId, BigDecimal amount) {}
}
