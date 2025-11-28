package com.bankapp.dashboard.repository;

import com.bankapp.dashboard.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PaymentRepository extends MongoRepository<Payment, String> {
    List<Payment> findByUserId(String userId);
    List<Payment> findByBillType(String billType);
    List<Payment> findByStatus(String status);
}
