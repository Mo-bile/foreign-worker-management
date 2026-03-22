package com.hr.fwc.domain.company;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CompanyTest {

    @Test
    @DisplayName("사업장 생성 시 모든 필드가 올바르게 설정됨")
    void 사업장_생성_시_필드가_올바르게_설정됨() {
        Company company = Company.create(
            "한국제조(주)", "123-45-67890",
            Region.GYEONGGI, null,
            IndustryCategory.MANUFACTURING, null,
            100, 20, "경기도 안산시", "031-123-4567"
        );

        assertThat(company.name()).isEqualTo("한국제조(주)");
        assertThat(company.businessNumber()).isEqualTo("123-45-67890");
        assertThat(company.region()).isEqualTo(Region.GYEONGGI);
        assertThat(company.subRegion()).isNull();
        assertThat(company.industryCategory()).isEqualTo(IndustryCategory.MANUFACTURING);
        assertThat(company.industrySubCategory()).isNull();
        assertThat(company.employeeCount()).isEqualTo(100);
        assertThat(company.foreignWorkerCount()).isEqualTo(20);
        assertThat(company.createdAt()).isNotNull();
    }

    @Test
    @DisplayName("사업장명이 null이면 예외 발생")
    void 사업장명이_null이면_예외_발생() {
        assertThatThrownBy(() -> Company.create(
            null, "123-45-67890", Region.SEOUL, null,
            IndustryCategory.MANUFACTURING, null, 10, 0, "서울", "02-0000"
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("사업자등록번호가 null이면 예외 발생")
    void 사업자등록번호가_null이면_예외_발생() {
        assertThatThrownBy(() -> Company.create(
            "테스트", null, Region.SEOUL, null,
            IndustryCategory.MANUFACTURING, null, 10, 0, "서울", "02-0000"
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("region이 null이면 예외 발생")
    void region이_null이면_예외_발생() {
        assertThatThrownBy(() -> Company.create(
            "테스트", "123-45-67890", null, null,
            IndustryCategory.MANUFACTURING, null, 10, 0, "서울", "02-0000"
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("industryCategory가 null이면 예외 발생")
    void industryCategory가_null이면_예외_발생() {
        assertThatThrownBy(() -> Company.create(
            "테스트", "123-45-67890", Region.SEOUL, null,
            null, null, 10, 0, "서울", "02-0000"
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("employeeCount가 0 이하이면 예외 발생")
    void employeeCount가_0이하이면_예외_발생() {
        assertThatThrownBy(() -> Company.create(
            "테스트", "123-45-67890", Region.SEOUL, null,
            IndustryCategory.MANUFACTURING, null, 0, 0, "서울", "02-0000"
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("foreignWorkerCount가 employeeCount보다 크면 예외 발생")
    void foreignWorkerCount가_employeeCount_초과시_예외_발생() {
        assertThatThrownBy(() -> Company.create(
            "테스트", "123-45-67890", Region.SEOUL, null,
            IndustryCategory.MANUFACTURING, null, 10, 15, "서울", "02-0000"
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("subRegion과 industrySubCategory는 nullable")
    void 선택_필드는_nullable() {
        Company company = Company.create(
            "테스트", "123-45-67890", Region.SEOUL, "강남구",
            IndustryCategory.MANUFACTURING, "식료품제조업",
            50, 10, "서울", "02-0000"
        );

        assertThat(company.subRegion()).isEqualTo("강남구");
        assertThat(company.industrySubCategory()).isEqualTo("식료품제조업");
    }

    @Test
    @DisplayName("updateInfo는 새 Company 객체를 반환함 (불변)")
    void updateInfo는_새_객체를_반환함() {
        Company original = Company.create(
            "테스트", "123-45-67890", Region.SEOUL, null,
            IndustryCategory.MANUFACTURING, null, 50, 10, "서울", "02-0000"
        );

        Company updated = original.updateInfo(
            "변경됨", Region.BUSAN, null,
            IndustryCategory.CONSTRUCTION, null, 100, 20, "부산", "051-0000"
        );

        assertThat(updated).isNotSameAs(original);
        assertThat(updated.name()).isEqualTo("변경됨");
        assertThat(updated.businessNumber()).isEqualTo("123-45-67890"); // 불변
        assertThat(original.name()).isEqualTo("테스트"); // 원본 변경 없음
    }
}
