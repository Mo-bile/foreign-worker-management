# 계층 간 객체 분리 리팩터링 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Service 레이어가 presentation 객체(Response)를 반환하지 않도록 리팩터링하여, 계층 간 책임을 명확히 분리한다.

**Architecture:** Application Service는 도메인 객체 또는 application-level DTO를 반환하고, Controller(presentation)가 Response로 변환한다. Worker 도메인은 `WorkerWithEligibilities` application DTO를 도입하고, Compliance 도메인은 도메인 객체 직접 반환으로 되돌린다.

**Tech Stack:** Java 21, Spring Boot 3.3.7

---

## 파일 변경 맵

### 신규 생성
- `src/main/java/com/hr/fwc/application/dto/WorkerWithEligibilities.java` — application-level DTO

### 수정
- `src/main/java/com/hr/fwc/application/dto/WorkerResponse.java` — `from(WorkerWithEligibilities)` 팩토리 메서드 추가
- `src/main/java/com/hr/fwc/application/service/WorkerQueryService.java` — `WorkerWithEligibilities` 반환
- `src/main/java/com/hr/fwc/application/service/WorkerRegistrationService.java` — `WorkerWithEligibilities` 반환
- `src/main/java/com/hr/fwc/presentation/WorkerController.java` — DTO → Response 변환 추가
- `src/main/java/com/hr/fwc/application/service/ComplianceDashboardService.java` — `List<ComplianceDeadline>` 반환으로 되돌림
- `src/main/java/com/hr/fwc/presentation/ComplianceController.java` — 도메인 → Response 변환 추가
- `src/main/java/com/hr/fwc/presentation/WorkerController.java` — DTO → Response 변환 추가 (Task 2, 3에서 단계적 수정)
- `src/test/java/com/hr/fwc/application/service/WorkerQueryServiceTest.java` — `WorkerWithEligibilities` 검증
- `src/test/java/com/hr/fwc/application/service/WorkerRegistrationServiceIntegrationTest.java` — `WorkerWithEligibilities` 검증
- `src/test/java/com/hr/fwc/application/service/ComplianceDashboardServiceIntegrationTest.java` — `List<ComplianceDeadline>` 검증
- `src/test/java/com/hr/fwc/presentation/WorkerControllerTest.java` — mock 반환 타입 변경

---

## Task 1: WorkerWithEligibilities application DTO 생성

**Files:**
- Create: `src/main/java/com/hr/fwc/application/dto/WorkerWithEligibilities.java`
- Test: `src/test/java/com/hr/fwc/application/dto/WorkerWithEligibilitiesTest.java`

- [ ] **Step 1: 단위 테스트 작성**

```java
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
```

- [ ] **Step 2: 테스트 실패 확인**

Run: `./gradlew test --tests "*.WorkerWithEligibilitiesTest"`
Expected: FAIL — `WorkerWithEligibilities` 클래스 없음

- [ ] **Step 3: WorkerWithEligibilities record 구현**

```java
package com.hr.fwc.application.dto;

import com.hr.fwc.domain.insurance.InsuranceEligibility;
import com.hr.fwc.domain.worker.ForeignWorker;

import java.util.List;

public record WorkerWithEligibilities(
    ForeignWorker worker,
    List<InsuranceEligibility> eligibilities
) {
}
```

- [ ] **Step 4: 테스트 통과 확인**

Run: `./gradlew test --tests "*.WorkerWithEligibilitiesTest"`
Expected: PASS

- [ ] **Step 5: WorkerResponse에 from(WorkerWithEligibilities) 팩토리 메서드 추가**

`src/main/java/com/hr/fwc/application/dto/WorkerResponse.java`에 추가:

```java
public static WorkerResponse from(WorkerWithEligibilities dto) {
    return from(dto.worker(), dto.eligibilities());
}
```

기존 `from(ForeignWorker, List<InsuranceEligibility>)` 메서드는 그대로 유지한다 (내부 위임).

- [ ] **Step 6: 전체 테스트 확인**

Run: `./gradlew test`
Expected: 전체 PASS (아직 호출부는 변경하지 않았으므로 기존 코드 그대로 동작)

- [ ] **Step 7: 커밋**

