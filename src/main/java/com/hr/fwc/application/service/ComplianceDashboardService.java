package com.hr.fwc.application.service;

import com.hr.fwc.application.dto.ComplianceDeadlineResponse;
import com.hr.fwc.domain.compliance.ComplianceDeadlineRepository;
import com.hr.fwc.domain.compliance.DeadlineStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ComplianceDashboardService {

    private final ComplianceDeadlineRepository deadlineRepository;

    public ComplianceDashboardService(ComplianceDeadlineRepository deadlineRepository) {
        this.deadlineRepository = deadlineRepository;
    }

    public List<ComplianceDeadlineResponse> getOverdueDeadlines() {
        return deadlineRepository.findByStatusIn(List.of(DeadlineStatus.OVERDUE))
            .stream().map(ComplianceDeadlineResponse::from).toList();
    }

    public List<ComplianceDeadlineResponse> getUpcomingDeadlines(int days) {
        return deadlineRepository.findByDueDateBeforeAndStatusNot(
            LocalDate.now().plusDays(days),
            DeadlineStatus.COMPLETED
        ).stream().map(ComplianceDeadlineResponse::from).toList();
    }

    public List<ComplianceDeadlineResponse> getWorkerDeadlines(Long workerId) {
        return deadlineRepository.findByWorkerId(workerId)
            .stream().map(ComplianceDeadlineResponse::from).toList();
    }

}
