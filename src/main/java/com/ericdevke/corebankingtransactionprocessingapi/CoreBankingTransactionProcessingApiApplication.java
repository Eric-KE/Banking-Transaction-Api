package com.ericdevke.corebankingtransactionprocessingapi;

import com.ericdevke.corebankingtransactionprocessingapi.entity.Account;
import com.ericdevke.corebankingtransactionprocessingapi.entity.Currency;
import com.ericdevke.corebankingtransactionprocessingapi.entity.User;
import com.ericdevke.corebankingtransactionprocessingapi.service.AccountService;
import com.ericdevke.corebankingtransactionprocessingapi.service.LedgerService;
import com.ericdevke.corebankingtransactionprocessingapi.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class CoreBankingTransactionProcessingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreBankingTransactionProcessingApiApplication.class, args);
    }

    @Bean
    public CommandLineRunner testRun(UserService userService, AccountService accountService, LedgerService ledgerService) {
        return args -> {
            User user1 = userService.registerUser("Bernard Ngugi", "bernardngugi@gmail.com");
            Account account1 = accountService.openAccount(user1.getId(), Currency.USD);
            System.out.println("User1 Account: " + account1.getAccountNumber() + "| Balance: " + account1.getBalance());

            User user2 = userService.registerUser("Nancy Nyambura", "nancynyambura@gmail.com");
            Account account2 = accountService.openAccount(user2.getId(), Currency.USD);
            System.out.println("User2 Account: " + account2.getAccountNumber() + "| Balance: " + account2.getBalance());

            ledgerService.deposit(account1.getId(), new BigDecimal("1000.00"));
            System.out.println("After deposit, account balance: " + accountService.getAccountsForUser(user1.getId()).get(0).getBalance());

            ledgerService.withdraw(account1.getId(), new BigDecimal("200.00"));
            System.out.println("After withdrawal, account1 balance: " + accountService.getAccountsForUser(user1.getId()).get(0).getBalance());

            ledgerService.transfer(account1.getId(), account2.getId(), new BigDecimal("150.00"));
            System.out.println("After transfer, account1 balance: " + accountService.getAccountsForUser(user1.getId()).get(0).getBalance());
            System.out.println("After transfer, account2 balance: " + accountService.getAccountsForUser(user2.getId()).get(0).getBalance());
        };

    }
}