```bash
git add src/main/java/com/hr/fwc/application/dto/WorkerWithEligibilities.java \
        src/test/java/com/hr/fwc/application/dto/WorkerWithEligibilitiesTest.java \
        src/main/java/com/hr/fwc/application/dto/WorkerResponse.java
git commit -m "refactor: add WorkerWithEligibilities application DTO"
```

---

## Task 2: WorkerQueryService — Response 대신 application DTO 반환

**Files:**
- Modify: `src/main/java/com/hr/fwc/application/service/WorkerQueryService.java`
- Modify: `src/main/java/com/hr/fwc/presentation/WorkerController.java`
- Modify: `src/test/java/com/hr/fwc/application/service/WorkerQueryServiceTest.java`
- Modify: `src/test/java/com/hr/fwc/presentation/WorkerControllerTest.java`

- [ ] **Step 1: WorkerQueryServiceTest 수정 — WorkerWithEligibilities 검증으로 변경**

```java
package com.hr.fwc.application.service;

import com.hr.fwc.application.dto.WorkerWithEligibilities;
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

            List<WorkerWithEligibilities> result = workerQueryService.getAllWorkers();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).worker().id()).isEqualTo(1L);
            assertThat(result.get(0).eligibilities()).hasSize(2);
        }

        @Test
        @DisplayName("등록된 근로자가 없으면 빈 목록을 반환한다")
        void 등록된_근로자가_없으면_빈_목록을_반환한다() {
            when(workerRepository.findAll()).thenReturn(List.of());

            List<WorkerWithEligibilities> result = workerQueryService.getAllWorkers();

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

            WorkerWithEligibilities result = workerQueryService.getWorkerById(1L);

            assertThat(result.worker().id()).isEqualTo(1L);
            assertThat(result.worker().personalInfo().name()).isEqualTo("Nguyen Van A");
            assertThat(result.eligibilities()).hasSize(2);
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
```

- [ ] **Step 2: 테스트 실패 확인**

Run: `./gradlew test --tests "*.WorkerQueryServiceTest"`
Expected: FAIL — 컴파일 에러. `WorkerQueryService.getAllWorkers()`가 `List<WorkerResponse>`를 반환하지만, 테스트는 `List<WorkerWithEligibilities>`로 받으려 하므로 타입 불일치

- [ ] **Step 3: WorkerQueryService 수정**

```java
package com.hr.fwc.application.service;

import com.hr.fwc.application.dto.WorkerWithEligibilities;
import com.hr.fwc.domain.insurance.InsuranceEligibility;
import com.hr.fwc.domain.insurance.InsuranceEligibilityService;
import com.hr.fwc.domain.worker.ForeignWorker;
import com.hr.fwc.domain.worker.ForeignWorkerRepository;
import com.hr.fwc.domain.worker.WorkerNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WorkerQueryService {

    private final ForeignWorkerRepository workerRepository;
    private final InsuranceEligibilityService insuranceService;

    public WorkerQueryService(ForeignWorkerRepository workerRepository,
                              InsuranceEligibilityService insuranceService) {
        this.workerRepository = workerRepository;
        this.insuranceService = insuranceService;
    }

    public List<WorkerWithEligibilities> getAllWorkers() {
        return workerRepository.findAll().stream()
            .map(this::toWorkerWithEligibilities)
            .toList();
    }

    public WorkerWithEligibilities getWorkerById(Long id) {
        ForeignWorker worker = workerRepository.findById(id)
            .orElseThrow(() -> new WorkerNotFoundException("근로자를 찾을 수 없습니다. ID: " + id));
        return toWorkerWithEligibilities(worker);
    }

    private WorkerWithEligibilities toWorkerWithEligibilities(ForeignWorker worker) {
        List<InsuranceEligibility> eligibilities = insuranceService.determineAllEligibilities(worker);
        return new WorkerWithEligibilities(worker, eligibilities);
    }
}
```

- [ ] **Step 4: WorkerQueryServiceTest 통과 확인**

Run: `./gradlew test --tests "*.WorkerQueryServiceTest"`
Expected: PASS

- [ ] **Step 5: WorkerController 수정 — DTO → Response 변환 추가**

