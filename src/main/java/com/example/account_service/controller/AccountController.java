package com.example.account_service.controller;

import com.example.account_service.dto.AccountResponse;
import com.example.account_service.dto.CreateAccountRequest;
import com.example.account_service.dto.UpdateAccountRequest;
import com.example.account_service.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
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
@Tag(name = "Account Management", description = "Operations for managing bank accounts")
public class AccountController {
    private AccountService accountService;

    @GetMapping("/user-id")
    @Operation(summary = "identity check", description = "Simple endpoint to check id of the connected user")
    public Integer getUserId(){
        Integer userId = accountService.getUserId();
        if (userId == null) {
            return -1; // or throw an exception
        }
        return userId;
    }

    @GetMapping("/hello")
    @Operation(summary = "Health check", description = "Simple endpoint to test service availability")
    public String hello() {
        return "Hello from Account Service!";
    }

    @PostMapping
    @Operation(summary = "Create new account", description = "Create a new bank account for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AccountResponse> createAccount(
            @Parameter(description = "Account creation request") @RequestBody CreateAccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Get account by ID", description = "Retrieve account details by account ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<AccountResponse> getAccount(
            @Parameter(description = "Account ID") @PathVariable Long accountId) {
        AccountResponse response = accountService.getAccountById(accountId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/number/{accountNumber}")
    @Operation(summary = "Get account by number", description = "Retrieve account details by account number")
    public ResponseEntity<AccountResponse> getAccountByNumber(
            @Parameter(description = "Account number") @PathVariable String accountNumber) {
        AccountResponse response = accountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get customer accounts", description = "Retrieve all accounts for a specific customer")
    public ResponseEntity<List<AccountResponse>> getAccountsByCustomer(
            @Parameter(description = "Customer ID") @PathVariable Long customerId) {
        List<AccountResponse> accounts = accountService.getAccountsByCustomerId(customerId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/customer/{customerId}/paginated")
    @Operation(summary = "Get customer accounts (paginated)", description = "Retrieve customer accounts with pagination")
    public ResponseEntity<Page<AccountResponse>> getAccountsByCustomerPaginated(
            @Parameter(description = "Customer ID") @PathVariable Long customerId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AccountResponse> accounts = accountService.getAccountsByCustomerId(customerId, pageable);
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/{accountId}")
    @Operation(summary = "Update account", description = "Update existing account information")
    public ResponseEntity<AccountResponse> updateAccount(
            @Parameter(description = "Account ID") @PathVariable Long accountId,
            @Parameter(description = "Account update request") @Valid @RequestBody UpdateAccountRequest request) {

        AccountResponse response = accountService.updateAccount(accountId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{accountId}")
    @Operation(summary = "Close account", description = "Close/delete an existing account")
    @ApiResponse(responseCode = "204", description = "Account closed successfully")
    public ResponseEntity<Void> closeAccount(
            @Parameter(description = "Account ID") @PathVariable Long accountId) {
        accountService.closeAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{accountId}/suspend")
    @Operation(summary = "Suspend account", description = "Suspend an active account")
    public ResponseEntity<Void> suspendAccount(
            @Parameter(description = "Account ID") @PathVariable Long accountId) {
        accountService.suspendAccount(accountId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{accountId}/activate")
    @Operation(summary = "Activate account", description = "Activate a suspended account")
    public ResponseEntity<Void> activateAccount(
            @Parameter(description = "Account ID") @PathVariable Long accountId) {
        accountService.activateAccount(accountId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}/balance")
    @Operation(summary = "Get account balance", description = "Retrieve current account balance")
    public ResponseEntity<BigDecimal> getAccountBalance(
            @Parameter(description = "Account ID") @PathVariable Long accountId) {
        BigDecimal balance = accountService.getAccountBalance(accountId);
        return ResponseEntity.ok(balance);
    }

    @PutMapping("/{accountId}/balance")
    @Operation(summary = "Update account balance", description = "Update account balance directly")
    public ResponseEntity<Void> updateBalance(
            @Parameter(description = "Account ID") @PathVariable Long accountId,
            @Parameter(description = "New balance amount") @RequestParam BigDecimal newBalance) {

        accountService.updateBalance(accountId, newBalance);
        return ResponseEntity.ok().build();
    }
}