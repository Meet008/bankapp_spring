package com.bankapp.dashboard.controller;

import com.bankapp.dashboard.model.Payment;
import com.bankapp.dashboard.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // Health check
    @GetMapping("/health")
    public String health() {
        return "Payments API is working!";
    }

    // Get all payments
    @GetMapping
    public List<Payment> getAll() {
        return paymentService.getAll();
    }

    // Create a new payment / schedule a payment
    @PostMapping
    public Payment create(@RequestBody Payment payment) {
        return paymentService.create(payment);
    }

    // Get payments for a user
    @GetMapping("/user/{userId}")
    public List<Payment> getByUser(@PathVariable String userId) {
        return paymentService.getByUserId(userId);
    }

    // Get payments by bill type (ELECTRICITY, WATER, MOBILE, INTERNET, CREDIT_CARD, etc.)
    @GetMapping("/bill-type/{billType}")
    public List<Payment> getByBillType(@PathVariable String billType) {
        return paymentService.getByBillType(billType);
    }

    // Get payments by status (PENDING, PAID, FAILED)
    @GetMapping("/status/{status}")
    public List<Payment> getByStatus(@PathVariable String status) {
        return paymentService.getByStatus(status);
    }

    // Delete a payment by ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        paymentService.deleteById(id);
    }
}
