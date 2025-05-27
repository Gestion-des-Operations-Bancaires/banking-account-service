package com.example.account_service.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountEvent {

    private String eventType;
    private Long accountId;
    private String accountNumber;
    private Long customerId;
    private String accountType;
    private BigDecimal balance;
    private LocalDateTime timestamp;
}