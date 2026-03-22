package com.hr.fwc.application.dto;

import com.hr.fwc.domain.company.Company;

import java.time.LocalDateTime;

public record CompanyResponse(
    Long id,
    String name,
    String businessNumber,
    String region,
    String regionName,
    String subRegion,
    String industryCategory,
    String industryCategoryName,
    String industrySubCategory,
    int employeeCount,
    int foreignWorkerCount,
    String address,
    String contactPhone,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static CompanyResponse from(Company company) {
        return new CompanyResponse(
            company.id(),
            company.name(),
            company.businessNumber(),
            company.region().name(),
            company.region().koreanName(),
            company.subRegion(),
            company.industryCategory().name(),
            company.industryCategory().koreanName(),
            company.industrySubCategory(),
            company.employeeCount(),
            company.foreignWorkerCount(),
            company.address(),
            company.contactPhone(),
            company.createdAt(),
            company.updatedAt()
        );
    }
}
