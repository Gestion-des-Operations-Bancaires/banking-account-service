package com.example.account_service.controller;

import com.example.account_service.dto.AccountResponse;
import com.example.account_service.dto.CreateAccountRequest;
import com.example.account_service.dto.UpdateAccountRequest;
import com.example.account_service.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/account")
@CrossOrigin(origins = "*")
public class AccountController {
    
    private AccountService accountService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Account Service!";
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId) {
        AccountResponse response = accountService.getAccountById(accountId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccountByNumber(@PathVariable String accountNumber) {
        AccountResponse response = accountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByCustomer(@PathVariable Long customerId) {
        List<AccountResponse> accounts = accountService.getAccountsByCustomerId(customerId);
        return ResponseEntity.ok(accounts);
    }
    
    @GetMapping("/customer/{customerId}/paginated")
    public ResponseEntity<Page<AccountResponse>> getAccountsByCustomerPaginated(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<AccountResponse> accounts = accountService.getAccountsByCustomerId(customerId, pageable);
        return ResponseEntity.ok(accounts);
    }
    
    @PutMapping("/{accountId}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable Long accountId,
            @Valid @RequestBody UpdateAccountRequest request) {
        
        AccountResponse response = accountService.updateAccount(accountId, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> closeAccount(@PathVariable Long accountId) {
        accountService.closeAccount(accountId);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{accountId}/suspend")
    public ResponseEntity<Void> suspendAccount(@PathVariable Long accountId) {
        accountService.suspendAccount(accountId);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{accountId}/activate")
    public ResponseEntity<Void> activateAccount(@PathVariable Long accountId) {
        accountService.activateAccount(accountId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BigDecimal> getAccountBalance(@PathVariable Long accountId) {
        BigDecimal balance = accountService.getAccountBalance(accountId);
        return ResponseEntity.ok(balance);
    }
    
    @PutMapping("/{accountId}/balance")
    public ResponseEntity<Void> updateBalance(
            @PathVariable Long accountId,
            @RequestParam BigDecimal newBalance) {
        
        accountService.updateBalance(accountId, newBalance);
        return ResponseEntity.ok().build();
    }
}