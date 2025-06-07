package com.example.account_service.entity;


import jakarta.persistence.PostPersist;
import org.springframework.stereotype.Component;

@Component
public class AccountListener {

    @PostPersist
    public void setReference(Account entity) {

        if (entity.getAccountNumber() == null) {
            // Générer la référence basée sur l'ID
            String formattedAccount = "ACC." + String.format("%04d", entity.getId());
            entity.setAccountNumber(formattedAccount);
            System.out.println("PostPersist"+entity.getAccountNumber());
        }
    }
}
