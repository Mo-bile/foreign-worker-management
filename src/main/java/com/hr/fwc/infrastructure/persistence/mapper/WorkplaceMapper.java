package com.hr.fwc.infrastructure.persistence.mapper;

import com.hr.fwc.domain.workplace.Workplace;
import com.hr.fwc.infrastructure.persistence.entity.WorkplaceEntity;

public final class WorkplaceMapper {

    private WorkplaceMapper() {
    }

    public static WorkplaceEntity toEntity(Workplace domain) {
        WorkplaceEntity entity = new WorkplaceEntity();
        entity.setId(domain.id());
        entity.setName(domain.name());
        entity.setBusinessNumber(domain.businessNumber());
        entity.setAddress(domain.address());
        entity.setContactPhone(domain.contactPhone());
        entity.setCreatedAt(domain.createdAt());
        return entity;
    }

    public static Workplace toDomain(WorkplaceEntity entity) {
        return Workplace.reconstitute(
            entity.getId(),
            entity.getName(),
            entity.getBusinessNumber(),
            entity.getAddress(),
            entity.getContactPhone(),
            entity.getCreatedAt()
        );
    }
}