```java
package com.hr.fwc.presentation;

import com.hr.fwc.application.dto.WorkerResponse;
import com.hr.fwc.application.service.WorkerQueryService;
import com.hr.fwc.application.service.WorkerRegistrationService;
import com.hr.fwc.application.dto.RegisterWorkerRequest;
import com.hr.fwc.presentation.api.WorkerApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workers")
public class WorkerController implements WorkerApi {

    private final WorkerRegistrationService registrationService;
    private final WorkerQueryService queryService;

    public WorkerController(WorkerRegistrationService registrationService,
                            WorkerQueryService queryService) {
        this.registrationService = registrationService;
        this.queryService = queryService;
    }

    @PostMapping
    public ResponseEntity<WorkerResponse> registerWorker(@RequestBody RegisterWorkerRequest request) {
        // 주의: 이 메서드는 Task 3에서 WorkerRegistrationService 반환 타입 변경 시 함께 수정된다.
        // Task 2에서는 QueryService만 수정하므로 여기는 아직 기존 방식(WorkerResponse 직접 수신) 유지.
        WorkerResponse response = registrationService.registerWorker(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<WorkerResponse>> getAllWorkers() {
        List<WorkerResponse> responses = queryService.getAllWorkers().stream()
            .map(WorkerResponse::from)
            .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkerResponse> getWorkerById(@PathVariable Long id) {
        WorkerResponse response = WorkerResponse.from(queryService.getWorkerById(id));
        return ResponseEntity.ok(response);
    }
}
```

- [ ] **Step 6: WorkerControllerTest 수정 — mock 반환 타입을 WorkerWithEligibilities로 변경**

목록/상세 조회 테스트에서 `queryService` mock이 `WorkerWithEligibilities`를 반환하도록 변경한다. 등록 테스트(`registerWorker`)는 Task 3에서 수정하므로 이 단계에서는 그대로 둔다.

변경 대상 (4개 테스트 메서드 중 3개):

```java
// import 추가
import com.hr.fwc.application.dto.WorkerWithEligibilities;
import com.hr.fwc.domain.insurance.InsuranceEligibility;
import com.hr.fwc.domain.insurance.InsuranceType;
import com.hr.fwc.domain.insurance.EligibilityStatus;
import com.hr.fwc.domain.worker.*;
import java.time.LocalDateTime;

// 근로자_목록_조회_성공 메서드 변경:
@Test
@DisplayName("근로자 목록 조회 API - 200 OK")
void 근로자_목록_조회_성공() throws Exception {
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
        InsuranceEligibility.of(InsuranceType.NATIONAL_PENSION, EligibilityStatus.MANDATORY, "테스트")
    );
    WorkerWithEligibilities dto = new WorkerWithEligibilities(worker, eligibilities);

    when(queryService.getAllWorkers()).thenReturn(List.of(dto));

    mockMvc.perform(get("/api/workers"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].name").value("Nguyen Van A"))
        .andExpect(jsonPath("$[0].insuranceEligibilities[0].insuranceType").value("국민연금"));
}

// 근로자_목록이_비어있으면_빈_배열_반환 변경:
@Test
@DisplayName("근로자 목록 조회 API - 빈 목록")
void 근로자_목록이_비어있으면_빈_배열_반환() throws Exception {
    when(queryService.getAllWorkers()).thenReturn(List.of());

    mockMvc.perform(get("/api/workers"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
}

// 근로자_상세_조회_성공 변경:
@Test
@DisplayName("근로자 상세 조회 API - 200 OK")
void 근로자_상세_조회_성공() throws Exception {
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
        InsuranceEligibility.of(InsuranceType.NATIONAL_PENSION, EligibilityStatus.MANDATORY, "테스트"),
        InsuranceEligibility.of(InsuranceType.HEALTH_INSURANCE, EligibilityStatus.MANDATORY, "테스트")
    );
    WorkerWithEligibilities dto = new WorkerWithEligibilities(worker, eligibilities);

    when(queryService.getWorkerById(1L)).thenReturn(dto);

    mockMvc.perform(get("/api/workers/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Nguyen Van A"))
        .andExpect(jsonPath("$.insuranceEligibilities", hasSize(2)));
}

// 존재하지_않는_근로자_조회시_404_반환은 변경 없음 (예외를 던지므로 반환 타입 무관)

// 주의: registerWorker() 테스트는 이 태스크에서 수정하지 않는다.
// Task 3 Step 5에서 WorkerRegistrationService 반환 타입 변경과 함께 수정한다.
```

