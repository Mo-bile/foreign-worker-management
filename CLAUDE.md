# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트 개요

**서비스명:** 고용지킴이 (CompliMate)
**대회:** 제5회 고용노동 공공데이터·AI 활용 공모전 — 제품 및 서비스 개발 부문

외국인 근로자 고용 라이프사이클(고용 전 → 고용 중 → 지속적 관리)을 AI × 공공데이터로 커버하는 컴플라이언스 서비스.

```
고용 전: 고용허가 시뮬레이터 (쿼터·경쟁 분석)
고용 중: 근로자 등록 → 4대보험 자동 판단 → 사업장 벤치마크
지속적 관리: 법령 변경 감지 → 영향 분석 → 데드라인 추적
```

> 상세 명세: `docs/SPEC.md` 참조
> 전체 기획·전략 문서: `../foreign-worker-compliance-project-management/` 참조

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
│   ├── company/      # Company 애그리거트 (Region, IndustryCategory enum)
│   └── publicdata/   # 공공데이터 도메인 (RegionalIndustry, Manufacturing, VietnamE9, Quota)
├── application/      # 서비스, DTO
│   ├── service/      # WorkerRegistrationService, CompanyService, ComplianceDashboardService
│   └── dto/          # RegisterWorkerRequest, CompanyResponse, WorkerResponse
├── infrastructure/   # JPA 엔티티, 매퍼, 리포지토리 구현체, CSV 로더
│   ├── persistence/  # entity/, mapper/, repository/
│   └── loader/       # CsvLoader, PublicDataInitializer, csv/
└── presentation/     # REST 컨트롤러
    └── api/          # WorkerApi, CompanyApi, ComplianceController
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

### 현재 구현
- `POST /api/companies` — 사업장 등록 (201, 400, 409)
- `GET /api/companies` — 사업장 목록 조회
- `GET /api/companies/{id}` — 사업장 상세 조회 (200, 404)
- `PUT /api/companies/{id}` — 사업장 정보 수정 (200, 404)
- `POST /api/workers` — 외국인 근로자 등록 (자동으로 보험 자격 판정 + 데드라인 생성)
- `GET /api/compliance/overdue` — 기한 초과 데드라인 조회
- `GET /api/compliance/upcoming?days=30` — 임박 데드라인 조회
- `GET /api/compliance/worker/{workerId}` — 근로자별 데드라인 조회

> **Breaking Change (Stage 1):** `POST /api/workers` 요청의 `workplaceId` → `companyId`로 변경

### 전체 API 설계 (SPEC 기준)

**사업장:** `POST/GET/PUT /api/companies`, `GET /api/companies/{id}/dashboard`
**근로자:** `POST/GET/PUT /api/workers`, `GET /api/workers?companyId={id}`
**시뮬레이터 (1단계):** `POST /api/simulations`, `GET /api/simulations/{id}`, `GET /api/simulations?companyId={id}`
**벤치마크 (2단계):** `POST /api/benchmarks/generate`, `GET /api/benchmarks/{id}`
**컴플라이언스:** 기존 + `PATCH /api/compliance/{deadlineId}/complete`
**법령 변경 (3단계):** `GET /api/legal-changes`, `GET /api/legal-changes/{id}/impacts`, `PATCH .../acknowledge`
**인증:** `POST /api/auth/signup`, `POST /api/auth/login`

## 도메인 용어

| 코드 | 의미 |
|------|------|
| E9 | 고용허가제 일반외국인 |
| E8 | 계절근로자 (2026.2 신설) |
| H2 | 외국국적동포 |
| E7/E7_4 | 전문직/숙련기능인력 |
| F2/F5/F6 | 거주/영주/결혼이민 |
| SSA | 사회보장협정 (Social Security Agreement) |

## 구현 우선순위

**1단계 — 최소 제출 가능 (시뮬레이터 + 보험판단):**
- 사업장 CRUD + 고용허가 시뮬레이터 API + 근로자 등록 + 4대보험 판단
- CSV 3종 적재 (15102368, 15111730, 15111729) + HWP 수동추출

**2단계 — 표준 (+ 벤치마크):**
- 벤치마크 API + 15105236 적재 + 근로복지공단 API 연동 + 데드라인 관리

**3단계 — 전체 (+ 법령 감지):**
- 법령 변경 감지 배치(법제처 API) + 영향 분석 + 컴플라이언스 리포트

## 공공데이터 파이프라인

### CSV 적재 (4종)
| 데이터 ID | 테이블 | 갱신 주기 |
|-----------|--------|----------|
| 15102368 | public_data_regional_industry | 분기 |
| 15111730 | public_data_manufacturing | 연 |
| 15111729 | public_data_vietnam_e9 | 연 |
| 15105236 | public_data_keis_worker | 연 |

- CSV 파싱 → 정규화 → UPSERT, 버전 관리 (snapshotId)

### API 연동 (2종)
| API | 제공기관 | 용도 | 단계 |
|-----|----------|------|------|
| 고용/산재보험 현황정보 | 근로복지공단 | 사업장 보험가입 검증 + 자동채움 (사업자등록번호 기반) | 1~2단계 |
| 국가법령정보센터 | 법제처 | 법령 개정 모니터링 | 3단계 |

- 근로복지공단 API: data.go.kr 직접 활용신청, 자동승인, 응답 필드(사업장명, 주소, 상시인원, 업종코드, 보험가입구분)
- API 장애 시 최근 캐시 데이터로 폴백

## Claude API 연동 원칙

- 법률 용어 → 사업주 눈높이 자연어로 번역
- "그래서 뭘 해야 하는가"를 알려주는 액션 중심 리포트
- 판단 근거를 항상 함께 제시
- **면책 문구 상시 포함:** "본 서비스는 법률 자문이 아닌 관리 보조 도구입니다"

| 연동 포인트 | 입력 | 출력 |
|------------|------|------|
| 시뮬레이션 리포트 | 쿼터 소진율, 지역 경쟁도, 국적별 분포 | 자연어 분석 리포트 |
| 벤치마크 리포트 | 임금 백분위, 유출 위험, 의존도, 점수 | 자연어 종합 진단 |
| 법령 영향 분석 | 변경 법령 + 사업장/근로자 정보 | 맞춤 영향 분석 + 필요 조치 |

## DB 마이그레이션 계획

- 현재: H2 인메모리 (개발용, ddl-auto=create-drop)
- 목표: MySQL (운영용) + Flyway/Liquibase 마이그레이션
- `ddl-auto=validate`로 전환 필요

## 주의사항

- 테스트 메서드명은 한글 서술형 사용 (예: `사회보장협정_체결국_근로자는_국민연금_면제`)
- `Nationality` enum에 `socialSecurityAgreement` 플래그가 있음 — 보험 정책 로직의 핵심 분기점
- `DeadlineType` enum에 알림 임계값(D-90, D-30 등)이 정의되어 있음
- E-8 계절근로자는 2026.2 법개정으로 추가됨 — 별도 보험 처리는 v0.2 예정
- **신규 기능 완료 시 FE 변경점 공유 필수** — 기능 구현이 끝나면 아래 템플릿으로 프론트엔드 팀에 전달할 변경사항을 반드시 출력할 것:

```
### FE 변경점

| 엔드포인트 | 메서드 | 변경 | 요청 DTO | 응답 DTO | 상태 코드 | 비고 |
|-----------|--------|------|----------|----------|----------|------|
| /api/workers | POST | 신규 | RegisterWorkerRequest | WorkerResponse | 201, 400, 409 | 보험 자격 자동 판정 포함 |
```
