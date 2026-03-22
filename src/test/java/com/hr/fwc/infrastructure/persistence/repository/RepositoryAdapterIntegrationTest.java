package com.hr.fwc.infrastructure.persistence.repository;

import com.hr.fwc.domain.company.Company;
import com.hr.fwc.domain.company.CompanyRepository;
import com.hr.fwc.domain.company.IndustryCategory;
import com.hr.fwc.domain.company.Region;
import com.hr.fwc.domain.compliance.ComplianceDeadline;
import com.hr.fwc.domain.compliance.ComplianceDeadlineRepository;
import com.hr.fwc.domain.compliance.DeadlineStatus;
import com.hr.fwc.domain.compliance.DeadlineType;
import com.hr.fwc.domain.worker.EmploymentInfo;
import com.hr.fwc.domain.worker.ForeignWorker;
import com.hr.fwc.domain.worker.ForeignWorkerRepository;
import com.hr.fwc.domain.worker.Nationality;
import com.hr.fwc.domain.worker.PersonalInfo;
import com.hr.fwc.domain.worker.VisaInfo;
import com.hr.fwc.domain.worker.VisaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RepositoryAdapterIntegrationTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ForeignWorkerRepository workerRepository;

    @Autowired
    private ComplianceDeadlineRepository deadlineRepository;

    @Test
    void companyRepositoryShouldSaveAndFindByBusinessNumber() {
        Company company = Company.create("Test Company", "111-22-33333",
            Region.SEOUL, null, IndustryCategory.MANUFACTURING, null,
            10, 0, "Seoul", "02-0000-0000");

        Company saved = companyRepository.save(company);

        assertThat(saved.id()).isNotNull();
        assertThat(companyRepository.findById(saved.id())).isPresent();
        assertThat(companyRepository.findByBusinessNumber("111-22-33333"))
            .isPresent()
            .get()
            .extracting(Company::name)
            .isEqualTo("Test Company");
    }

    @Test
    void foreignWorkerRepositoryShouldSupportCompanyAndNationalityQueries() {
        Company company = companyRepository.save(
            Company.create("Factory A", "222-33-44444",
                Region.BUSAN, null, IndustryCategory.MANUFACTURING, null,
                50, 10, "Busan", "051-123-4567")
        );

        ForeignWorker worker = ForeignWorker.create(
            PersonalInfo.of("Nguyen", "P-777", "010-7777-7777", null),
            VisaInfo.of(VisaType.E9, LocalDate.of(2027, 5, 31), LocalDate.of(2024, 3, 1), "12345678901"),
            EmploymentInfo.of(LocalDate.of(2024, 3, 1), null, company.id()),
            Nationality.VIETNAM
        );

        ForeignWorker saved = workerRepository.save(worker);

        assertThat(workerRepository.findById(saved.id())).isPresent();
        assertThat(workerRepository.findByCompanyId(company.id())).hasSize(1);
        assertThat(workerRepository.findByNationality(Nationality.VIETNAM)).hasSize(1);
    }

    @Test
    void complianceDeadlineRepositoryShouldSupportStatusAndDateQueries() {
        Company company = companyRepository.save(
            Company.create("Factory B", "333-44-55555",
                Region.INCHEON, null, IndustryCategory.CONSTRUCTION, null,
                30, 5, "Incheon", "032-111-2222")
        );
        ForeignWorker worker = workerRepository.save(
            ForeignWorker.create(
                PersonalInfo.of("John", "P-999", "010-9999-9999", null),
                VisaInfo.of(VisaType.E7, LocalDate.of(2028, 1, 1), LocalDate.of(2024, 1, 1), "10987654321"),
                EmploymentInfo.of(LocalDate.of(2024, 1, 1), null, company.id()),
                Nationality.USA
            )
        );

        ComplianceDeadline urgent = deadlineRepository.save(
            ComplianceDeadline.reconstitute(
                null,
                worker.id(),
                DeadlineType.VISA_EXPIRY,
                LocalDate.of(2026, 1, 15),
                DeadlineStatus.URGENT,
                "urgent visa renewal"
            )
        );
        deadlineRepository.save(
            ComplianceDeadline.reconstitute(
                null,
                worker.id(),
                DeadlineType.INSURANCE_ENROLLMENT,
                LocalDate.of(2026, 2, 10),
                DeadlineStatus.COMPLETED,
                "done"
            )
        );

        List<ComplianceDeadline> statusMatched = deadlineRepository.findByStatusIn(List.of(DeadlineStatus.URGENT));
        List<ComplianceDeadline> dateMatched =
            deadlineRepository.findByDueDateBeforeAndStatusNot(LocalDate.of(2026, 1, 31), DeadlineStatus.COMPLETED);

        assertThat(deadlineRepository.findById(urgent.id())).isPresent();
        assertThat(statusMatched).extracting(ComplianceDeadline::status).contains(DeadlineStatus.URGENT);
        assertThat(dateMatched).extracting(ComplianceDeadline::status).doesNotContain(DeadlineStatus.COMPLETED);
    }
}