- [ ] **Step 7: 전체 테스트 확인**

Run: `./gradlew test`
Expected: 전체 PASS

- [ ] **Step 8: 커밋**

```bash
git add src/main/java/com/hr/fwc/application/service/WorkerQueryService.java \
        src/main/java/com/hr/fwc/presentation/WorkerController.java \
        src/test/java/com/hr/fwc/application/service/WorkerQueryServiceTest.java \
        src/test/java/com/hr/fwc/presentation/WorkerControllerTest.java
git commit -m "refactor: WorkerQueryService returns WorkerWithEligibilities instead of WorkerResponse"
```

---

## Task 3: WorkerRegistrationService — Response 대신 application DTO 반환

**Files:**
- Modify: `src/main/java/com/hr/fwc/application/service/WorkerRegistrationService.java`
- Modify: `src/main/java/com/hr/fwc/presentation/WorkerController.java` (registerWorker 변환 추가)
- Modify: `src/test/java/com/hr/fwc/application/service/WorkerRegistrationServiceIntegrationTest.java`
- Modify: `src/test/java/com/hr/fwc/presentation/WorkerControllerTest.java` (registerWorker mock 변경)

- [ ] **Step 1: WorkerRegistrationServiceIntegrationTest 수정 — WorkerWithEligibilities 검증**

변경할 부분:
- `WorkerResponse response = registrationService.registerWorker(request);` → `WorkerWithEligibilities result = registrationService.registerWorker(request);`
- `response.id()` → `result.worker().id()`
- `response.name()` → `result.worker().personalInfo().name()`
- `response.nationality()` → `result.worker().nationality().koreanName()`
- `response.visaType()` → `result.worker().visaInfo().visaType().description()`
- `response.insuranceEligibilities()` → `result.eligibilities()`
- `InsuranceEligibilityDto::insuranceType` → `e -> e.insuranceType().koreanName()`
- 보험 status/reason 접근도 도메인 객체의 메서드로 변경

전체 테스트 파일:

```java
package com.hr.fwc.application.service;

import com.hr.fwc.application.dto.RegisterWorkerRequest;
import com.hr.fwc.application.dto.WorkerWithEligibilities;
import com.hr.fwc.domain.compliance.ComplianceDeadline;
import com.hr.fwc.domain.compliance.ComplianceDeadlineRepository;
import com.hr.fwc.domain.compliance.DeadlineType;
import com.hr.fwc.domain.insurance.InsuranceEligibility;
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
            "Nguyen Van A", "V123456", "VIETNAM", "E9",
            LocalDate.of(2026, 12, 31), LocalDate.of(2024, 1, 15),
            "12345678901", LocalDate.of(2024, 2, 1), null, 1L,
            "010-1234-5678", null
        );

        WorkerWithEligibilities result = registrationService.registerWorker(request);

        assertThat(result.worker().id()).isNotNull();
        assertThat(result.worker().personalInfo().name()).isEqualTo("Nguyen Van A");
        assertThat(result.worker().nationality().koreanName()).isEqualTo("베트남");
        assertThat(result.worker().visaInfo().visaType().description()).isEqualTo("고용허가제 일반외국인");

        assertThat(result.eligibilities()).hasSize(4);
        assertThat(result.eligibilities())
            .extracting(e -> e.insuranceType().koreanName())
            .containsExactlyInAnyOrder("국민연금", "건강보험", "고용보험", "산재보험");

        List<ComplianceDeadline> deadlines = deadlineRepository.findByWorkerId(result.worker().id());
        assertThat(deadlines).hasSize(2);
        assertThat(deadlines)
            .extracting(ComplianceDeadline::deadlineType)
            .containsExactlyInAnyOrder(DeadlineType.VISA_EXPIRY, DeadlineType.INSURANCE_ENROLLMENT);
    }

    @Test
    @DisplayName("미국 E-7 근로자 등록 시 국민연금 면제 확인")
    void registerUsaE7Worker() {
        RegisterWorkerRequest request = new RegisterWorkerRequest(
            "John Smith", "US789012", "USA", "E7",
            LocalDate.of(2025, 6, 30), LocalDate.of(2024, 1, 1),
            "98765432101", LocalDate.of(2024, 1, 15), null, 1L,
            "010-9876-5432", "john@example.com"
        );

        WorkerWithEligibilities result = registrationService.registerWorker(request);

        InsuranceEligibility pension = result.eligibilities().stream()
            .filter(e -> e.insuranceType().koreanName().equals("국민연금"))
            .findFirst()
            .orElseThrow();

        assertThat(pension.status().koreanName()).isEqualTo("가입제외");
        assertThat(pension.reason()).contains("사회보장협정");
    }

    @Test
    @DisplayName("F-6 결혼이민자 등록 시 고용보험 의무가입 확인")
    void registerF6Worker() {
        RegisterWorkerRequest request = new RegisterWorkerRequest(
            "Maria Garcia", "PH456789", "PHILIPPINES", "F6",
            LocalDate.of(2027, 3, 15), LocalDate.of(2023, 6, 1),
            "11223344556", LocalDate.of(2023, 6, 15), null, 1L,
            "010-1111-2222", null
        );

        WorkerWithEligibilities result = registrationService.registerWorker(request);

        InsuranceEligibility employment = result.eligibilities().stream()
            .filter(e -> e.insuranceType().koreanName().equals("고용보험"))
            .findFirst()
            .orElseThrow();

        assertThat(employment.status().koreanName()).isEqualTo("의무가입");
    }
}
```

