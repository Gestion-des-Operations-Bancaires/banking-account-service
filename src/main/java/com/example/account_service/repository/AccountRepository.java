package com.example.account_service.repository;

import com.example.account_service.entity.Account;
import com.example.account_service.entity.AccountStatus;
import com.example.account_service.entity.AccountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Optional<Account> findByAccountNumber(String accountNumber);
    
    List<Account> findByCustomerId(Long customerId);
    
    List<Account> findByCustomerIdAndStatus(Long customerId, AccountStatus status);
    
    Page<Account> findByCustomerId(Long customerId, Pageable pageable);
    
    List<Account> findByAccountType(AccountType accountType);
    
    List<Account> findByStatus(AccountStatus status);
    
    @Query("SELECT a FROM Account a WHERE a.balance < :threshold")
    List<Account> findAccountsWithLowBalance(@Param("threshold") BigDecimal threshold);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.customerId = :customerId")
    long countAccountsByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT SUM(a.balance) FROM Account a WHERE a.customerId = :customerId AND a.status = 'ACTIVE'")
    BigDecimal getTotalBalanceByCustomerId(@Param("customerId") Long customerId);
    
    boolean existsByAccountNumber(String accountNumber);
}