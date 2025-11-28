package com.bankapp.dashboard.controller;

import com.bankapp.dashboard.model.Accounts;
import com.bankapp.dashboard.model.AccountType;
import com.bankapp.dashboard.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // Simple health check
    @GetMapping("/health")
    public String health() {
        return "Accounts API is working!";
    }

    // Get all accounts
    @GetMapping
    public List<Accounts> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    // Get single account by ID
    @GetMapping("/{id}")
    public Accounts getAccountById(@PathVariable String id) {
        return accountService.getById(id);
    }

    // Get all accounts for a user
    @GetMapping("/user/{userId}")
    public List<Accounts> getAccountsByUser(@PathVariable String userId) {
        return accountService.getByUserId(userId);
    }

    // Get accounts for a user filtered by type (SAVINGS/CHECKING/CREDIT)
    @GetMapping("/user/{userId}/type/{type}")
    public List<Accounts> getAccountsByUserAndType(@PathVariable String userId,
                                                   @PathVariable AccountType type) {
        return accountService.getByUserIdAndType(userId, type);
    }

    // Create a new account
    @PostMapping
    public Accounts createAccount(@RequestBody Accounts account) {
        return accountService.createAccount(account);
    }

    // Delete an account by ID
    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable String id) {
        accountService.deleteById(id);
    }
}