- [ ] **Step 2: 테스트 실패 확인**

Run: `./gradlew test --tests "*.WorkerRegistrationServiceIntegrationTest"`
Expected: FAIL — 컴파일 에러. `registerWorker()`가 `WorkerResponse`를 반환하지만 테스트는 `WorkerWithEligibilities`로 받으려 하므로 타입 불일치

> **주의:** Task 3의 Step 3~5는 빌드가 깨지지 않도록 반드시 연속 실행해야 한다. Step 3(Service 변경)만 하면 WorkerController와 WorkerControllerTest가 컴파일 실패한다. Step 3 → Step 4(Controller 수정) → Step 5(ControllerTest 수정)를 한 번에 진행할 것.

- [ ] **Step 3: WorkerRegistrationService 수정**

`registerWorker` 메서드의 반환 타입만 변경:

```java
// 기존
public WorkerResponse registerWorker(RegisterWorkerRequest request) {
    ...
    return WorkerResponse.from(savedWorker, eligibilities);
}

// 변경
public WorkerWithEligibilities registerWorker(RegisterWorkerRequest request) {
    ...
    return new WorkerWithEligibilities(savedWorker, eligibilities);
}
```

import 변경: `import com.hr.fwc.application.dto.WorkerResponse;` 제거, `import com.hr.fwc.application.dto.WorkerWithEligibilities;` 추가. (사용하지 않는 import가 남지 않도록 확인할 것)

- [ ] **Step 4: WorkerController.registerWorker() 변환 추가**

```java
@PostMapping
public ResponseEntity<WorkerResponse> registerWorker(@RequestBody RegisterWorkerRequest request) {
    WorkerResponse response = WorkerResponse.from(registrationService.registerWorker(request));
    return ResponseEntity.ok(response);
}
```

- [ ] **Step 5: WorkerControllerTest.registerWorker() mock 변경**

```java
@Test
@DisplayName("근로자 등록 API 요청")
void registerWorker() throws Exception {
    RegisterWorkerRequest request = new RegisterWorkerRequest(
        "Nguyen Van A", "V123456", "VIETNAM", "E9",
        LocalDate.of(2026, 12, 31), LocalDate.of(2024, 1, 15),
        "12345678901", LocalDate.of(2024, 2, 1), null, 1L,
        "010-1234-5678", null
    );

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
        InsuranceEligibility.of(InsuranceType.NATIONAL_PENSION, EligibilityStatus.MANDATORY, "테스트"),
        InsuranceEligibility.of(InsuranceType.HEALTH_INSURANCE, EligibilityStatus.MANDATORY, "테스트")
    );
    WorkerWithEligibilities dto = new WorkerWithEligibilities(worker, eligibilities);

    when(registrationService.registerWorker(any())).thenReturn(dto);

    mockMvc.perform(post("/api/workers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Nguyen Van A"))
        .andExpect(jsonPath("$.nationality").value("베트남"))
        .andExpect(jsonPath("$.insuranceEligibilities[0].insuranceType").value("국민연금"));
}
```

