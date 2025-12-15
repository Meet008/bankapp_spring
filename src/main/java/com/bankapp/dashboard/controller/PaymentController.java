package com.bankapp.dashboard.controller;

import com.bankapp.dashboard.dto.ApiResponse;
import com.bankapp.dashboard.model.Payment;
import com.bankapp.dashboard.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // Health check
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        ApiResponse<String> body = new ApiResponse<>("Payments API is working!", "OK");
        return ResponseEntity.ok(body);
    }

    // Get all payments
    @GetMapping
    public ResponseEntity<ApiResponse<List<Payment>>> getAll() {
        List<Payment> payments = paymentService.getAll();
        String message = payments.isEmpty()
                ? "No payments found"
                : "Payments fetched successfully";
        ApiResponse<List<Payment>> body = new ApiResponse<>(message, payments);
        return ResponseEntity.ok(body);
    }

    // Create a new payment / schedule a payment
    @PostMapping
    public ResponseEntity<ApiResponse<Payment>> create(@RequestBody Payment payment) {
        Payment created = paymentService.create(payment);
        ApiResponse<Payment> body = new ApiResponse<>("Payment created successfully", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // Get payments for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Payment>>> getByUser(@PathVariable String userId) {
        List<Payment> payments = paymentService.getByUserId(userId);
        String message = payments.isEmpty()
                ? "No payments found for user"
                : "User payments fetched successfully";
        ApiResponse<List<Payment>> body = new ApiResponse<>(message, payments);
        return ResponseEntity.ok(body);
    }

    // Get payments by bill type
    @GetMapping("/bill-type/{billType}")
    public ResponseEntity<ApiResponse<List<Payment>>> getByBillType(@PathVariable String billType) {
        List<Payment> payments = paymentService.getByBillType(billType);
        String message = payments.isEmpty()
                ? "No payments found for bill type " + billType
                : "Payments for bill type " + billType + " fetched successfully";
        ApiResponse<List<Payment>> body = new ApiResponse<>(message, payments);
        return ResponseEntity.ok(body);
    }

    // Get payments by status
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<Payment>>> getByStatus(@PathVariable String status) {
        List<Payment> payments = paymentService.getByStatus(status);
        String message = payments.isEmpty()
                ? "No payments found with status " + status
                : "Payments with status " + status + " fetched successfully";
        ApiResponse<List<Payment>> body = new ApiResponse<>(message, payments);
        return ResponseEntity.ok(body);
    }

    // Delete a payment by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        paymentService.deleteById(id); // let service throw ResourceNotFoundException if missing
        ApiResponse<Void> body = new ApiResponse<>("Payment deleted successfully", null);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(body);
    }
}
