package com.ericdevke.corebankingtransactionprocessingapi.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "source_account_id", nullable = false, updatable = false)
    private Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "destination_account_id")
    private Account destinationAccount;

    @Column(name = "failure_reason")
    private String failureReason;

    public Transaction() {

    }

    public Transaction(TransactionType type, BigDecimal amount, Account sourceAccount, Account destinationAccount) {
        this.type = type;
        this.amount = amount;
        this.sourceAccount  = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.status = TransactionStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TransactionType getType(){
        return type;
    }

    public void setType (TransactionType type){
        this.type = type;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionStatus getStatus(){
        return status;
    }

    public void setStatus(TransactionStatus status){
        this.status = status;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Account getSourceAccount(){
        return sourceAccount;
    }

    public void setSourceAccount(Account sourceAccount){
        this.sourceAccount = sourceAccount;
    }

    public Account getDestinationAccount(){
        return destinationAccount;
    }

    public void setDestinationAccount(Account destinationAccount){
        this.destinationAccount = destinationAccount;
    }

    public String getFailureReason(){
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }



}
