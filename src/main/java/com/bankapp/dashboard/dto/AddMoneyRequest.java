package com.bankapp.dashboard.dto;

import lombok.Data;

@Data
public class AddMoneyRequest {
    private String accountId;
    private Double amount;
    private String note;
}
