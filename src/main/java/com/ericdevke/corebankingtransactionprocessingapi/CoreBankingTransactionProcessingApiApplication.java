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
            User user = userService.registerUser("Alice Munene", "alicemunene@gmail.com");
            Account account = accountService.openAccount(user.getId(), Currency.USD);
            ledgerService.deposit(account.getId(), new BigDecimal("1000.00"));

            System.out.println("Starting Balance: " +
                    accountService.getAccountsForUser(user.getId()).get(0).getBalance());

            long accountId = account.getId();

            Runnable withdrawTask = () -> {
                try{
                    ledgerService.withdraw(accountId, new BigDecimal("800.00"));
                    System.out.println(Thread.currentThread().getName() + "Withdrawal Succeeded");
                } catch (Exception e) {
                    System.out.println(Thread.currentThread().getName() + "Withdrawal Failed - "+ e.getMessage());
                }
            };

            Thread threadA = new Thread(withdrawTask, "Thread-A");
            Thread threadB = new Thread(withdrawTask, "Thread-B");

            threadA.start();
            threadB.start();

            threadA.join();
            threadB.join();

            BigDecimal finalBalance = accountService.getAccountsForUser(user.getId()).get(0).getBalance();
            System.out.println("Final balance: "+ finalBalance);
        };

    }
}