- [ ] **Step 6: 전체 테스트 확인**

Run: `./gradlew test`
Expected: 전체 PASS

- [ ] **Step 7: 커밋**

```bash
git add src/main/java/com/hr/fwc/application/service/WorkerRegistrationService.java \
        src/main/java/com/hr/fwc/presentation/WorkerController.java \
        src/test/java/com/hr/fwc/application/service/WorkerRegistrationServiceIntegrationTest.java \
        src/test/java/com/hr/fwc/presentation/WorkerControllerTest.java
git commit -m "refactor: WorkerRegistrationService returns WorkerWithEligibilities instead of WorkerResponse"
```

---

## Task 4: ComplianceDashboardService — 도메인 객체 반환으로 되돌림

**Files:**
- Modify: `src/main/java/com/hr/fwc/application/service/ComplianceDashboardService.java`
- Modify: `src/main/java/com/hr/fwc/presentation/ComplianceController.java`
- Modify: `src/test/java/com/hr/fwc/application/service/ComplianceDashboardServiceIntegrationTest.java`

- [ ] **Step 1: ComplianceDashboardServiceIntegrationTest 수정 — 도메인 객체 검증으로 되돌림**

```java
package com.hr.fwc.application.service;

import com.hr.fwc.domain.compliance.ComplianceDeadline;
import com.hr.fwc.domain.compliance.ComplianceDeadlineRepository;
import com.hr.fwc.domain.compliance.DeadlineStatus;
import com.hr.fwc.domain.compliance.DeadlineType;
import org.junit.jupiter.api.BeforeEach;
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
class ComplianceDashboardServiceIntegrationTest {

    @Autowired
    private ComplianceDashboardService dashboardService;

    @Autowired
    private ComplianceDeadlineRepository deadlineRepository;

    @BeforeEach
    void setUp() {
        createDeadline(1L, DeadlineType.VISA_EXPIRY, LocalDate.now().minusDays(5), DeadlineStatus.OVERDUE);
        createDeadline(2L, DeadlineType.VISA_EXPIRY, LocalDate.now().plusDays(5), DeadlineStatus.URGENT);
        createDeadline(3L, DeadlineType.INSURANCE_ENROLLMENT, LocalDate.now().plusDays(20), DeadlineStatus.APPROACHING);
        createDeadline(4L, DeadlineType.VISA_EXPIRY, LocalDate.now().plusDays(100), DeadlineStatus.PENDING);
        createDeadline(5L, DeadlineType.VISA_EXPIRY, LocalDate.now().plusDays(3), DeadlineStatus.COMPLETED);
    }

    @Test
    @DisplayName("기한 초과 데드라인 조회")
    void getOverdueDeadlines() {
        List<ComplianceDeadline> overdue = dashboardService.getOverdueDeadlines();

        assertThat(overdue).hasSize(1);
        assertThat(overdue.get(0).workerId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("임박한 데드라인 조회 (30일 이내)")
    void getUpcomingDeadlines() {
        List<ComplianceDeadline> upcoming = dashboardService.getUpcomingDeadlines(30);

        assertThat(upcoming).hasSize(3);
        assertThat(upcoming)
            .extracting(ComplianceDeadline::workerId)
            .containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    @DisplayName("특정 근로자의 데드라인 조회")
    void getWorkerDeadlines() {
        createDeadline(10L, DeadlineType.VISA_EXPIRY, LocalDate.now().plusDays(10), DeadlineStatus.URGENT);
        createDeadline(10L, DeadlineType.INSURANCE_ENROLLMENT, LocalDate.now().plusDays(25), DeadlineStatus.APPROACHING);

        List<ComplianceDeadline> workerDeadlines = dashboardService.getWorkerDeadlines(10L);

        assertThat(workerDeadlines).hasSize(2);
    }

    private void createDeadline(Long workerId, DeadlineType type, LocalDate dueDate, DeadlineStatus status) {
        ComplianceDeadline deadline = ComplianceDeadline.create(workerId, type, dueDate, "test");
        if (status == DeadlineStatus.COMPLETED) {
            deadline.markAsCompleted();
        }
        deadlineRepository.save(deadline);
    }
}
```

