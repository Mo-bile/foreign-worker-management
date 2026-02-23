package com.hr.fwc.domain.insurance;

import com.hr.fwc.domain.worker.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class HealthInsurancePolicyTest {

    private final HealthInsurancePolicy policy = new HealthInsurancePolicy();

    @Test
    @DisplayName("모든 외국인 근로자는 건강보험 의무가입")
    void allWorkersShouldBeMandatory() {
        ForeignWorker worker = createWorker(Nationality.VIETNAM, VisaType.E9);

        InsuranceEligibility result = policy.determineEligibility(worker);

        assertThat(result.status()).isEqualTo(EligibilityStatus.MANDATORY);
        assertThat(result.insuranceType()).isEqualTo(InsuranceType.HEALTH_INSURANCE);
    }

    @Test
    @DisplayName("비자 유형과 무관하게 건강보험 의무가입")
    void differentVisaTypesShouldAllBeMandatory() {
        for (VisaType visaType : VisaType.values()) {
            ForeignWorker worker = createWorker(Nationality.VIETNAM, visaType);

            InsuranceEligibility result = policy.determineEligibility(worker);

            assertThat(result.status())
                .as("VisaType %s should be MANDATORY", visaType)
                .isEqualTo(EligibilityStatus.MANDATORY);
        }
    }

    @Test
    @DisplayName("건강보험 판정 사유 메시지 포함 확인")
    void shouldIncludeCorrectReasonMessage() {
        ForeignWorker worker = createWorker(Nationality.USA, VisaType.E7);

        InsuranceEligibility result = policy.determineEligibility(worker);

        assertThat(result.reason()).contains("건강보험");
    }

    private ForeignWorker createWorker(Nationality nationality, VisaType visaType) {
        PersonalInfo personalInfo = PersonalInfo.of("홍길동", "P123456", "010-1234-5678", null);
        VisaInfo visaInfo = VisaInfo.of(visaType, LocalDate.of(2026, 12, 31), LocalDate.of(2024, 1, 1), "12345678901");
        EmploymentInfo employmentInfo = EmploymentInfo.of(LocalDate.of(2024, 1, 1), null, 1L);

        return ForeignWorker.create(personalInfo, visaInfo, employmentInfo, nationality);
    }

}
