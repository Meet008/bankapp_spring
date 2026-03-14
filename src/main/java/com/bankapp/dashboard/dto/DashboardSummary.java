package com.bankapp.dashboard.dto;

import com.bankapp.dashboard.model.Accounts;
import com.bankapp.dashboard.model.Transactions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardSummary {
    private Double totalBalance;
    private Double chequing;
    private Double savings;
    private Double expenses;
    private List<Accounts> accounts;
    private List<Transactions> recentTransactions;
}
