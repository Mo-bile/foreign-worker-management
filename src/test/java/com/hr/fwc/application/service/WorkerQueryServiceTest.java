package com.hr.fwc.application.service;

import com.hr.fwc.application.dto.WorkerResponse;
import com.hr.fwc.domain.insurance.InsuranceEligibility;
import com.hr.fwc.domain.insurance.InsuranceEligibilityService;
import com.hr.fwc.domain.insurance.InsuranceType;
import com.hr.fwc.domain.insurance.EligibilityStatus;
import com.hr.fwc.domain.worker.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkerQueryServiceTest {

    @Mock
    private ForeignWorkerRepository workerRepository;

    @Mock
    private InsuranceEligibilityService insuranceService;

    @InjectMocks
    private WorkerQueryService workerQueryService;

    private ForeignWorker sampleWorker;
    private List<InsuranceEligibility> sampleEligibilities;

    @BeforeEach
    void setUp() {
        sampleWorker = ForeignWorker.reconstitute(
            1L,
            PersonalInfo.of("Nguyen Van A", "V123456", "010-1234-5678", null),
            VisaInfo.of(VisaType.E9, LocalDate.of(2026, 12, 31), LocalDate.of(2024, 1, 15), "12345678901"),
            EmploymentInfo.of(LocalDate.of(2024, 2, 1), null, 1L),
            Nationality.VIETNAM,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        sampleEligibilities = List.of(
            InsuranceEligibility.of(InsuranceType.NATIONAL_PENSION, EligibilityStatus.MANDATORY, "의무가입 대상"),
            InsuranceEligibility.of(InsuranceType.HEALTH_INSURANCE, EligibilityStatus.MANDATORY, "의무가입 대상")
        );
    }

    @Nested
    @DisplayName("전체 근로자 목록 조회")
    class GetAllWorkers {

        @Test
        @DisplayName("등록된 근로자가 있으면 목록을 반환한다")
        void 등록된_근로자가_있으면_목록을_반환한다() {
            when(workerRepository.findAll()).thenReturn(List.of(sampleWorker));
            when(insuranceService.determineAllEligibilities(any())).thenReturn(sampleEligibilities);

            List<WorkerResponse> result = workerQueryService.getAllWorkers();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).id()).isEqualTo(1L);
            assertThat(result.get(0).name()).isEqualTo("Nguyen Van A");
            assertThat(result.get(0).insuranceEligibilities()).hasSize(2);
        }

        @Test
        @DisplayName("등록된 근로자가 없으면 빈 목록을 반환한다")
        void 등록된_근로자가_없으면_빈_목록을_반환한다() {
            when(workerRepository.findAll()).thenReturn(List.of());

            List<WorkerResponse> result = workerQueryService.getAllWorkers();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("근로자 상세 조회")
    class GetWorkerById {

        @Test
        @DisplayName("존재하는 ID로 조회하면 근로자 정보를 반환한다")
        void 존재하는_ID로_조회하면_근로자_정보를_반환한다() {
            when(workerRepository.findById(1L)).thenReturn(Optional.of(sampleWorker));
            when(insuranceService.determineAllEligibilities(sampleWorker)).thenReturn(sampleEligibilities);

            WorkerResponse result = workerQueryService.getWorkerById(1L);

            assertThat(result.id()).isEqualTo(1L);
            assertThat(result.name()).isEqualTo("Nguyen Van A");
            assertThat(result.nationality()).isNotNull();
            assertThat(result.insuranceEligibilities()).hasSize(2);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회하면 WorkerNotFoundException이 발생한다")
        void 존재하지_않는_ID로_조회하면_예외가_발생한다() {
            when(workerRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> workerQueryService.getWorkerById(999L))
                .isInstanceOf(WorkerNotFoundException.class);
        }
    }
}
