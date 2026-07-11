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
}