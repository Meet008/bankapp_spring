package com.bankapp.dashboard.config;

import com.bankapp.dashboard.model.*;
import com.bankapp.dashboard.repository.UserRepository;
import com.bankapp.dashboard.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DemoDataLoader implements CommandLineRunner {

    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final PaymentService paymentService;
    private final AnalyticsReportService analyticsService;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        // if demo user already exists, do nothing
        if (userRepository.findByEmail("demo@bankapp.com") != null) {
            return;
        }

        // 1) Create demo customer user (password will be encoded in UserService)
        Users demoUser = new Users();
        demoUser.setName("Demo User");
        demoUser.setEmail("demo@bankapp.com");
        demoUser.setPassword("demo123");   // plain here; service will encode
        demoUser.setPhone("+1-416-555-0000");
        demoUser.setAddress("Demo Street, Toronto");
        demoUser.setAvatarUrl("https://example.com/demo.png");
        demoUser.setRole(Role.CUSTOMER);

        demoUser = userService.createUser(demoUser);

        String userId = demoUser.getId();

        // 2) Create three accounts
        Accounts savings = new Accounts(null, userId, "9134",
                AccountType.SAVINGS, 45200.0, AccountStatus.ACTIVE);
        Accounts checking = new Accounts(null, userId, "1120",
                AccountType.CHECKING, 12900.0, AccountStatus.ACTIVE);
        Accounts credit = new Accounts(null, userId, "5521",
                AccountType.CREDIT, -5000.0, AccountStatus.PENDING);

        savings = accountService.createAccount(savings);
        checking = accountService.createAccount(checking);
        credit = accountService.createAccount(credit);

        // 3) Create some transactions
        Transactions t1 = new Transactions(null, userId, savings.getId(),
                "CREDIT", 25000.0, "Salary",
                LocalDate.now().withDayOfMonth(1), "Salary Credit");

        Transactions t2 = new Transactions(null, userId, checking.getId(),
                "DEBIT", 1200.0, "Shopping",
                LocalDate.now().withDayOfMonth(2), "Amazon Shopping");

        Transactions t3 = new Transactions(null, userId, savings.getId(),
                "DEBIT", 900.0, "Bills",
                LocalDate.now().withDayOfMonth(3), "Electricity Bill");

        transactionService.create(t1);
        transactionService.create(t2);
        transactionService.create(t3);

        // 4) Create a couple of payments
        Payment p1 = new Payment(null, userId, "ELECTRICITY", 1200.0,
                LocalDate.now().withDayOfMonth(3), "PAID");
        Payment p2 = new Payment(null, userId, "MOBILE", 399.0,
                LocalDate.now().withDayOfMonth(5), "PENDING");

        paymentService.create(p1);
        paymentService.create(p2);

        // 5) Create analytics report
        AnalyticsReport report = new AnalyticsReport();
        report.setUserId(userId);
        report.setTotalSpending(18000.0);
        report.setHighestCategory("Shopping");
        report.setMonthlyAverage(14800.0);
        report.setCategoryWiseSpending(Map.of(
                "Shopping", 6200.0,
                "Bills", 3800.0,
                "Groceries", 4500.0,
                "Travel", 2200.0,
                "Other", 150.0
        ));
        report.setMonthlyTrend(List.of(
                new MonthlyTrend("Jan", 12000.0),
                new MonthlyTrend("Feb", 14000.0),
                new MonthlyTrend("Mar", 11000.0),
                new MonthlyTrend("Apr", 16000.0),
                new MonthlyTrend("May", 17500.0),
                new MonthlyTrend("Jun", 15000.0)
        ));

        analyticsService.create(report);
    }
}
