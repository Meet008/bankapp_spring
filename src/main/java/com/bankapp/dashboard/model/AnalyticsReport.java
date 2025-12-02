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
    private String userId;  // which user's dashboard
    private Double totalSpending; //this month total
    private String highestCategory; //e.g"Shopping"
    private Double monthlyAverage; //avg spend
    // category -> amount (for pie chart)
    private Map<String, Double> categoryWiseSpending; // e.g. {"Shopping": 5000}
    // list for line chart: month vs amount
    private List<MonthlyTrend> monthlyTrend;
}


