package com.hr.fwc.presentation;

import com.hr.fwc.application.service.ComplianceDashboardService;
import com.hr.fwc.application.dto.ComplianceDeadlineResponse;
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
    public ResponseEntity<List<ComplianceDeadlineResponse>> getOverdueDeadlines() {
        List<ComplianceDeadlineResponse> responses = dashboardService.getOverdueDeadlines().stream()
            .map(ComplianceDeadlineResponse::from)
            .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<ComplianceDeadlineResponse>> getUpcomingDeadlines(
            @RequestParam(defaultValue = "30") int days) {
        List<ComplianceDeadlineResponse> responses = dashboardService.getUpcomingDeadlines(days).stream()
            .map(ComplianceDeadlineResponse::from)
            .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/worker/{workerId}")
    public ResponseEntity<List<ComplianceDeadlineResponse>> getWorkerDeadlines(@PathVariable Long workerId) {
        List<ComplianceDeadlineResponse> responses = dashboardService.getWorkerDeadlines(workerId).stream()
            .map(ComplianceDeadlineResponse::from)
            .toList();
        return ResponseEntity.ok(responses);
    }

}
