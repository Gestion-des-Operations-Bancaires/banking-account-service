package com.example.account_service.event;

import lombok.Getter;

@Getter
public enum LoanEventType {
    // Loan lifecycle events
    LOAN_CREATED("Loan application created"),
    LOAN_APPROVED("Loan application approved"),
    LOAN_REJECTED("Loan application rejected"),
    LOAN_ACTIVATED("Loan activated and funds disbursed"),
    LOAN_CLOSED("Loan closed"),
    LOAN_PAID_OFF("Loan fully paid off"),
    LOAN_DEFAULTED("Loan defaulted"),
    
    // Payment events
    PAYMENT_MADE("Payment made on loan"),
    PAYMENT_MISSED("Payment missed on loan"),
    PAYMENT_LATE("Late payment made on loan"),
    PAYMENT_EARLY("Early payment made on loan"),
    
    // Status change events
    STATUS_CHANGED("Loan status changed"),
    INTEREST_RATE_CHANGED("Interest rate modified"),
    TERM_MODIFIED("Loan term modified"),
    
    // Administrative events
    LOAN_UPDATED("Loan details updated"),
    LOAN_SUSPENDED("Loan suspended"),
    LOAN_REACTIVATED("Loan reactivated"),
    
    // Risk and compliance events
    LOAN_UNDER_REVIEW("Loan under review"),
    COMPLIANCE_CHECK_FAILED("Compliance check failed"),
    RISK_ASSESSMENT_UPDATED("Risk assessment updated");
    
    private final String description;
    
    LoanEventType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.name() + ": " + this.description;
    }
}