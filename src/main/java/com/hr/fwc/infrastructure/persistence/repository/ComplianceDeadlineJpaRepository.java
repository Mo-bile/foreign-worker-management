package com.hr.fwc.infrastructure.persistence.repository;

import com.hr.fwc.domain.compliance.DeadlineStatus;
import com.hr.fwc.infrastructure.persistence.entity.ComplianceDeadlineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ComplianceDeadlineJpaRepository extends JpaRepository<ComplianceDeadlineEntity, Long> {
    List<ComplianceDeadlineEntity> findByWorkerId(Long workerId);

    List<ComplianceDeadlineEntity> findByStatusIn(List<DeadlineStatus> statuses);

    List<ComplianceDeadlineEntity> findByDueDateBeforeAndStatusNot(LocalDate date, DeadlineStatus status);
}
