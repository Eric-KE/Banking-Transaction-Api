package com.ericdevke.corebankingtransactionprocessingapi.repository;

import com.ericdevke.corebankingtransactionprocessingapi.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySourceAccountId(Long accountId);
}
