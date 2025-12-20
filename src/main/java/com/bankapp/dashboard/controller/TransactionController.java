package com.bankapp.dashboard.controller;

import com.bankapp.dashboard.dto.AddMoneyRequest;
import com.bankapp.dashboard.dto.ApiResponse;
import com.bankapp.dashboard.dto.PayBillRequest;
import com.bankapp.dashboard.dto.SendMoneyRequest;
import com.bankapp.dashboard.model.*;
import com.bankapp.dashboard.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // send enums to frontend for dropdowns
    @GetMapping("/meta")
    public ResponseEntity<ApiResponse<Map<String, Object>>> meta() {
        Map<String, Object> data = Map.of(
                "types", TransactionType.values(),
                "categories", TransactionCategory.values()
        );
        return ResponseEntity.ok(new ApiResponse<>("Transaction meta", data));
    }

    // list current user's transactions with filters (for your screen)
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<Transactions>>> getMyTransactions(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) TransactionCategory category,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users current = (Users) auth.getPrincipal();

        List<Transactions> list = transactionService.searchForUser(
                current.getId(), date, category, minAmount, maxAmount
        );

        String msg = list.isEmpty() ? "No transactions found" : "Transactions fetched successfully";
        return ResponseEntity.ok(new ApiResponse<>(msg, list));
    }

    // ===== commands =====

    @PostMapping("/add-money")
    public ResponseEntity<ApiResponse<Transactions>> addMoney(@RequestBody AddMoneyRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users current = (Users) auth.getPrincipal();

        try {
            Transactions tx = transactionService.addMoney(current.getId(), req);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Money added successfully", tx));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Server Error", null));
        }
    }

    @PostMapping("/send-money")
    public ResponseEntity<ApiResponse<Transactions>> sendMoney(@RequestBody SendMoneyRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users current = (Users) auth.getPrincipal();

        try {
            Transactions tx = transactionService.sendMoney(current.getId(), req);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Money sent successfully", tx));
        } catch (IllegalArgumentException ex) {
            // for things like insufficient balance, invalid recipient, etc.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ex.getMessage(), null));
        } catch (Exception ex) {
            // optional: log ex
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Server Error", null));
        }
    }

    @PostMapping("/pay-bill")
    public ResponseEntity<ApiResponse<Transactions>> payBill(@RequestBody PayBillRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users current = (Users) auth.getPrincipal();

        try {
            Transactions tx = transactionService.payBill(current.getId(), req);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Bill paid successfully", tx));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Server Error", null));
        }
    }
}
