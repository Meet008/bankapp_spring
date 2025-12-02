package com.bankapp.dashboard.controller;

import com.bankapp.dashboard.dto.DashboardSummary;
import com.bankapp.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary/{userId}")
    public DashboardSummary getSummary(@PathVariable String userId) {
        return dashboardService.getSummaryForUser(userId);
    }
}
