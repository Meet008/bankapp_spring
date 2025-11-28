package com.bankapp.dashboard.service;

import com.bankapp.dashboard.model.Transactions;
import com.bankapp.dashboard.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<Transactions> getAll() {
        return transactionRepository.findAll();
    }

    public Transactions create(Transactions tx) {
        return transactionRepository.save(tx);
    }

    public List<Transactions> getByUserId(String userId) {
        return transactionRepository.findByUserId(userId);
    }

    public List<Transactions> getByAccountId(String accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public List<Transactions> getByCategory(String category) {
        return transactionRepository.findByCategory(category);
    }

    public List<Transactions> getByDateRange(LocalDate start, LocalDate end) {
        return transactionRepository.findByDateBetween(start, end);
    }
}
