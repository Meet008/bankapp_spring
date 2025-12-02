package com.bankapp.dashboard.dto;

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
    private Double savings;
    private Double expenses;             // total debits (this month, or overall)
    private List<Transactions> recentTransactions;
}
