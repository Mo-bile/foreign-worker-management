package com.hr.fwc.application.service;

import com.hr.fwc.application.dto.RegisterWorkerRequest;
import com.hr.fwc.application.dto.WorkerResponse;
import com.hr.fwc.domain.compliance.ComplianceDeadline;
import com.hr.fwc.domain.compliance.ComplianceDeadlineRepository;
import com.hr.fwc.domain.compliance.DeadlineType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class WorkerRegistrationServiceIntegrationTest {

    @Autowired
    private WorkerRegistrationService registrationService;

    @Autowired
    private ComplianceDeadlineRepository deadlineRepository;

    @Test
    @DisplayName("베트남 E-9 근로자 등록 시 보험 판단 및 데드라인 생성")
    void registerVietnamE9Worker() {
        RegisterWorkerRequest request = new RegisterWorkerRequest(
            "Nguyen Van A",
            "V123456",
            "VIETNAM",
            "E9",
            LocalDate.of(2026, 12, 31),
            LocalDate.of(2024, 1, 15),
            "12345678901",
            LocalDate.of(2024, 2, 1),
            null,
            1L,
            "010-1234-5678",
            null
        );

        WorkerResponse response = registrationService.registerWorker(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("Nguyen Van A");
        assertThat(response.nationality()).isEqualTo("베트남");
        assertThat(response.visaType()).isEqualTo("고용허가제 일반외국인");

        assertThat(response.insuranceEligibilities()).hasSize(4);
        assertThat(response.insuranceEligibilities())
            .extracting(WorkerResponse.InsuranceEligibilityDto::insuranceType)
            .containsExactlyInAnyOrder("국민연금", "건강보험", "고용보험", "산재보험");

        List<ComplianceDeadline> deadlines = deadlineRepository.findByWorkerId(response.id());
        assertThat(deadlines).hasSize(2);
        assertThat(deadlines)
            .extracting(ComplianceDeadline::deadlineType)
            .containsExactlyInAnyOrder(DeadlineType.VISA_EXPIRY, DeadlineType.INSURANCE_ENROLLMENT);
    }

    @Test
    @DisplayName("미국 E-7 근로자 등록 시 국민연금 면제 확인")
    void registerUsaE7Worker() {
        RegisterWorkerRequest request = new RegisterWorkerRequest(
            "John Smith",
            "US789012",
            "USA",
            "E7",
            LocalDate.of(2025, 6, 30),
            LocalDate.of(2024, 1, 1),
            "98765432101",
            LocalDate.of(2024, 1, 15),
            null,
            1L,
            "010-9876-5432",
            "john@example.com"
        );

        WorkerResponse response = registrationService.registerWorker(request);

        WorkerResponse.InsuranceEligibilityDto pension = response.insuranceEligibilities().stream()
            .filter(e -> e.insuranceType().equals("국민연금"))
            .findFirst()
            .orElseThrow();

        assertThat(pension.status()).isEqualTo("가입제외");
        assertThat(pension.reason()).contains("사회보장협정");
    }

    @Test
    @DisplayName("F-6 결혼이민자 등록 시 고용보험 의무가입 확인")
    void registerF6Worker() {
        RegisterWorkerRequest request = new RegisterWorkerRequest(
            "Maria Garcia",
            "PH456789",
            "PHILIPPINES",
            "F6",
            LocalDate.of(2027, 3, 15),
            LocalDate.of(2023, 6, 1),
            "11223344556",
            LocalDate.of(2023, 6, 15),
            null,
            1L,
            "010-1111-2222",
            null
        );

        WorkerResponse response = registrationService.registerWorker(request);

        WorkerResponse.InsuranceEligibilityDto employment = response.insuranceEligibilities().stream()
            .filter(e -> e.insuranceType().equals("고용보험"))
            .findFirst()
            .orElseThrow();

        assertThat(employment.status()).isEqualTo("의무가입");
    }

}
