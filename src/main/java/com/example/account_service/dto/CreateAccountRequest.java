package com.example.account_service.dto;

import com.example.account_service.entity.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;  // Correct import pour @NotNull
import javax.validation.constraints.DecimalMin;  // Import manquant
import javax.validation.constraints.Pattern;  // Correct import pour @Pattern

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateAccountRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @DecimalMin(value = "0.0", inclusive = false, message = "Initial deposit must be positive")
    private BigDecimal initialDeposit;

    @DecimalMin(value = "0.0", message = "Overdraft limit must be non-negative")
    private BigDecimal overdraftLimit;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid 3-letter code")
    private String currency = "EUR";
}