package com.bankapp.dashboard.controller;

import com.bankapp.dashboard.dto.ApiResponse;
import com.bankapp.dashboard.model.Accounts;
import com.bankapp.dashboard.model.AccountType;
import com.bankapp.dashboard.model.Users;
import com.bankapp.dashboard.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // ===== ADMIN endpoints =====

    // Get all accounts (system-wide)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Accounts>>> getAllAccounts() {
        List<Accounts> accounts = accountService.getAllAccounts();
        String message = accounts.isEmpty()
                ? "No accounts found"
                : "Accounts fetched successfully";
        ApiResponse<List<Accounts>> body = new ApiResponse<>(message, accounts);
        return ResponseEntity.ok(body);
    }

    // Get single account by ID
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Accounts>> getAccountById(@PathVariable String id) {
        Accounts account = accountService.getById(id);
        if (account == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("Account not found", null));
        }
        ApiResponse<Accounts> body = new ApiResponse<>("Account fetched successfully", account);
        return ResponseEntity.ok(body);
    }

    // Get available account types for dropdowns
    @GetMapping("/meta")
    public ResponseEntity<ApiResponse<AccountType[]>> getAccountMeta() {
        AccountType[] types = AccountType.values();
        return ResponseEntity.ok(
                new ApiResponse<>("Account types fetched successfully", types)
        );
    }
    // ===== CUSTOMER / ADMIN endpoints (current user) =====

    // Get all accounts for the current logged-in user
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<Accounts>>> getMyAccounts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = (Users) auth.getPrincipal();

        List<Accounts> accounts = accountService.getByUserId(currentUser.getId());
        String message = accounts.isEmpty()
                ? "No accounts found for current user"
                : "Accounts for current user fetched successfully";
        ApiResponse<List<Accounts>> body = new ApiResponse<>(message, accounts);
        return ResponseEntity.ok(body);
    }

}
