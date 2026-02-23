package com.hr.fwc.domain.worker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValueObjectEdgeCaseTest {

    @Test
    @DisplayName("PersonalInfo: 이름이 null이면 예외 발생")
    void personalInfoShouldRejectNullName() {
        assertThatThrownBy(() -> PersonalInfo.of(null, "P123", "010-1234-5678", null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("VisaInfo: 비자 유형이 null이면 예외 발생")
    void visaInfoShouldRejectNullVisaType() {
        assertThatThrownBy(() -> VisaInfo.of(null, LocalDate.of(2026, 12, 31), LocalDate.of(2024, 1, 1), "12345"))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("VisaInfo: 비자 만료일이 null이면 예외 발생")
    void visaInfoShouldRejectNullExpiryDate() {
        assertThatThrownBy(() -> VisaInfo.of(VisaType.E9, null, LocalDate.of(2024, 1, 1), "12345"))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("EmploymentInfo: 계약 시작 전이면 비활성")
    void isActiveShouldBeFalseBeforeStartDate() {
        EmploymentInfo info = EmploymentInfo.of(LocalDate.now().plusDays(30), null, 1L);

        assertThat(info.isActive()).isFalse();
    }

    @Test
    @DisplayName("EmploymentInfo: 종료일 없으면 활성")
    void isActiveShouldBeTrueWhenNoEndDate() {
        EmploymentInfo info = EmploymentInfo.of(LocalDate.now().minusDays(30), null, 1L);

        assertThat(info.isActive()).isTrue();
    }

    @Test
    @DisplayName("EmploymentInfo: 종료일 지나면 비활성")
    void isActiveShouldBeFalseAfterEndDate() {
        EmploymentInfo info = EmploymentInfo.of(LocalDate.now().minusDays(60), LocalDate.now().minusDays(1), 1L);

        assertThat(info.isActive()).isFalse();
    }

    @Test
    @DisplayName("ForeignWorker: SSA 체결국 여부가 국적과 일치")
    void hasSocialSecurityAgreementShouldMatchNationality() {
        ForeignWorker usaWorker = createWorker(Nationality.USA);
        ForeignWorker indonesiaWorker = createWorker(Nationality.INDONESIA);

        assertThat(usaWorker.hasSocialSecurityAgreement()).isTrue();
        assertThat(indonesiaWorker.hasSocialSecurityAgreement()).isFalse();
    }

    private ForeignWorker createWorker(Nationality nationality) {
        PersonalInfo personalInfo = PersonalInfo.of("테스트", "P999", "010-0000-0000", null);
        VisaInfo visaInfo = VisaInfo.of(VisaType.E9, LocalDate.of(2026, 12, 31), LocalDate.of(2024, 1, 1), "99999999999");
        EmploymentInfo employmentInfo = EmploymentInfo.of(LocalDate.of(2024, 1, 1), null, 1L);

        return ForeignWorker.create(personalInfo, visaInfo, employmentInfo, nationality);
    }

}