- [ ] **Step 2: 테스트 실패 확인**

Run: `./gradlew test --tests "*.ComplianceDashboardServiceIntegrationTest"`
Expected: FAIL — 반환 타입 불일치

- [ ] **Step 3: ComplianceDashboardService를 도메인 객체 반환으로 되돌림**

```java
package com.hr.fwc.application.service;

import com.hr.fwc.domain.compliance.ComplianceDeadline;
import com.hr.fwc.domain.compliance.ComplianceDeadlineRepository;
import com.hr.fwc.domain.compliance.DeadlineStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ComplianceDashboardService {

    private final ComplianceDeadlineRepository deadlineRepository;

    public ComplianceDashboardService(ComplianceDeadlineRepository deadlineRepository) {
        this.deadlineRepository = deadlineRepository;
    }

    public List<ComplianceDeadline> getOverdueDeadlines() {
        return deadlineRepository.findByStatusIn(List.of(DeadlineStatus.OVERDUE));
    }

    public List<ComplianceDeadline> getUpcomingDeadlines(int days) {
        return deadlineRepository.findByDueDateBeforeAndStatusNot(
            LocalDate.now().plusDays(days),
            DeadlineStatus.COMPLETED
        );
    }

    public List<ComplianceDeadline> getWorkerDeadlines(Long workerId) {
        return deadlineRepository.findByWorkerId(workerId);
    }
}
```

- [ ] **Step 4: ComplianceController에서 도메인 → Response 변환 추가**

```java
package com.hr.fwc.presentation;

import com.hr.fwc.application.dto.ComplianceDeadlineResponse;
import com.hr.fwc.application.service.ComplianceDashboardService;
import com.hr.fwc.presentation.api.ComplianceApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compliance")
public class ComplianceController implements ComplianceApi {

    private final ComplianceDashboardService dashboardService;

    public ComplianceController(ComplianceDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<ComplianceDeadlineResponse>> getOverdueDeadlines() {
        List<ComplianceDeadlineResponse> responses = dashboardService.getOverdueDeadlines().stream()
            .map(ComplianceDeadlineResponse::from)
            .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<ComplianceDeadlineResponse>> getUpcomingDeadlines(
            @RequestParam(defaultValue = "30") int days) {
        List<ComplianceDeadlineResponse> responses = dashboardService.getUpcomingDeadlines(days).stream()
            .map(ComplianceDeadlineResponse::from)
            .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/worker/{workerId}")
    public ResponseEntity<List<ComplianceDeadlineResponse>> getWorkerDeadlines(@PathVariable Long workerId) {
        List<ComplianceDeadlineResponse> responses = dashboardService.getWorkerDeadlines(workerId).stream()
            .map(ComplianceDeadlineResponse::from)
            .toList();
        return ResponseEntity.ok(responses);
    }
}
```

> **ComplianceApi 인터페이스**는 변경 불필요 — 이미 `List<ComplianceDeadlineResponse>` 반환 타입으로 되어 있음. 변환이 Controller에서 일어나는 것일 뿐 HTTP 응답 타입은 동일.

- [ ] **Step 5: 전체 테스트 확인**

Run: `./gradlew test`
Expected: 전체 PASS

- [ ] **Step 6: 커밋**

```bash
git add src/main/java/com/hr/fwc/application/service/ComplianceDashboardService.java \
        src/main/java/com/hr/fwc/presentation/ComplianceController.java \
        src/test/java/com/hr/fwc/application/service/ComplianceDashboardServiceIntegrationTest.java
git commit -m "refactor: ComplianceDashboardService returns domain objects, Controller handles conversion"
```

---

## 완료 확인 체크리스트

- [ ] `./gradlew test` — 전체 테스트 PASS
- [ ] `ComplianceDashboardService`가 `ComplianceDeadlineResponse`를 import하지 않음
- [ ] `WorkerQueryService`가 `WorkerResponse`를 import하지 않음
- [ ] `WorkerRegistrationService`가 `WorkerResponse`를 import하지 않음
- [ ] Controller에서만 Response 변환이 일어남
- [ ] Application DTO(`WorkerWithEligibilities`)가 도메인 객체만 참조함
