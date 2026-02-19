package com.hr.fwc.domain.compliance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ComplianceDeadlineRepository extends JpaRepository<ComplianceDeadline, Long> {

    List<ComplianceDeadline> findByWorkerId(Long workerId);

    List<ComplianceDeadline> findByStatusIn(List<DeadlineStatus> statuses);

    List<ComplianceDeadline> findByDueDateBeforeAndStatusNot(LocalDate date, DeadlineStatus status);

}
