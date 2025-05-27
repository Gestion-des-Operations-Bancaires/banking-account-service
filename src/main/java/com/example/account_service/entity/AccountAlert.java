package com.example.account_service.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "account_alerts")
public class AccountAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertType alertType;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
    