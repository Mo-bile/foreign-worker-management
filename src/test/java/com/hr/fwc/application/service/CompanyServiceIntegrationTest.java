package com.hr.fwc.application.service;

import com.hr.fwc.application.dto.CompanyResponse;
import com.hr.fwc.application.dto.CreateCompanyRequest;
import com.hr.fwc.application.dto.UpdateCompanyRequest;
import com.hr.fwc.domain.company.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class CompanyServiceIntegrationTest {

    @Autowired
    private CompanyService companyService;

    @Test
    @DisplayName("사업장을 등록하고 조회할 수 있다")
    void 사업장_등록_및_조회() {
        CreateCompanyRequest request = new CreateCompanyRequest(
            "테스트 제조", "999-99-99999", Region.GYEONGGI, "안산시",
            IndustryCategory.MANUFACTURING, "금속가공", 100, 20, "경기도 안산시", "031-000-0000"
        );

        CompanyResponse created = companyService.createCompany(request);

        assertThat(created.id()).isNotNull();
        assertThat(created.name()).isEqualTo("테스트 제조");
        assertThat(created.region()).isEqualTo("GYEONGGI");
        assertThat(created.regionName()).isEqualTo("경기도");

        CompanyResponse found = companyService.getCompany(created.id());
        assertThat(found.businessNumber()).isEqualTo("999-99-99999");
    }

    @Test
    @DisplayName("사업장 목록을 조회할 수 있다")
    void 사업장_목록_조회() {
        List<CompanyResponse> all = companyService.getAllCompanies();
        assertThat(all).hasSizeGreaterThanOrEqualTo(8);
    }

    @Test
    @DisplayName("사업장 정보를 수정할 수 있다")
    void 사업장_정보_수정() {
        CreateCompanyRequest request = new CreateCompanyRequest(
            "원래이름", "888-88-88888", Region.SEOUL, null,
            IndustryCategory.SERVICE, null, 50, 5, "서울", "02-000-0000"
        );
        CompanyResponse created = companyService.createCompany(request);

        UpdateCompanyRequest update = new UpdateCompanyRequest(
            "변경이름", Region.BUSAN, "해운대구",
            IndustryCategory.ACCOMMODATION, null, 80, 15, "부산", "051-000-0000"
        );
        CompanyResponse updated = companyService.updateCompany(created.id(), update);

        assertThat(updated.name()).isEqualTo("변경이름");
        assertThat(updated.businessNumber()).isEqualTo("888-88-88888");
        assertThat(updated.region()).isEqualTo("BUSAN");
    }

    @Test
    @DisplayName("존재하지 않는 사업장 조회 시 예외 발생")
    void 존재하지_않는_사업장_조회() {
        assertThatThrownBy(() -> companyService.getCompany(99999L))
            .isInstanceOf(CompanyNotFoundException.class);
    }

    @Test
    @DisplayName("중복 사업자등록번호 등록 시 예외 발생")
    void 중복_사업자등록번호() {
        CreateCompanyRequest request = new CreateCompanyRequest(
            "첫번째", "777-77-77777", Region.SEOUL, null,
            IndustryCategory.SERVICE, null, 10, 0, "서울", "02-000"
        );
        companyService.createCompany(request);

        CreateCompanyRequest duplicate = new CreateCompanyRequest(
            "두번째", "777-77-77777", Region.BUSAN, null,
            IndustryCategory.CONSTRUCTION, null, 20, 0, "부산", "051-000"
        );
        assertThatThrownBy(() -> companyService.createCompany(duplicate))
            .isInstanceOf(DuplicateBusinessNumberException.class);
    }
}
