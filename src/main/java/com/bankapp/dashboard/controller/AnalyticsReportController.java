package com.bankapp.dashboard.controller;

import com.bankapp.dashboard.dto.ApiResponse;
import com.bankapp.dashboard.model.AnalyticsReport;
import com.bankapp.dashboard.service.AnalyticsReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsReportController {

    private final AnalyticsReportService analyticsReportService;

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        ApiResponse<String> body = new ApiResponse<>("Analytics API is working!", "OK");
        return ResponseEntity.ok(body);
    }

    // Get all reports (mainly for testing)
    @GetMapping
    public ResponseEntity<ApiResponse<List<AnalyticsReport>>> getAll() {
        List<AnalyticsReport> reports = analyticsReportService.getAll();
        String message = reports.isEmpty()
                ? "No analytics reports found"
                : "Analytics reports fetched successfully";
        ApiResponse<List<AnalyticsReport>> body = new ApiResponse<>(message, reports);
        return ResponseEntity.ok(body);
    }

    // Create or save a report
    @PostMapping
    public ResponseEntity<ApiResponse<AnalyticsReport>> create(@RequestBody AnalyticsReport report) {
        AnalyticsReport created = analyticsReportService.create(report);
        ApiResponse<AnalyticsReport> body =
                new ApiResponse<>("Analytics report created successfully", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // Get analytics for a specific user (for dashboard)
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<AnalyticsReport>>> getByUser(@PathVariable String userId) {
        List<AnalyticsReport> reports = analyticsReportService.getByUserId(userId);
        String message = reports.isEmpty()
                ? "No analytics reports found for user"
                : "User analytics reports fetched successfully";
        ApiResponse<List<AnalyticsReport>> body = new ApiResponse<>(message, reports);
        return ResponseEntity.ok(body);
    }
}
