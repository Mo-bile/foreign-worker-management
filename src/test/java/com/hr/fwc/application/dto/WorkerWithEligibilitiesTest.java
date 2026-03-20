package com.hr.fwc.application.dto;

import com.hr.fwc.domain.insurance.EligibilityStatus;
import com.hr.fwc.domain.insurance.InsuranceEligibility;
import com.hr.fwc.domain.insurance.InsuranceType;
import com.hr.fwc.domain.worker.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WorkerWithEligibilitiesTest {

    @Test
    @DisplayName("근로자와 보험 자격 정보를 함께 보관한다")
    void 근로자와_보험자격을_함께_보관한다() {
        ForeignWorker worker = ForeignWorker.reconstitute(
            1L,
            PersonalInfo.of("Nguyen Van A", "V123456", "010-1234-5678", null),
            VisaInfo.of(VisaType.E9, LocalDate.of(2026, 12, 31), LocalDate.of(2024, 1, 15), "12345678901"),
            EmploymentInfo.of(LocalDate.of(2024, 2, 1), null, 1L),
            Nationality.VIETNAM,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        List<InsuranceEligibility> eligibilities = List.of(
            InsuranceEligibility.of(InsuranceType.NATIONAL_PENSION, EligibilityStatus.MANDATORY, "의무가입")
        );

        WorkerWithEligibilities dto = new WorkerWithEligibilities(worker, eligibilities);

        assertThat(dto.worker().id()).isEqualTo(1L);
        assertThat(dto.eligibilities()).hasSize(1);
    }
}
