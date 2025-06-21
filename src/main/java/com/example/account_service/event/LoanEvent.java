package com.example.account_service.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanEvent {
    
    @JsonProperty("eventType")
    private LoanEventType eventType;
    
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
    
    @JsonProperty("loanId")
    private Long loanId;
    
    @JsonProperty("loanNumber")
    private String loanNumber;
    
    @JsonProperty("customerId")
    private Long customerId;
    
    @JsonProperty("accountNumber")
    private String accountNumber;
    
    @JsonProperty("principalAmount")
    private BigDecimal principalAmount;
    
    @JsonProperty("interestRate")
    private BigDecimal interestRate;
    
    @JsonProperty("termInMonths")
    private Integer termInMonths;
    
    @JsonProperty("monthlyPayment")
    private BigDecimal monthlyPayment;
    
    @JsonProperty("outstandingBalance")
    private BigDecimal outstandingBalance;
    
    @JsonProperty("loanType")
    private LoanType loanType;
    
    @JsonProperty("status")
    private LoanStatus status;
    
    @JsonProperty("startDate")
    private LocalDate startDate;
    
    @JsonProperty("maturityDate")
    private LocalDate maturityDate;
    
    @JsonProperty("lastPaymentDate")
    private LocalDate lastPaymentDate;
    
    @JsonProperty("paymentsMade")
    private Integer paymentsMade;
    
    @JsonProperty("paymentsRemaining")
    private Integer paymentsRemaining;
    
    @JsonProperty("paymentAmount")
    private BigDecimal paymentAmount; // For payment-related events
    
    @JsonProperty("previousStatus")
    private LoanStatus previousStatus; // For status change events
    
    @JsonProperty("reason")
    private String reason; // For approval/rejection reasons
    
    // Constructors for specific event types
    public LoanEvent(LoanEventType eventType, Long loanId, String loanNumber, Long customerId) {
        this.eventType = eventType;
        this.loanId = loanId;
        this.loanNumber = loanNumber;
        this.customerId = customerId;
        this.timestamp = LocalDateTime.now();
    }
    
    public LoanEvent(LoanEventType eventType, Long loanId, String loanNumber, 
                     Long customerId, LoanStatus status, LoanStatus previousStatus) {
        this.eventType = eventType;
        this.loanId = loanId;
        this.loanNumber = loanNumber;
        this.customerId = customerId;
        this.status = status;
        this.previousStatus = previousStatus;
        this.timestamp = LocalDateTime.now();
    }
    
    public LoanEvent(LoanEventType eventType, Long loanId, String loanNumber, 
                     Long customerId, BigDecimal paymentAmount, BigDecimal outstandingBalance) {
        this.eventType = eventType;
        this.loanId = loanId;
        this.loanNumber = loanNumber;
        this.customerId = customerId;
        this.paymentAmount = paymentAmount;
        this.outstandingBalance = outstandingBalance;
        this.timestamp = LocalDateTime.now();
    }
    
    // Factory methods for common events
    public static LoanEvent loanCreated(Long loanId, String loanNumber, Long customerId, 
                                       String accountNumber, BigDecimal principalAmount, 
                                       BigDecimal interestRate, Integer termInMonths, 
                                       BigDecimal monthlyPayment, LoanType loanType, 
                                       LocalDate startDate, LocalDate maturityDate) {
        LoanEvent event = new LoanEvent();
        event.setEventType(LoanEventType.LOAN_CREATED);
        event.setTimestamp(LocalDateTime.now());
        event.setLoanId(loanId);
        event.setLoanNumber(loanNumber);
        event.setCustomerId(customerId);
        event.setAccountNumber(accountNumber);
        event.setPrincipalAmount(principalAmount);
        event.setInterestRate(interestRate);
        event.setTermInMonths(termInMonths);
        event.setMonthlyPayment(monthlyPayment);
        event.setOutstandingBalance(principalAmount);
        event.setLoanType(loanType);
        event.setStatus(LoanStatus.PENDING);
        event.setStartDate(startDate);
        event.setMaturityDate(maturityDate);
        event.setPaymentsMade(0);
        event.setPaymentsRemaining(termInMonths);
        return event;
    }
    
    public static LoanEvent loanApproved(Long loanId, String loanNumber, Long customerId, String reason) {
        LoanEvent event = new LoanEvent(LoanEventType.LOAN_APPROVED, loanId, loanNumber, customerId);
        event.setStatus(LoanStatus.ACTIVE);
        event.setPreviousStatus(LoanStatus.PENDING);
        event.setReason(reason);
        return event;
    }
    
    public static LoanEvent loanRejected(Long loanId, String loanNumber, Long customerId, String reason) {
        LoanEvent event = new LoanEvent(LoanEventType.LOAN_REJECTED, loanId, loanNumber, customerId);
        event.setStatus(LoanStatus.REJECTED);
        event.setPreviousStatus(LoanStatus.PENDING);
        event.setReason(reason);
        return event;
    }
    
    public static LoanEvent paymentMade(Long loanId, String loanNumber, Long customerId, 
                                       BigDecimal paymentAmount, BigDecimal outstandingBalance, 
                                       Integer paymentsMade, Integer paymentsRemaining) {
        LoanEvent event = new LoanEvent(LoanEventType.PAYMENT_MADE, loanId, loanNumber, 
                                       customerId, paymentAmount, outstandingBalance);
        event.setPaymentsMade(paymentsMade);
        event.setPaymentsRemaining(paymentsRemaining);
        event.setLastPaymentDate(LocalDate.now());
        return event;
    }
    
    public static LoanEvent loanPaidOff(Long loanId, String loanNumber, Long customerId, 
                                       BigDecimal finalPaymentAmount) {
        LoanEvent event = new LoanEvent(LoanEventType.LOAN_PAID_OFF, loanId, loanNumber, customerId);
        event.setPaymentAmount(finalPaymentAmount);
        event.setOutstandingBalance(BigDecimal.ZERO);
        event.setStatus(LoanStatus.PAID_OFF);
        event.setLastPaymentDate(LocalDate.now());
        return event;
    }
    
    public static LoanEvent loanDefaulted(Long loanId, String loanNumber, Long customerId, String reason) {
        LoanEvent event = new LoanEvent(LoanEventType.LOAN_DEFAULTED, loanId, loanNumber, customerId);
        event.setStatus(LoanStatus.DEFAULTED);
        event.setPreviousStatus(LoanStatus.ACTIVE);
        event.setReason(reason);
        return event;
    }
    
    public static LoanEvent paymentMissed(Long loanId, String loanNumber, Long customerId, 
                                         BigDecimal missedAmount, LocalDate dueDate) {
        LoanEvent event = new LoanEvent(LoanEventType.PAYMENT_MISSED, loanId, loanNumber, customerId);
        event.setPaymentAmount(missedAmount);
        event.setReason("Payment missed for due date: " + dueDate);
        return event;
    }
}