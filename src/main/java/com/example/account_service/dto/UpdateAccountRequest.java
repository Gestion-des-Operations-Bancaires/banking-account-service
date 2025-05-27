package com.example.account_service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateAccountRequest {
    
    private BigDecimal overdraftLimit;
    private String currency;
    
    // Constructeurs
    public UpdateAccountRequest() {}
}