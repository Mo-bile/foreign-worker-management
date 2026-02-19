package com.hr.fwc.domain.insurance;

import com.hr.fwc.domain.worker.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class InsuranceEligibilityServiceTest {

    private final InsuranceEligibilityService service = new InsuranceEligibilityService();

    @Test
    @DisplayName("베트남 E-9 근로자의 4대보험 판단")
    void vietnamE9WorkerEligibility() {
        ForeignWorker worker = createWorker(Nationality.VIETNAM, VisaType.E9);

        var results = service.determineAllEligibilities(worker);

        assertThat(results).hasSize(4);
        assertThat(findByType(results, InsuranceType.NATIONAL_PENSION).status()).isEqualTo(EligibilityStatus.MANDATORY);
        assertThat(findByType(results, InsuranceType.HEALTH_INSURANCE).status()).isEqualTo(EligibilityStatus.MANDATORY);
        assertThat(findByType(results, InsuranceType.EMPLOYMENT_INSURANCE).status()).isEqualTo(EligibilityStatus.OPTIONAL);
        assertThat(findByType(results, InsuranceType.INDUSTRIAL_ACCIDENT).status()).isEqualTo(EligibilityStatus.MANDATORY);
    }

    @Test
    @DisplayName("미국 E-7 근로자의 4대보험 판단 - 국민연금 면제")
    void usaE7WorkerEligibility() {
        ForeignWorker worker = createWorker(Nationality.USA, VisaType.E7);

        var results = service.determineAllEligibilities(worker);

        assertThat(findByType(results, InsuranceType.NATIONAL_PENSION).status()).isEqualTo(EligibilityStatus.EXEMPT);
        assertThat(findByType(results, InsuranceType.EMPLOYMENT_INSURANCE).status()).isEqualTo(EligibilityStatus.OPTIONAL);
    }

    @Test
    @DisplayName("F-6 결혼이민자는 고용보험도 의무가입")
    void f6WorkerEligibility() {
        ForeignWorker worker = createWorker(Nationality.VIETNAM, VisaType.F6);

        var mandatory = service.getMandatoryInsurances(worker);

        assertThat(mandatory).hasSize(4);
        assertThat(mandatory.stream().map(InsuranceEligibility::insuranceType))
            .containsExactlyInAnyOrder(
                InsuranceType.NATIONAL_PENSION,
                InsuranceType.HEALTH_INSURANCE,
                InsuranceType.EMPLOYMENT_INSURANCE,
                InsuranceType.INDUSTRIAL_ACCIDENT
            );
    }

    private ForeignWorker createWorker(Nationality nationality, VisaType visaType) {
        PersonalInfo personalInfo = PersonalInfo.of("홍길동", "P123456", "010-1234-5678", null);
        VisaInfo visaInfo = VisaInfo.of(visaType, LocalDate.of(2026, 12, 31), LocalDate.of(2024, 1, 1), "12345678901");
        EmploymentInfo employmentInfo = EmploymentInfo.of(LocalDate.of(2024, 1, 1), null, 1L);

        return ForeignWorker.create(personalInfo, visaInfo, employmentInfo, nationality);
    }

    private InsuranceEligibility findByType(java.util.List<InsuranceEligibility> results, InsuranceType type) {
        return results.stream()
            .filter(r -> r.insuranceType() == type)
            .findFirst()
            .orElseThrow();
    }

}
