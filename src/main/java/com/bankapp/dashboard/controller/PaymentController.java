package com.bankapp.dashboard.controller;

import com.bankapp.dashboard.dto.ApiResponse;
import com.bankapp.dashboard.model.BillType;
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

    @GetMapping("/meta")
    public ResponseEntity<ApiResponse<BillType[]>> getBillTypes() {
        return ResponseEntity.ok(
                new ApiResponse<>("Bill types fetched successfully", BillType.values())
        );
    }
}
