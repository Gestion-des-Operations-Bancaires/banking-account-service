package com.example.account_service.entity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class AccountListener {

    @PrePersist
    public void setDefaultValues(Account entity) {
        LocalDateTime now = LocalDateTime.now();

        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(now);
        }

        if (entity.getUpdatedAt() == null) {
            entity.setUpdatedAt(now);
        }

        // Générer un numéro de compte unique si pas déjà défini
        if (entity.getAccountNumber() == null) {
            // Utiliser UUID pour garantir l'unicité
            String accountNumber = "ACC." + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            entity.setAccountNumber(accountNumber);
            System.out.println("PrePersist - Generated account number: " + accountNumber);
        }
    }

    @PreUpdate
    public void setUpdateTime(Account entity) {
        entity.setUpdatedAt(LocalDateTime.now());
    }
}