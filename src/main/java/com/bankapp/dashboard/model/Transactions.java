package com.bankapp.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class Transactions {

    @Id
    private String id;

    private String accountId;  // reference to Account
    private String userId;     // reference to User

    // CREDIT or DEBIT
    private TransactionType type;

    private Double amount;

    // ADD_MONEY, SEND_MONEY, PAY_BILL, etc.
    private TransactionCategory category;
    private BillType billType;  // ELECTRICITY, WATER, etc. (nullable)

    private LocalDate date;
    private String description;
}
