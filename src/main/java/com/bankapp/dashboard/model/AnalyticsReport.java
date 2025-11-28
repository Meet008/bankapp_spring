package com.bankapp.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "analytics_reports")
public class AnalyticsReport {
    @Id
    private String id;
    private String userId;
    private Double totalSpending;
    private String highestCategory;
    private Double monthlyAverage;
    private Map<String, Double> categoryWiseSpending; // e.g. {"Shopping": 5000}
    private List<MonthlyTrend> monthlyTrend;
}


