# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트 개요

외국인 근로자 고용 시 발생하는 법적 의무(4대보험 가입, 비자 만료, 고용변동 신고 등)를 자동 추적·관리하는 Spring Boot 백엔드 시스템. 한국 노동법 기반 도메인 로직이 핵심.

## 빌드 & 실행

```bash
./gradlew build          # 빌드 + 테스트
./gradlew test           # 테스트만 실행
./gradlew bootRun        # 로컬 실행 (localhost:8080)

# 단일 테스트 실행
./gradlew test --tests "com.hr.fwc.domain.insurance.HealthInsurancePolicyTest"

# 특정 메서드
./gradlew test --tests "*.NationalPensionPolicyTest.사회보장협정_체결국_근로자는_국민연금_면제"
```

- Java 21, Gradle 9.0, Spring Boot 3.3.7
- H2 인메모리 DB (create-drop) — H2 콘솔: `http://localhost:8080/h2-console` (JDBC: `jdbc:h2:mem:fwcdb`, user: `sa`)

## 아키텍처

DDD 레이어드 아키텍처. 패키지 구조:

```
com.hr.fwc
├── domain/           # 도메인 모델, 정책, 리포지토리 인터페이스
│   ├── worker/       # ForeignWorker 애그리거트 (PersonalInfo, VisaInfo, EmploymentInfo VO)
│   ├── insurance/    # 4대보험 정책 (Strategy 패턴)
│   ├── compliance/   # ComplianceDeadline (상태 자동 계산)
│   └── workplace/    # Workplace 엔티티
├── application/      # 서비스, DTO
│   ├── service/      # WorkerRegistrationService, ComplianceDashboardService
│   └── dto/          # RegisterWorkerRequest, WorkerResponse
└── presentation/     # REST 컨트롤러
    └── api/          # WorkerController, ComplianceController
```

### 핵심 도메인 개념

**보험 정책 (Strategy 패턴):** `InsuranceEligibilityPolicy` 인터페이스를 4개 정책 클래스가 구현. 국적(사회보장협정 여부) × 비자 유형 조합으로 MANDATORY/OPTIONAL/EXEMPT 판정.

- `NationalPensionPolicy` — 사회보장협정 체결국 → EXEMPT, 나머지 → MANDATORY
- `HealthInsurancePolicy` — 전원 MANDATORY
- `EmploymentInsurancePolicy` — F2/F5/F6 비자 → MANDATORY, 나머지 → OPTIONAL
- `IndustrialAccidentPolicy` — 전원 MANDATORY

**컴플라이언스 데드라인 (상태 머신):** `ComplianceDeadline.calculateStatus()`가 due date 기준 상태 자동 계산:
- D-90+ → PENDING, D-30~D-7 → APPROACHING, D-7~D-0 → URGENT, 초과 → OVERDUE, 수동 → COMPLETED

**Value Object 불변성:** PersonalInfo, VisaInfo, EmploymentInfo, InsuranceEligibility는 모두 `@Embeddable` 불변 객체. 팩토리 메서드(`of()`, `create()`)로만 생성.

## API 엔드포인트

- `POST /api/workers` — 외국인 근로자 등록 (자동으로 보험 자격 판정 + 데드라인 생성)
- `GET /api/compliance/overdue` — 기한 초과 데드라인 조회
- `GET /api/compliance/upcoming?days=30` — 임박 데드라인 조회
- `GET /api/compliance/worker/{workerId}` — 근로자별 데드라인 조회

## 도메인 용어

| 코드 | 의미 |
|------|------|
| E9 | 고용허가제 일반외국인 |
| E8 | 계절근로자 (2026.2 신설) |
| H2 | 외국국적동포 |
| E7/E7_4 | 전문직/숙련기능인력 |
| F2/F5/F6 | 거주/영주/결혼이민 |
| SSA | 사회보장협정 (Social Security Agreement) |

## 주의사항

- 테스트 메서드명은 한글 서술형 사용 (예: `사회보장협정_체결국_근로자는_국민연금_면제`)
- `Nationality` enum에 `socialSecurityAgreement` 플래그가 있음 — 보험 정책 로직의 핵심 분기점
- `DeadlineType` enum에 알림 임계값(D-90, D-30 등)이 정의되어 있음
- E-8 계절근로자는 2026.2 법개정으로 추가됨 — 별도 보험 처리는 v0.2 예정
