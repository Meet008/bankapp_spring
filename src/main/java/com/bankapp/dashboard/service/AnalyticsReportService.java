package com.bankapp.dashboard.service;

import com.bankapp.dashboard.model.AnalyticsReport;
import com.bankapp.dashboard.repository.AnalyticsReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsReportService {

    private final AnalyticsReportRepository analyticsReportRepository;

    public List<AnalyticsReport> getAll() {
        return analyticsReportRepository.findAll();
    }

    public AnalyticsReport create(AnalyticsReport report) {
        return analyticsReportRepository.save(report);
    }

    public List<AnalyticsReport> getByUserId(String userId) {
        return analyticsReportRepository.findByUserId(userId);
    }
}
