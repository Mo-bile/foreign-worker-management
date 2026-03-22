package com.hr.fwc.infrastructure.persistence.mapper;

import com.hr.fwc.domain.company.Company;
import com.hr.fwc.infrastructure.persistence.entity.CompanyEntity;

public final class CompanyMapper {

    private CompanyMapper() {
    }

    public static CompanyEntity toEntity(Company domain) {
        CompanyEntity entity = new CompanyEntity();
        entity.setId(domain.id());
        entity.setName(domain.name());
        entity.setBusinessNumber(domain.businessNumber());
        entity.setRegion(domain.region());
        entity.setSubRegion(domain.subRegion());
        entity.setIndustryCategory(domain.industryCategory());
        entity.setIndustrySubCategory(domain.industrySubCategory());
        entity.setEmployeeCount(domain.employeeCount());
        entity.setForeignWorkerCount(domain.foreignWorkerCount());
        entity.setAddress(domain.address());
        entity.setContactPhone(domain.contactPhone());
        entity.setCreatedAt(domain.createdAt());
        entity.setUpdatedAt(domain.updatedAt());
        return entity;
    }

    public static Company toDomain(CompanyEntity entity) {
        return Company.reconstitute(
            entity.getId(),
            entity.getName(),
            entity.getBusinessNumber(),
            entity.getRegion(),
            entity.getSubRegion(),
            entity.getIndustryCategory(),
            entity.getIndustrySubCategory(),
            entity.getEmployeeCount(),
            entity.getForeignWorkerCount(),
            entity.getAddress(),
            entity.getContactPhone(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
