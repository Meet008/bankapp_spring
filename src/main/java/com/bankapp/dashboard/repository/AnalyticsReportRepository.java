package com.bankapp.dashboard.repository;

import com.bankapp.dashboard.model.AnalyticsReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface AnalyticsReportRepository extends MongoRepository<AnalyticsReport, String> {
    List<AnalyticsReport> findByUserId(String userId);
}
