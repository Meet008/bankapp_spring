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
    private final AccountService accountService;   // used to create default accounts

    // ===== READ =====

    public List<Users> getAllUsers() {
        return userRepository.findAll();
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

    // ===== CREATE =====

    // Simple create – kept for internal/legacy usage if needed
    public Users createUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(Role.CUSTOMER);
        }
        return userRepository.save(user);
    }

    // Main method: admin create or registration, with default accounts for CUSTOMER
    public Users createUserWithDefaults(Users user) {
        // encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // default role if not set
        if (user.getRole() == null) {
            user.setRole(Role.CUSTOMER);
        }

        // save user
        Users saved = userRepository.save(user);

        // if CUSTOMER, create default accounts + welcome transaction
        if (saved.getRole() == Role.CUSTOMER) {
            accountService.createDefaultAccountsForUser(saved.getId());
        }

        return saved;
    }

    // ===== UPDATE =====

    public Users updateUser(String id, Users updated) {
        return userRepository.findById(id)
                .map(existing -> {
                    if (updated.getName() != null) {
                        existing.setName(updated.getName());
                    }
                    if (updated.getPhone() != null) {
                        existing.setPhone(updated.getPhone());
                    }
                    if (updated.getAddress() != null) {
                        existing.setAddress(updated.getAddress());
                    }
                    if (updated.getAvatarUrl() != null) {
                        existing.setAvatarUrl(updated.getAvatarUrl());
                    }
                    // do NOT update password or role here
                    return userRepository.save(existing);
                })
                .orElse(null);
    }

    // ===== DELETE =====

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

        // Extra safety: transactions by accountId too
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

    // Delete all users with a given email, returns count
    public long deleteAllByEmail(String email) {
        return userRepository.deleteByEmail(email);
    }
}
