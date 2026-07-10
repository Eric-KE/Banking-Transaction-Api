package com.ericdevke.corebankingtransactionprocessingapi.service;

import com.ericdevke.corebankingtransactionprocessingapi.entity.Account;
import com.ericdevke.corebankingtransactionprocessingapi.entity.Transaction;
import com.ericdevke.corebankingtransactionprocessingapi.entity.TransactionStatus;
import com.ericdevke.corebankingtransactionprocessingapi.entity.TransactionType;
import com.ericdevke.corebankingtransactionprocessingapi.exception.InsufficientFundsException;
import com.ericdevke.corebankingtransactionprocessingapi.exception.ResourceNotFoundException;
import com.ericdevke.corebankingtransactionprocessingapi.repository.AccountRepository;
import com.ericdevke.corebankingtransactionprocessingapi.repository.TransactionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;

@Service
public class LedgerService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public LedgerService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Transaction deposit(long accountId, BigDecimal amount){
        validateAmount(amount);

        Account account = getAccountForUpdateOrThrow(accountId);
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction(TransactionType.DEPOSIT, amount, account, null);
        transaction.setStatus(TransactionStatus.SUCCESS);
        return transactionRepository.save(transaction);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Transaction withdraw(long accountId, BigDecimal amount){
        validateAmount(amount);

        Account account = getAccountForUpdateOrThrow(accountId);

        if(account.getBalance().compareTo(amount) < 0){
            throw new InsufficientFundsException("Insufficient funds in account "+ account.getAccountNumber());
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction(TransactionType.WITHDRAWAL, amount, account, null);
        transaction.setStatus(TransactionStatus.SUCCESS);
        return transactionRepository.save(transaction);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Transaction transfer(Long fromAccountId, long toAccountId, BigDecimal amount){
        validateAmount(amount);

        if(fromAccountId.equals(toAccountId)){
            throw new IllegalStateException("Cannot transfer to the same account");
        }

        Account fromAccount = getAccountForUpdateOrThrow(fromAccountId);
        Account toAccount = getAccountForUpdateOrThrow(toAccountId);

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

    private Account getAccountForUpdateOrThrow(long accountId){
        return accountRepository.findByIdForUpdate(accountId)
                .orElseThrow( () -> new ResourceNotFoundException("Account not found with id: "+ accountId));
    }

    private void validateAmount(BigDecimal amount) {
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Amount must be grater than zero");
        }
    }
}
