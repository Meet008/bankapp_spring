package com.bankapp.dashboard.service;

import com.bankapp.dashboard.dto.DashboardSummary;
import com.bankapp.dashboard.model.AccountType;
import com.bankapp.dashboard.model.Accounts;
import com.bankapp.dashboard.model.TransactionType;
import com.bankapp.dashboard.model.Transactions;
import com.bankapp.dashboard.repository.AccountRepository;
import com.bankapp.dashboard.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public DashboardSummary getSummaryForUser(String userId) {

        // All accounts for user
        List<Accounts> accounts = accountRepository.findByUserId(userId);

        double totalBalance = accounts.stream()
                .mapToDouble(a -> a.getBalance() != null ? a.getBalance() : 0.0)
                .sum();

        double savings = accounts.stream()
                .filter(a -> a.getType() == AccountType.SAVINGS)
                .mapToDouble(a -> a.getBalance() != null ? a.getBalance() : 0.0)
                .sum();

        // Expenses: sum of DEBIT transactions for current month
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now();

        List<Transactions> monthTx =
                transactionRepository.findByDateBetween(startOfMonth, endOfMonth);

        double expenses = monthTx.stream()
                .filter(tx -> userId.equals(tx.getUserId()))
                .filter(tx -> tx.getType() == TransactionType.DEBIT)
                .mapToDouble(tx -> tx.getAmount() != null ? tx.getAmount() : 0.0)
                .sum();

        // Recent transactions: latest 5 for this user
        List<Transactions> recent = transactionRepository.findByUserId(userId).stream()
                .sorted(Comparator.comparing(Transactions::getDate).reversed())
                .limit(5)
                .toList();

        return new DashboardSummary(totalBalance, savings, expenses, recent);
    }
}
