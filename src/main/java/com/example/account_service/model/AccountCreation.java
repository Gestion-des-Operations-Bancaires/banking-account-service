package com.example.account_service.model;

import com.example.account_service.entity.AccountStatus;
import com.example.account_service.entity.AccountType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreation implements Serializable {
    @JsonProperty
    private Long id;
    @JsonProperty
    private String accountNumber;
    @JsonProperty
    private Long customerId;
    @JsonProperty
    private AccountType accountType;
    @JsonProperty
    private AccountStatus status;
    @JsonProperty
    private BigDecimal balance;
    @JsonProperty
    private BigDecimal overdraftLimit;
    @JsonProperty
    private String currency;
    @Override
    public String toString() {
        return "AccountCreation{" +
                "accountId='" + id + '\'' +
                "accountNumber='" + accountNumber + '\'' +
                ", customerId=" + customerId +
                ", accountType=" + accountType +
                ", currency='" + currency + '\'' +
                '}';
    }
}