package com.bankapp.dashboard.service;

import com.bankapp.dashboard.model.Accounts;
import com.bankapp.dashboard.model.AccountType;
import com.bankapp.dashboard.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

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
}
