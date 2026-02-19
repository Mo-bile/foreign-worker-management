package com.hr.fwc.presentation;

import com.hr.fwc.application.service.ComplianceDashboardService;
import com.hr.fwc.domain.compliance.ComplianceDeadline;
import com.hr.fwc.presentation.api.ComplianceApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compliance")
public class ComplianceController implements ComplianceApi {

    private final ComplianceDashboardService dashboardService;

    public ComplianceController(ComplianceDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<ComplianceDeadline>> getOverdueDeadlines() {
        return ResponseEntity.ok(dashboardService.getOverdueDeadlines());
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<ComplianceDeadline>> getUpcomingDeadlines(
            @RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(dashboardService.getUpcomingDeadlines(days));
    }

    @GetMapping("/worker/{workerId}")
    public ResponseEntity<List<ComplianceDeadline>> getWorkerDeadlines(@PathVariable Long workerId) {
        return ResponseEntity.ok(dashboardService.getWorkerDeadlines(workerId));
    }

}
