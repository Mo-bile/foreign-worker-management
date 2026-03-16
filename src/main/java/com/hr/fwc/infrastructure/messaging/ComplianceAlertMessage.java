package com.hr.fwc.infrastructure.messaging;

import com.hr.fwc.domain.compliance.DeadlineStatus;
import com.hr.fwc.domain.compliance.DeadlineType;
import java.time.LocalDate;
import java.util.UUID;

public record ComplianceAlertMessage(
    String messageId,
    Long workerId,
    Long deadlineId,
    DeadlineType deadlineType,
    DeadlineStatus status,
    LocalDate dueDate,
    String description
) {
    public static ComplianceAlertMessage from(
        Long deadlineId,
        Long workerId,
        DeadlineType deadlineType,
        DeadlineStatus status,
        LocalDate dueDate,
        String description
    ) {
        return new ComplianceAlertMessage(UUID.randomUUID().toString(), workerId, deadlineId, deadlineType, status, dueDate, description);
    }

}
