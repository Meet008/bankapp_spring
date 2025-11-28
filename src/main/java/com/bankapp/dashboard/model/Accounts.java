package com.bankapp.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "accounts")
public class Accounts {
    @Id
    private String id;
    private String accountNumber;
    private AccountType type; // e.g. SAVINGS, CHECKING, CREDIT
    private Double balance;
    private String userId; // reference to User
    private AccountStatus status; // ACTIVE, PENDING
}
