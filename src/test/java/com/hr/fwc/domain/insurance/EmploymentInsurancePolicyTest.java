package com.hr.fwc.domain.insurance;

import com.hr.fwc.domain.worker.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class EmploymentInsurancePolicyTest {

    private final EmploymentInsurancePolicy policy = new EmploymentInsurancePolicy();

    @Test
    @DisplayName("F-2, F-5, F-6 비자는 고용보험 의무가입")
    void residenceVisasShouldBeMandatory() {
        ForeignWorker f2Worker = createWorker(VisaType.F2);
        ForeignWorker f5Worker = createWorker(VisaType.F5);
        ForeignWorker f6Worker = createWorker(VisaType.F6);

        assertThat(policy.determineEligibility(f2Worker).status()).isEqualTo(EligibilityStatus.MANDATORY);
        assertThat(policy.determineEligibility(f5Worker).status()).isEqualTo(EligibilityStatus.MANDATORY);
        assertThat(policy.determineEligibility(f6Worker).status()).isEqualTo(EligibilityStatus.MANDATORY);
    }

    @Test
    @DisplayName("E-9, H-2, E-7 비자는 고용보험 임의가입")
    void workVisasShouldBeOptional() {
        ForeignWorker e9Worker = createWorker(VisaType.E9);
        ForeignWorker h2Worker = createWorker(VisaType.H2);
        ForeignWorker e7Worker = createWorker(VisaType.E7);

        assertThat(policy.determineEligibility(e9Worker).status()).isEqualTo(EligibilityStatus.OPTIONAL);
        assertThat(policy.determineEligibility(h2Worker).status()).isEqualTo(EligibilityStatus.OPTIONAL);
        assertThat(policy.determineEligibility(e7Worker).status()).isEqualTo(EligibilityStatus.OPTIONAL);
    }

    @Test
    @DisplayName("E-7-4 숙련기능인력도 임의가입")
    void e7_4ShouldBeOptional() {
        ForeignWorker e7_4Worker = createWorker(VisaType.E7_4);

        InsuranceEligibility result = policy.determineEligibility(e7_4Worker);

        assertThat(result.status()).isEqualTo(EligibilityStatus.OPTIONAL);
    }

    private ForeignWorker createWorker(VisaType visaType) {
        PersonalInfo personalInfo = PersonalInfo.of("홍길동", "P123456", "010-1234-5678", null);
        VisaInfo visaInfo = VisaInfo.of(visaType, LocalDate.of(2026, 12, 31), LocalDate.of(2024, 1, 1), "12345678901");
        EmploymentInfo employmentInfo = EmploymentInfo.of(LocalDate.of(2024, 1, 1), null, 1L);

        return ForeignWorker.create(personalInfo, visaInfo, employmentInfo, Nationality.VIETNAM);
    }

}
