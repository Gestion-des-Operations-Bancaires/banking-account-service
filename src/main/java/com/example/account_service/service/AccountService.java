package com.example.account_service.service;

import com.example.account_service.dto.*;
import com.example.account_service.entity.Account;
import com.example.account_service.entity.AccountEvent;
import com.example.account_service.entity.AccountStatus;
import com.example.account_service.entity.BalanceUpdateEvent;
import com.example.account_service.exception.AccountNotFoundException;
import com.example.account_service.exception.InsufficientFundsException;
import com.example.account_service.exception.InvalidAccountOperationException;
import com.example.account_service.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;
    
//    @Autowired
//    private KafkaTemplate<String, Object> kafkaTemplate;
    
    private final SecureRandom random = new SecureRandom();
    
    public AccountResponse createAccount(CreateAccountRequest request) {
        // Génération du numéro de compte unique
        String accountNumber = generateAccountNumber();
        
        // Création du compte
        Account account = new Account(accountNumber, request.getCustomerId(), request.getAccountType());
        
        if (request.getOverdraftLimit() != null) {
            account.setOverdraftLimit(request.getOverdraftLimit());
        }
        
        if (request.getCurrency() != null) {
            account.setCurrency(request.getCurrency());
        }
        
        // Dépôt initial si spécifié
        if (request.getInitialDeposit() != null && request.getInitialDeposit().compareTo(BigDecimal.ZERO) > 0) {
            account.setBalance(request.getInitialDeposit());
        }
        
        Account savedAccount = accountRepository.save(account);
        
        // Publication d'un événement
        //publishAccountEvent("ACCOUNT_CREATED", savedAccount);
        
        return mapToResponse(savedAccount);
    }
    
    public AccountResponse getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        
        return mapToResponse(account);
    }
    
    public AccountResponse getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber));
        
        return mapToResponse(account);
    }
    
    public List<AccountResponse> getAccountsByCustomerId(Long customerId) {
        List<Account> accounts = accountRepository.findByCustomerId(customerId);
        return accounts.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    public Page<AccountResponse> getAccountsByCustomerId(Long customerId, Pageable pageable) {
        Page<Account> accounts = accountRepository.findByCustomerId(customerId, pageable);
        return accounts.map(this::mapToResponse);
    }
    
    public AccountResponse updateAccount(Long accountId, UpdateAccountRequest request) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        
        if (request.getOverdraftLimit() != null) {
            account.setOverdraftLimit(request.getOverdraftLimit());
        }
        
        if (request.getCurrency() != null) {
            account.setCurrency(request.getCurrency());
        }
        
        Account updatedAccount = accountRepository.save(account);
        
        //publishAccountEvent("ACCOUNT_UPDATED", updatedAccount);
        
        return mapToResponse(updatedAccount);
    }
    
    public void closeAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new InvalidAccountOperationException("Cannot close account with non-zero balance");
        }
        
        account.setStatus(AccountStatus.CLOSED);
        accountRepository.save(account);
        
        //publishAccountEvent("ACCOUNT_CLOSED", account);
    }
    
    public void suspendAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        
        account.setStatus(AccountStatus.SUSPENDED);
        accountRepository.save(account);
        
        //publishAccountEvent("ACCOUNT_SUSPENDED", account);
    }
    
    public void activateAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        
        account.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(account);
        
        //publishAccountEvent("ACCOUNT_ACTIVATED", account);
    }
    
    public BigDecimal getAccountBalance(Long accountId) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        
        return account.getBalance();
    }
    
    public void updateBalance(Long accountId, BigDecimal newBalance) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        
        BigDecimal minAllowedBalance = account.getOverdraftLimit() != null 
            ? account.getOverdraftLimit().negate() 
            : BigDecimal.ZERO;
        
        if (newBalance.compareTo(minAllowedBalance) < 0) {
            throw new InsufficientFundsException("Insufficient funds. Balance would exceed overdraft limit");
        }
        
        account.setBalance(newBalance);
        accountRepository.save(account);
        
        //publishBalanceUpdateEvent(account);
    }
    
    private String generateAccountNumber() {
        String accountNumber;
        do {
            // Format: XXXX-XXXX-XXXX-XXXX (16 digits)
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                if (i > 0 && i % 4 == 0) {
                    sb.append("-");
                }
                sb.append(random.nextInt(10));
            }
            accountNumber = sb.toString();
        } while (accountRepository.existsByAccountNumber(accountNumber));
        
        return accountNumber;
    }
    
    private AccountResponse mapToResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setAccountNumber(account.getAccountNumber());
        response.setCustomerId(account.getCustomerId());
        response.setAccountType(account.getAccountType());
        response.setStatus(account.getStatus());
        response.setBalance(account.getBalance());
        response.setOverdraftLimit(account.getOverdraftLimit());
        response.setCurrency(account.getCurrency());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }
    
    /*private void publishAccountEvent(String eventType, Account account) {
        AccountEvent event = new AccountEvent();
        event.setEventType(eventType);
        event.setAccountId(account.getId());
        event.setAccountNumber(account.getAccountNumber());
        event.setCustomerId(account.getCustomerId());
        event.setAccountType(account.getAccountType().toString());
        event.setBalance(account.getBalance());
        event.setTimestamp(java.time.LocalDateTime.now());
        
        kafkaTemplate.send("account-events", event);
    }
    
    private void publishBalanceUpdateEvent(Account account) {
        BalanceUpdateEvent event = new BalanceUpdateEvent();
        event.setAccountId(account.getId());
        event.setAccountNumber(account.getAccountNumber());
        event.setNewBalance(account.getBalance());
        event.setTimestamp(java.time.LocalDateTime.now());
        
        kafkaTemplate.send("balance-updates", event);
    }*/
}