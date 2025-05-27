package com.example.account_service.dto;

import com.example.account_service.entity.AccountStatus;
import com.example.account_service.entity.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountResponse {
    
    private Long id;
    private String accountNumber;
    private Long customerId;
    private AccountType accountType;
    private AccountStatus status;
    private BigDecimal balance;
    private BigDecimal overdraftLimit;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructeurs
    public AccountResponse() {}
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    
    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
    
    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }
    
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    
    public BigDecimal getOverdraftLimit() { return overdraftLimit; }
    public void setOverdraftLimit(BigDecimal overdraftLimit) { this.overdraftLimit = overdraftLimit; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}