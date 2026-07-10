package com.ericdevke.corebankingtransactionprocessingapi.service;

import com.ericdevke.corebankingtransactionprocessingapi.entity.Account;
import com.ericdevke.corebankingtransactionprocessingapi.entity.Transaction;
import com.ericdevke.corebankingtransactionprocessingapi.entity.TransactionStatus;
import com.ericdevke.corebankingtransactionprocessingapi.entity.TransactionType;
import com.ericdevke.corebankingtransactionprocessingapi.repository.AccountRepository;
import com.ericdevke.corebankingtransactionprocessingapi.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LedgerService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public LedgerService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction deposit(long accountId, BigDecimal amount){
        validateAmount(amount);

        Account account = getAccountOrThrow(accountId);
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction(TransactionType.DEPOSIT, amount, account, null);
        transaction.setStatus(TransactionStatus.SUCCESS);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction withdraw(long accountId, BigDecimal amount){
        validateAmount(amount);

        Account account = getAccountOrThrow(accountId);

        if(account.getBalance().compareTo(amount) < 0){
            throw new IllegalStateException("Insufficient funds in account "+ account.getAccountNumber());
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction(TransactionType.WITHDRAWAL, amount, account, null);
        transaction.setStatus(TransactionStatus.SUCCESS);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction transfer(Long fromAccountId, long toAccountId, BigDecimal amount){
        validateAmount(amount);

        if(fromAccountId.equals(toAccountId)){
            throw new IllegalStateException("Cannot transfer to the same account");
        }

        Account fromAccount = getAccountOrThrow(fromAccountId);
        Account toAccount = getAccountOrThrow(toAccountId);

        if(fromAccount.getCurrency() != toAccount.getCurrency()){
            throw new IllegalStateException("Cannot transfer between accounts of different currencies");
        }

        if(fromAccount.getBalance().compareTo(amount) < 0){
            throw new IllegalStateException("Insufficient funds in account " + fromAccount.getAccountNumber());
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction(TransactionType.TRANSFER, amount, fromAccount, toAccount);
        transaction.setStatus(TransactionStatus.SUCCESS);
        return transactionRepository.save(transaction);
    }

    private Account getAccountOrThrow(long accountId){
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with id: "+ accountId));
    }

    private void validateAmount(BigDecimal amount) {
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Amount must be grater than zero");
        }
    }
}
