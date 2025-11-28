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
@Document(collection = "payments")
public class Payment {

        @Id
        private String id;
        private String userId;
        private String billType; // ELECTRICITY, WATER, etc
        private Double amount;
        private LocalDate scheduledDate;
        private String status; // PAID, PENDING
    }


