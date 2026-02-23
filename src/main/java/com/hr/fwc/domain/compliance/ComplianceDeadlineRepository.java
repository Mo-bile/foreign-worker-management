package com.hr.fwc.domain.compliance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ComplianceDeadlineRepository {
    ComplianceDeadline save(ComplianceDeadline deadline);

    Optional<ComplianceDeadline> findById(Long id);

    List<ComplianceDeadline> findByWorkerId(Long workerId);

    List<ComplianceDeadline> findByStatusIn(List<DeadlineStatus> statuses);

    List<ComplianceDeadline> findByDueDateBeforeAndStatusNot(LocalDate date, DeadlineStatus status);
}
