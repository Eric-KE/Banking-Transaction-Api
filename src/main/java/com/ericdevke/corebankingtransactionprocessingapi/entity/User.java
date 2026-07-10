package com.ericdevke.corebankingtransactionprocessingapi.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false)
    private String email;

    @Column(name = "customer_number", nullable = false, updatable = false)
    private String customerNumber;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts;

    public User(){

    }

    public User(String fullname, String email, String customerNumber){
        this.fullname = fullname;
        this.email = email;
        this.customerNumber = customerNumber;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getFullname(){
        return fullname;
    }

    public void setFullname(String fullname){
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts){
        this.accounts = accounts;
    }


    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber){
        this.customerNumber = customerNumber;
    }
}
