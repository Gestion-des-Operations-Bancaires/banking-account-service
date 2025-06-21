package com.example.account_service.dto;

import com.example.account_service.entity.AccountStatus;
import com.example.account_service.entity.AccountType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountResponseWithUser {
    private Long id;
    private User customer;
    private String accountNumber;
    private AccountType accountType;
    private AccountStatus status;
    private BigDecimal balance;
    private BigDecimal overdraftLimit;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // Constructeurs
    public AccountResponseWithUser() {}
}
