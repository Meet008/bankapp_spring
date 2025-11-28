package com.bankapp.dashboard.repository;

import com.bankapp.dashboard.model.Transactions;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends MongoRepository<Transactions, String> {
    List<Transactions> findByUserId(String userId);
    List<Transactions> findByAccountId(String accountId);
    List<Transactions> findByCategory(String category);
    List<Transactions> findByDateBetween(LocalDate start, LocalDate end);
}
