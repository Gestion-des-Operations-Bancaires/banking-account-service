package com.example.account_service.entity;

import lombok.Getter;

@Getter
public enum AccountType {
    CURRENT("Compte Courant"),
    SAVINGS("Compte Épargne"), 
    PROFESSIONAL("Compte Professionnel"),
    JOINT("Compte Joint");
    
    private final String displayName;

    AccountType(String displayName) {
        this.displayName = displayName;
    }

}