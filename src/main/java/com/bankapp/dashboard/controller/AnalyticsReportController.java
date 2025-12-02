package com.bankapp.dashboard.controller;

import com.bankapp.dashboard.model.AnalyticsReport;
import com.bankapp.dashboard.service.AnalyticsReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsReportController {

    private final AnalyticsReportService analyticsReportService;

    @GetMapping("/health")
    public String health() {
        return "Analytics API is working!";
    }

    // Get all reports (mainly for testing)
    @GetMapping
    public List<AnalyticsReport> getAll() {
        return analyticsReportService.getAll();
    }

    // Create or save a report (you can precompute from transactions later)
    @PostMapping
    public AnalyticsReport create(@RequestBody AnalyticsReport report) {
        return analyticsReportService.create(report);
    }

    // Get analytics for a specific user (for dashboard)
    @GetMapping("/user/{userId}")
    public List<AnalyticsReport> getByUser(@PathVariable String userId) {
        return analyticsReportService.getByUserId(userId);
    }
}
