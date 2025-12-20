package com.bankapp.dashboard.config;

import com.bankapp.dashboard.model.*;
import com.bankapp.dashboard.repository.AccountRepository;
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
    private final AccountRepository accountRepository;

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

        // This will also create one CHECKING + one SAVINGS + welcome transaction
        demoUser = userService.createUserWithDefaults(demoUser);

        String userId = demoUser.getId();

        // 2) Fetch the default accounts created for this user
        Accounts checking = accountRepository
                .findByUserIdAndType(userId, AccountType.CHECKING)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Default checking account not found"));

        Accounts savings = accountRepository
                .findByUserIdAndType(userId, AccountType.SAVINGS)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Default savings account not found"));

        // 3) Optional: add a few extra demo transactions using enums

        Transactions salaryCredit = new Transactions(
                null,
                checking.getId(),
                userId,
                TransactionType.CREDIT,
                25000.0,
                TransactionCategory.ADD_MONEY,
                null, // billType
                LocalDate.now().withDayOfMonth(1),
                "Salary Credit"
        );

        Transactions electricityBillTx = new Transactions(
                null,
                checking.getId(),
                userId,
                TransactionType.DEBIT,
                1200.0,
                TransactionCategory.PAY_BILL,
                BillType.ELECTRICITY,
                LocalDate.now().withDayOfMonth(3),
                "Electricity Bill"
        );

        Transactions mobileRechargeTx = new Transactions(
                null,
                savings.getId(),
                userId,
                TransactionType.DEBIT,
                399.0,
                TransactionCategory.PAY_BILL,
                BillType.MOBILE_RECHARGE,
                LocalDate.now().withDayOfMonth(5),
                "Mobile Recharge"
        );

        // Use repository directly or add a create method in TransactionService if you prefer
        // transactionService.getAll() etc. – assuming TransactionRepository.save(...) is used there
        // If you want, inject TransactionRepository instead and call save() directly.

        // 4) Create a couple of payments (used for Recent Payments section)
        Payment p1 = new Payment(
                null,
                userId,
                "ELECTRICITY",
                1200.0,
                LocalDate.now().withDayOfMonth(3),
                "PAID"
        );

        Payment p2 = new Payment(
                null,
                userId,
                "MOBILE_RECHARGE",
                399.0,
                LocalDate.now().withDayOfMonth(5),
                "PENDING"
        );

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
