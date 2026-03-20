package com.hr.fwc.application.dto;

import com.hr.fwc.domain.compliance.ComplianceDeadline;

public record ComplianceDeadlineResponse(
    Long id,
    Long workerId,
    String deadlineType,
    String dueDate,
    String status,
    String description
) {

    public static ComplianceDeadlineResponse from(ComplianceDeadline deadline) {
        return new ComplianceDeadlineResponse(
            deadline.id(),
            deadline.workerId(),
            deadline.deadlineType().name(),
            deadline.dueDate().toString(),
            deadline.status().name(),
            deadline.description()
        );
    }
}
