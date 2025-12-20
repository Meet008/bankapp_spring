package com.bankapp.dashboard.service;

import com.bankapp.dashboard.model.AccountStatus;
import com.bankapp.dashboard.model.AccountType;
import com.bankapp.dashboard.model.Accounts;
import com.bankapp.dashboard.model.TransactionCategory;
import com.bankapp.dashboard.model.TransactionType;
import com.bankapp.dashboard.model.Transactions;
import com.bankapp.dashboard.repository.AccountRepository;
import com.bankapp.dashboard.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    // ===== basic CRUD =====

    public List<Accounts> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Accounts createAccount(Accounts account) {
        return accountRepository.save(account);
    }

    public Accounts getById(String id) {
        return accountRepository.findById(id).orElse(null);
    }

    public List<Accounts> getByUserId(String userId) {
        return accountRepository.findByUserId(userId);
    }

    public List<Accounts> getByUserIdAndType(String userId, AccountType type) {
        return accountRepository.findByUserIdAndType(userId, type);
    }

    public void deleteById(String id) {
        accountRepository.deleteById(id);
    }

    private String generateAccountNumber() {
        // simple 10‑digit random number as String
        long num = (long) (Math.random() * 1_000_000_0000L);
        return String.format("%010d", num);
    }

    // ===== default accounts for new CUSTOMER =====

    public void createDefaultAccountsForUser(String userId) {
        // CHECKING
        List<Accounts> existingChecking = accountRepository.findByUserIdAndType(userId, AccountType.CHECKING);
        Accounts checking;
        if (existingChecking.isEmpty()) {
            checking = new Accounts();
            checking.setUserId(userId);
            checking.setType(AccountType.CHECKING);
            checking.setAccountNumber(generateAccountNumber());  // <-- add this
            checking.setBalance(25.00);
            checking.setStatus(AccountStatus.ACTIVE);
            checking = accountRepository.save(checking);
            // welcome transaction...
        } else {
            checking = existingChecking.get(0);
        }

        // SAVINGS
        List<Accounts> existingSavings = accountRepository.findByUserIdAndType(userId, AccountType.SAVINGS);
        if (existingSavings.isEmpty()) {
            Accounts savings = new Accounts();
            savings.setUserId(userId);
            savings.setType(AccountType.SAVINGS);
            savings.setAccountNumber(generateAccountNumber());   // <-- and here
            savings.setBalance(0.00);
            savings.setStatus(AccountStatus.ACTIVE);
            accountRepository.save(savings);
        }
    }
}
