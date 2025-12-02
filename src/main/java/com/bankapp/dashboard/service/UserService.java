package com.bankapp.dashboard.service;

import com.bankapp.dashboard.model.*;
import com.bankapp.dashboard.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PaymentRepository paymentRepository;
    private final AnalyticsReportRepository analyticsReportRepository;

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users createUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Users findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<Users> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public Users getById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    // Cascade delete: user + all related data
    public boolean deleteUserById(String id) {
        Users user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return false;
        }

        String userId = user.getId();

        // 1) Accounts
        List<Accounts> accounts = accountRepository.findByUserId(userId);
        List<String> accountIds = accounts.stream()
                .map(Accounts::getId)
                .toList();
        accountRepository.deleteAll(accounts);

        // 2) Transactions by userId
        transactionRepository.deleteAll(
                transactionRepository.findByUserId(userId)
        );

        // Extra safety: delete by accountId too
        for (String accId : accountIds) {
            transactionRepository.deleteAll(
                    transactionRepository.findByAccountId(accId)
            );
        }

        // 3) Payments
        paymentRepository.deleteAll(
                paymentRepository.findByUserId(userId)
        );

        // 4) Analytics reports
        analyticsReportRepository.deleteAll(
                analyticsReportRepository.findByUserId(userId)
        );

        // 5) Finally delete user
        userRepository.deleteById(id);
        return true;
    }

    public Users updateUser(String id, Users updated) {
        return userRepository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setPhone(updated.getPhone());
                    existing.setAddress(updated.getAddress());
                    existing.setAvatarUrl(updated.getAvatarUrl());
                    return userRepository.save(existing);
                })
                .orElse(null);
    }

    // Delete all users with a given email, returns count
    public long deleteAllByEmail(String email) {
        return userRepository.deleteByEmail(email);
    }
}
