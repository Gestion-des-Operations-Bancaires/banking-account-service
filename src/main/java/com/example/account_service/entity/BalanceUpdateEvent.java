package com.example.account_service.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BalanceUpdateEvent {

    private Long accountId;
    private String accountNumber;
    private BigDecimal newBalance;
    private LocalDateTime timestamp;
}