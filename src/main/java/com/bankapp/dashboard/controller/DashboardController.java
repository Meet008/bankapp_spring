package com.bankapp.dashboard.controller;

import com.bankapp.dashboard.dto.ApiResponse;
import com.bankapp.dashboard.dto.DashboardSummary;
import com.bankapp.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary/{userId}")
    public ResponseEntity<ApiResponse<DashboardSummary>> getSummary(@PathVariable String userId) {
        DashboardSummary summary = dashboardService.getSummaryForUser(userId);

        String message = (summary == null)
                ? "No dashboard data found for user"
                : "Dashboard summary fetched successfully";

        ApiResponse<DashboardSummary> body = new ApiResponse<>(message, summary);

        return ResponseEntity.ok(body);
    }
}
