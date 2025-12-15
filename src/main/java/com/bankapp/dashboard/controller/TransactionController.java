package com.bankapp.dashboard.controller;

import com.bankapp.dashboard.dto.ApiResponse;
import com.bankapp.dashboard.model.Transactions;
import com.bankapp.dashboard.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // Health check
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        ApiResponse<String> body = new ApiResponse<>("Transactions API is working!", "OK");
        return ResponseEntity.ok(body);
    }

    // Get all transactions
    @GetMapping
    public ResponseEntity<ApiResponse<List<Transactions>>> getAll() {
        List<Transactions> list = transactionService.getAll();
        String message = list.isEmpty()
                ? "No transactions found"
                : "Transactions fetched successfully";
        ApiResponse<List<Transactions>> body = new ApiResponse<>(message, list);
        return ResponseEntity.ok(body);
    }

    // Create a new transaction
    @PostMapping
    public ResponseEntity<ApiResponse<Transactions>> create(@RequestBody Transactions tx) {
        Transactions created = transactionService.create(tx);
        ApiResponse<Transactions> body =
                new ApiResponse<>("Transaction created successfully", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // Get transactions for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Transactions>>> getByUser(@PathVariable String userId) {
        List<Transactions> list = transactionService.getByUserId(userId);
        String message = list.isEmpty()
                ? "No transactions found for user"
                : "User transactions fetched successfully";
        ApiResponse<List<Transactions>> body = new ApiResponse<>(message, list);
        return ResponseEntity.ok(body);
    }

    // Get transactions for an account
    @GetMapping("/account/{accountId}")
    public ResponseEntity<ApiResponse<List<Transactions>>> getByAccount(@PathVariable String accountId) {
        List<Transactions> list = transactionService.getByAccountId(accountId);
        String message = list.isEmpty()
                ? "No transactions found for account"
                : "Account transactions fetched successfully";
        ApiResponse<List<Transactions>> body = new ApiResponse<>(message, list);
        return ResponseEntity.ok(body);
    }

    // Get transactions by category
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<Transactions>>> getByCategory(@PathVariable String category) {
        List<Transactions> list = transactionService.getByCategory(category);
        String message = list.isEmpty()
                ? "No transactions found for category " + category
                : "Transactions for category " + category + " fetched successfully";
        ApiResponse<List<Transactions>> body = new ApiResponse<>(message, list);
        return ResponseEntity.ok(body);
    }

    // Get transactions between two dates (yyyy-MM-dd)
    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<List<Transactions>>> getByDateRange(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {

        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);
        List<Transactions> list = transactionService.getByDateRange(s, e);

        String message = list.isEmpty()
                ? "No transactions found in given date range"
                : "Transactions in date range fetched successfully";

        ApiResponse<List<Transactions>> body = new ApiResponse<>(message, list);
        return ResponseEntity.ok(body);
    }
}
