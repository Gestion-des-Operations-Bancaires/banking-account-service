package com.example.account_service.event;

import com.example.account_service.entity.AccountType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEvent implements Serializable {
    @JsonProperty
    private Long transactionId;
    @JsonProperty
    private String accountNumber;
    @JsonProperty
    private Long customerId;
    @JsonProperty
    private AccountType accountType;
    @JsonProperty
    private String transactionType;
    @JsonProperty
    private BigDecimal amount;
    @JsonProperty
    private String currency;

}