package com.bankapp.dashboard.controller;

import com.bankapp.dashboard.model.Transactions;
import com.bankapp.dashboard.service.TransactionService;
import lombok.RequiredArgsConstructor;
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
    public String health() {
        return "Transactions API is working!";
    }

    // Get all transactions
    @GetMapping
    public List<Transactions> getAll() {
        return transactionService.getAll();
    }

    // Create a new transaction
    @PostMapping
    public Transactions create(@RequestBody Transactions tx) {
        return transactionService.create(tx);
    }

    // Get transactions for a user
    @GetMapping("/user/{userId}")
    public List<Transactions> getByUser(@PathVariable String userId) {
        return transactionService.getByUserId(userId);
    }

    // Get transactions for an account
    @GetMapping("/account/{accountId}")
    public List<Transactions> getByAccount(@PathVariable String accountId) {
        return transactionService.getByAccountId(accountId);
    }

    // Get transactions by category
    @GetMapping("/category/{category}")
    public List<Transactions> getByCategory(@PathVariable String category) {
        return transactionService.getByCategory(category);
    }

    // Get transactions between two dates (yyyy-MM-dd)
    @GetMapping("/date-range")
    public List<Transactions> getByDateRange(@RequestParam("start") String start,
                                             @RequestParam("end") String end) {
        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);
        return transactionService.getByDateRange(s, e);
    }
}
