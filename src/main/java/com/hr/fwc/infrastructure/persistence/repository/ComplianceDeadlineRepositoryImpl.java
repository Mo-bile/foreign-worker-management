package com.hr.fwc.infrastructure.persistence.repository;

import com.hr.fwc.domain.compliance.ComplianceDeadline;
import com.hr.fwc.domain.compliance.ComplianceDeadlineRepository;
import com.hr.fwc.domain.compliance.DeadlineStatus;
import com.hr.fwc.infrastructure.persistence.mapper.ComplianceDeadlineMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ComplianceDeadlineRepositoryImpl implements ComplianceDeadlineRepository {

    private final ComplianceDeadlineJpaRepository jpaRepository;

    public ComplianceDeadlineRepositoryImpl(ComplianceDeadlineJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ComplianceDeadline save(ComplianceDeadline deadline) {
        return ComplianceDeadlineMapper.toDomain(jpaRepository.save(ComplianceDeadlineMapper.toEntity(deadline)));
    }

    @Override
    public Optional<ComplianceDeadline> findById(Long id) {
        return jpaRepository.findById(id).map(ComplianceDeadlineMapper::toDomain);
    }

    @Override
    public List<ComplianceDeadline> findByWorkerId(Long workerId) {
        return jpaRepository.findByWorkerId(workerId)
            .stream()
            .map(ComplianceDeadlineMapper::toDomain)
            .toList();
    }

    @Override
    public List<ComplianceDeadline> findByStatusIn(List<DeadlineStatus> statuses) {
        return jpaRepository.findByStatusIn(statuses)
            .stream()
            .map(ComplianceDeadlineMapper::toDomain)
            .toList();
    }

    @Override
    public List<ComplianceDeadline> findByDueDateBeforeAndStatusNot(LocalDate date, DeadlineStatus status) {
        return jpaRepository.findByDueDateBeforeAndStatusNot(date, status)
            .stream()
            .map(ComplianceDeadlineMapper::toDomain)
            .toList();
    }
}
