package com.hr.fwc.infrastructure.persistence.mapper;

import com.hr.fwc.domain.company.Company;
import com.hr.fwc.domain.company.IndustryCategory;
import com.hr.fwc.domain.company.Region;
import com.hr.fwc.domain.compliance.ComplianceDeadline;
import com.hr.fwc.domain.compliance.DeadlineStatus;
import com.hr.fwc.domain.compliance.DeadlineType;
import com.hr.fwc.domain.worker.EmploymentInfo;
import com.hr.fwc.domain.worker.ForeignWorker;
import com.hr.fwc.domain.worker.Nationality;
import com.hr.fwc.domain.worker.PersonalInfo;
import com.hr.fwc.domain.worker.VisaInfo;
import com.hr.fwc.domain.worker.VisaType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PersistenceMapperTest {

    @Test
    void foreignWorkerMapperShouldPreserveFields() {
        ForeignWorker source = ForeignWorker.reconstitute(
            10L,
            PersonalInfo.of("Alice", "P-123", "010-1111-2222", "alice@example.com"),
            VisaInfo.of(VisaType.E9, LocalDate.of(2027, 1, 31), LocalDate.of(2024, 2, 1), "99001122334"),
            EmploymentInfo.of(LocalDate.of(2024, 2, 1), LocalDate.of(2026, 1, 31), 77L),
            Nationality.VIETNAM,
            LocalDateTime.of(2024, 2, 1, 9, 0),
            LocalDateTime.of(2024, 3, 1, 9, 0)
        );

        ForeignWorker mapped = ForeignWorkerMapper.toDomain(ForeignWorkerMapper.toEntity(source));

        assertThat(mapped.id()).isEqualTo(source.id());
        assertThat(mapped.personalInfo().name()).isEqualTo(source.personalInfo().name());
        assertThat(mapped.personalInfo().passportNumber()).isEqualTo(source.personalInfo().passportNumber());
        assertThat(mapped.visaInfo().visaType()).isEqualTo(source.visaInfo().visaType());
        assertThat(mapped.employmentInfo().companyId()).isEqualTo(source.employmentInfo().companyId());
        assertThat(mapped.nationality()).isEqualTo(source.nationality());
        assertThat(mapped.createdAt()).isEqualTo(source.createdAt());
        assertThat(mapped.updatedAt()).isEqualTo(source.updatedAt());
    }

    @Test
    void complianceDeadlineMapperShouldPreserveFields() {
        ComplianceDeadline source = ComplianceDeadline.reconstitute(
            15L,
            99L,
            DeadlineType.INSURANCE_ENROLLMENT,
            LocalDate.of(2026, 4, 1),
            DeadlineStatus.APPROACHING,
            "insurance due"
        );

        ComplianceDeadline mapped = ComplianceDeadlineMapper.toDomain(ComplianceDeadlineMapper.toEntity(source));

        assertThat(mapped.id()).isEqualTo(source.id());
        assertThat(mapped.workerId()).isEqualTo(source.workerId());
        assertThat(mapped.deadlineType()).isEqualTo(source.deadlineType());
        assertThat(mapped.dueDate()).isEqualTo(source.dueDate());
        assertThat(mapped.status()).isEqualTo(source.status());
        assertThat(mapped.description()).isEqualTo(source.description());
    }

    @Test
    void companyMapperShouldPreserveFields() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 10, 8, 30);
        Company source = Company.reconstitute(
            25L, "Sample Factory", "123-45-67890",
            Region.GYEONGGI, null,
            IndustryCategory.MANUFACTURING, null,
            100, 20, "Seoul", "02-1234-5678",
            createdAt, null
        );

        Company mapped = CompanyMapper.toDomain(CompanyMapper.toEntity(source));

        assertThat(mapped.id()).isEqualTo(source.id());
        assertThat(mapped.name()).isEqualTo(source.name());
        assertThat(mapped.businessNumber()).isEqualTo(source.businessNumber());
        assertThat(mapped.region()).isEqualTo(source.region());
        assertThat(mapped.industryCategory()).isEqualTo(source.industryCategory());
        assertThat(mapped.employeeCount()).isEqualTo(source.employeeCount());
        assertThat(mapped.foreignWorkerCount()).isEqualTo(source.foreignWorkerCount());
        assertThat(mapped.address()).isEqualTo(source.address());
        assertThat(mapped.contactPhone()).isEqualTo(source.contactPhone());
        assertThat(mapped.createdAt()).isEqualTo(source.createdAt());
    }
}
