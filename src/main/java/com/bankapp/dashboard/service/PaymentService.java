package com.bankapp.dashboard.service;

import com.bankapp.dashboard.model.Payment;
import com.bankapp.dashboard.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    public Payment create(Payment payment) {
        return paymentRepository.save(payment);
    }

    public List<Payment> getByUserId(String userId) {
        return paymentRepository.findByUserId(userId);
    }

    public List<Payment> getByBillType(String billType) {
        return paymentRepository.findByBillType(billType);
    }

    public List<Payment> getByStatus(String status) {
        return paymentRepository.findByStatus(status);
    }

    public void deleteById(String id) {
        paymentRepository.deleteById(id);
    }
}
