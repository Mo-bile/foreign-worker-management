package com.hr.fwc.infrastructure.persistence.mapper;

import com.hr.fwc.domain.compliance.ComplianceDeadline;
import com.hr.fwc.infrastructure.persistence.entity.ComplianceDeadlineEntity;

public final class ComplianceDeadlineMapper {

    private ComplianceDeadlineMapper() {
    }

    public static ComplianceDeadlineEntity toEntity(ComplianceDeadline domain) {
        ComplianceDeadlineEntity entity = new ComplianceDeadlineEntity();
        entity.setId(domain.id());
        entity.setWorkerId(domain.workerId());
        entity.setDeadlineType(domain.deadlineType());
        entity.setDueDate(domain.dueDate());
        entity.setStatus(domain.status());
        entity.setDescription(domain.description());
        return entity;
    }

    public static ComplianceDeadline toDomain(ComplianceDeadlineEntity entity) {
        return ComplianceDeadline.reconstitute(
            entity.getId(),
            entity.getWorkerId(),
            entity.getDeadlineType(),
            entity.getDueDate(),
            entity.getStatus(),
            entity.getDescription()
        );
    }
}
