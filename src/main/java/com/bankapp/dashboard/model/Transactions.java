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
        private String accountId; // reference to Account
        private String userId; // reference to User
        private String type; // CREDIT, DEBIT, TRANSFER, WITHDRAWAL
        private Double amount;
        private String category; // e.g., Shopping, Utilities
        private LocalDate date;
        private String description;
}
