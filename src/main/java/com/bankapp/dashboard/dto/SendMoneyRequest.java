package com.bankapp.dashboard.dto;

import lombok.Data;

@Data
public class SendMoneyRequest {
    private String fromAccountId;
    private String toAccountNumber;
    private Double amount;
    private String note;
}
