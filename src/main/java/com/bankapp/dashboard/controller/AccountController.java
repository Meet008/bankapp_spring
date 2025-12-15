package com.bankapp.dashboard.controller;

import com.bankapp.dashboard.dto.ApiResponse;
import com.bankapp.dashboard.model.Accounts;
import com.bankapp.dashboard.model.AccountType;
import com.bankapp.dashboard.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // Simple health check
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        ApiResponse<String> body = new ApiResponse<>("Accounts API is working!", "OK");
        return ResponseEntity.ok(body);
    }

    // Get all accounts
    @GetMapping
    public ResponseEntity<ApiResponse<List<Accounts>>> getAllAccounts() {
        List<Accounts> accounts = accountService.getAllAccounts();
        String message = accounts.isEmpty() ? "No accounts found" : "Accounts fetched successfully";
        ApiResponse<List<Accounts>> body = new ApiResponse<>(message, accounts);
        return ResponseEntity.ok(body);
    }

    // Get single account by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Accounts>> getAccountById(@PathVariable String id) {
        Accounts account = accountService.getById(id); // throw ResourceNotFoundException inside service if null
        ApiResponse<Accounts> body = new ApiResponse<>("Account fetched successfully", account);
        return ResponseEntity.ok(body);
    }

    // Get all accounts for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Accounts>>> getAccountsByUser(@PathVariable String userId) {
        List<Accounts> accounts = accountService.getByUserId(userId);
        String message = accounts.isEmpty() ? "No accounts found for user" : "User accounts fetched successfully";
        ApiResponse<List<Accounts>> body = new ApiResponse<>(message, accounts);
        return ResponseEntity.ok(body);
    }

    // Get accounts for a user filtered by type
    @GetMapping("/user/{userId}/type/{type}")
    public ResponseEntity<ApiResponse<List<Accounts>>> getAccountsByUserAndType(
            @PathVariable String userId,
            @PathVariable AccountType type) {

        List<Accounts> accounts = accountService.getByUserIdAndType(userId, type);
        String message = accounts.isEmpty()
                ? "No accounts found for user with type " + type
                : "User accounts of type " + type + " fetched successfully";
        ApiResponse<List<Accounts>> body = new ApiResponse<>(message, accounts);
        return ResponseEntity.ok(body);
    }

    // Create a new account
    @PostMapping
    public ResponseEntity<ApiResponse<Accounts>> createAccount(@RequestBody Accounts account) {
        Accounts created = accountService.createAccount(account);
        ApiResponse<Accounts> body = new ApiResponse<>("Account created successfully", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // Delete an account by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(@PathVariable String id) {
        accountService.deleteById(id); // service throws ResourceNotFoundException if not present
        ApiResponse<Void> body = new ApiResponse<>("Account deleted successfully", null);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(body);
    }
}
