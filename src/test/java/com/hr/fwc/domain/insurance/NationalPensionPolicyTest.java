package com.hr.fwc.domain.insurance;

import com.hr.fwc.domain.worker.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class NationalPensionPolicyTest {

    private final NationalPensionPolicy policy = new NationalPensionPolicy();

    @Test
    @DisplayName("사회보장협정 체결국 국적자는 국민연금 면제")
    void ssaCountryShouldBeExempt() {
        ForeignWorker usWorker = createWorker(Nationality.USA, VisaType.E9);

        InsuranceEligibility result = policy.determineEligibility(usWorker);

        assertThat(result.status()).isEqualTo(EligibilityStatus.EXEMPT);
        assertThat(result.insuranceType()).isEqualTo(InsuranceType.NATIONAL_PENSION);
    }

    @Test
    @DisplayName("사회보장협정 미체결국 국적자는 국민연금 의무가입")
    void nonSsaCountryShouldBeMandatory() {
        ForeignWorker vietnamWorker = createWorker(Nationality.VIETNAM, VisaType.E9);

        InsuranceEligibility result = policy.determineEligibility(vietnamWorker);

        assertThat(result.status()).isEqualTo(EligibilityStatus.MANDATORY);
    }

    @Test
    @DisplayName("일본도 사회보장협정 미체결국으로 의무가입")
    void japanShouldBeMandatory() {
        ForeignWorker japanWorker = createWorker(Nationality.JAPAN, VisaType.E7);

        InsuranceEligibility result = policy.determineEligibility(japanWorker);

        assertThat(result.status()).isEqualTo(EligibilityStatus.MANDATORY);
    }

    private ForeignWorker createWorker(Nationality nationality, VisaType visaType) {
        PersonalInfo personalInfo = PersonalInfo.of("홍길동", "P123456", "010-1234-5678", null);
        VisaInfo visaInfo = VisaInfo.of(visaType, LocalDate.of(2026, 12, 31), LocalDate.of(2024, 1, 1), "12345678901");
        EmploymentInfo employmentInfo = EmploymentInfo.of(LocalDate.of(2024, 1, 1), null, 1L);

        return ForeignWorker.create(personalInfo, visaInfo, employmentInfo, nationality);
    }

}
