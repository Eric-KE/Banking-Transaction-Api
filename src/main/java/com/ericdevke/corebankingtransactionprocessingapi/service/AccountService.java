package com.ericdevke.corebankingtransactionprocessingapi.service;

import com.ericdevke.corebankingtransactionprocessingapi.entity.Account;
import com.ericdevke.corebankingtransactionprocessingapi.entity.Currency;
import com.ericdevke.corebankingtransactionprocessingapi.entity.User;
import com.ericdevke.corebankingtransactionprocessingapi.exception.ResourceNotFoundException;
import com.ericdevke.corebankingtransactionprocessingapi.repository.AccountRepository;
import com.ericdevke.corebankingtransactionprocessingapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Account openAccount(Long userId, Currency currency) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found with id: "+ userId)
                );

        List<Account> existingAccounts = accountRepository.findByUserId(userId);

        boolean alreadyHasCurrency = existingAccounts.stream()
                .anyMatch(account -> account.getCurrency() == currency);

        if (alreadyHasCurrency) {
            throw new IllegalArgumentException(
                    "User already has a "+ currency + "account");
        }

        Account account = new Account(currency, user);

        return accountRepository.save(account);
    }

    public Account getAccountById(long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: "+ id));
    }

    public List<Account> getAccountsForUser(Long userId) {
        return accountRepository.findByUserId(userId);
    }


}
