package com.bankapp.dashboard.dto;

import lombok.Data;

@Data
public class PayBillRequest {
    private String fromAccountId;
    private String billerCode;
    private Double amount;
    private String note;
}
