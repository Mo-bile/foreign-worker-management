# 고용지킴이 — 백엔드 기능 명세

> 이 문서는 Spring Boot 백엔드가 구현해야 할 기능의 상세 명세입니다.
> 전체 서비스 아키텍처 중 **데이터 모델, API, 비즈니스 로직, 공공데이터 파이프라인**을 다룹니다.

---

## 서비스 개요

외국인 근로자 고용 라이프사이클(고용 전 → 고용 중 → 지속적 관리)을 AI × 공공데이터로 커버하는 컴플라이언스 서비스.

```
고용 전: 고용허가 시뮬레이터 (쿼터·경쟁 분석)
고용 중: 근로자 등록 → 4대보험 자동 판단 → 사업장 벤치마크
지속적 관리: 법령 변경 감지 → 영향 분석 → 데드라인 추적
```

---

## 1. 데이터 모델

### 1-1. 사업장 (Company)

```
[Company]
├── companyId (PK)
├── name, bizNumber (사업자등록번호)
├── region (시도/시군구)
├── industryCode (업종 대분류/중분류)
├── employeeCount (상시 근로자 수)
├── foreignWorkerCount (외국인 근로자 수)
└── createdAt, updatedAt
```

### 1-2. 근로자 (Worker)

```
[Worker]
├── workerId (PK)
├── companyId (FK → Company)
├── name (영문/한글), nationality, visaType
├── visaExpiry, contractStart, contractEnd
├── monthlySalary (월 임금)
├── employmentPermitNo (고용허가서 번호)
└── status (재직/퇴직/이탈)
```

### 1-3. 보험판단결과 (InsuranceDetermination)

```
[InsuranceDetermination]
├── determinationId (PK)
├── workerId (FK → Worker)
├── nationalPension (의무/면제/선택 + 근거)
├── healthInsurance (의무/면제/선택 + 근거)
├── employmentInsurance (의무/면제/선택 + 근거)
├── industrialAccident (의무 + 근거)
└── determinedAt
```

> 기존 MVP의 Strategy 패턴 유지. 국적(SSA 여부) × 비자 유형 조합으로 판정.

### 1-4. 데드라인 (Deadline)

```
[Deadline]
├── deadlineId (PK)
├── workerId (FK → Worker)
├── type (비자만료/계약만료/신고기한/보험신고)
├── dueDate
├── status (PENDING/APPROACHING/URGENT/OVERDUE/COMPLETED)
└── notifiedAt
```

### 1-5. 공공데이터 테이블

```
[public_data_regional_industry]  — 15102368 CSV
├── id (PK)
├── region (시도, 17개)
├── industry (업종, 8개 대분류)
├── quarter (분기)
├── workerCount
└── referenceDate

[public_data_manufacturing]  — 15111730 CSV
├── id (PK)
├── subIndustry (제조업 중분류, 25종)
├── workerCount
└── year

[public_data_vietnam_e9]  — 15111729 CSV
├── id (PK)
├── industry (업종)
├── totalCount, maleCount, femaleCount
└── referenceDate

[public_data_keis_worker]  — 15105236 CSV (한국고용정보원 마이크로데이터)
├── recordId (PK)
├── region (사업장 소재 지역)
├── industryCode (업종 코드)
├── companySize (사업장 규모)
├── permitType (고용허가유형: 일반/특례/혼합)
├── nationality (국적)
├── workStatus (근무 상태)
├── employmentPeriod (고용 기간)
├── reemployment (재고용 여부)
└── mobilityHistory (이동 이력)

[public_data_quota]  — HWP 수동추출
├── id (PK)
├── year
├── industry (업종)
├── quotaCount (배정 인원)
└── source (도입계획/E-9현황)
```

### 1-6. AI 분석 결과

```
[SimulationResult]  — 1단계 시뮬레이션
├── resultId (PK)
├── companyId (FK)
├── input (JSON — 희망인원, 국적, 시기)
├── quotaAnalysis (JSON)
├── competitionAnalysis (JSON)
├── aiReport (TEXT — Claude 생성 자연어 리포트)
└── createdAt

[BenchmarkReport]  — 2단계 벤치마크
├── reportId (PK)
├── companyId (FK)
├── salaryPercentile, turnoverRisk, dependencyRatio
├── complianceScore (100점 만점)
├── aiReport (TEXT)
└── createdAt

[LegalChange]  — 3단계 법령변경
├── changeId (PK)
├── lawName, changeType (개정/시행/폐지)
├── effectiveDate
├── summary (TEXT)
├── affectedCompanies (JSON)
└── detectedAt

[LegalChangeImpact]  — 3단계 법령변경 영향
├── impactId (PK)
├── changeId (FK), companyId (FK)
├── affectedWorkers (JSON)
├── aiAnalysis (TEXT)
├── requiredActions (JSON)
└── acknowledgedAt
```

---

## 2. API 설계

### 2-1. 사업장 관리

| Method | Path | 설명 |
|--------|------|------|
| POST | `/api/companies` | 사업장 등록 |
| GET | `/api/companies/{id}` | 사업장 조회 |
| PUT | `/api/companies/{id}` | 사업장 정보 수정 |
| GET | `/api/companies/{id}/dashboard` | 대시보드 데이터 (긴급알림, 요약, 컴플라이언스 점수) |

### 2-2. 근로자 관리

| Method | Path | 설명 |
|--------|------|------|
| POST | `/api/workers` | 근로자 등록 (→ 보험 자동 판단 + 데드라인 생성) |
| GET | `/api/workers?companyId={id}` | 근로자 목록 (필터: 비자, 국적, 상태) |
| GET | `/api/workers/{id}` | 근로자 상세 (보험판단 + 데드라인 포함) |
| PUT | `/api/workers/{id}` | 근로자 정보 수정 |

### 2-3. 고용허가 시뮬레이터 (1단계)

| Method | Path | 설명 |
|--------|------|------|
| POST | `/api/simulations` | 시뮬레이션 실행 (입력: 희망인원, 국적, 시기) |
| GET | `/api/simulations/{id}` | 시뮬레이션 결과 조회 |
| GET | `/api/simulations?companyId={id}` | 사업장별 시뮬레이션 이력 |

### 2-4. 벤치마크 (2단계)

| Method | Path | 설명 |
|--------|------|------|
| POST | `/api/benchmarks/generate` | 벤치마크 리포트 생성 |
| GET | `/api/benchmarks/{id}` | 리포트 조회 |
| GET | `/api/benchmarks?companyId={id}` | 사업장별 리포트 이력 |

### 2-5. 컴플라이언스 (데드라인)

| Method | Path | 설명 |
|--------|------|------|
| GET | `/api/compliance/overdue` | 기한 초과 데드라인 |
| GET | `/api/compliance/upcoming?days=30` | 임박 데드라인 |
| GET | `/api/compliance/worker/{workerId}` | 근로자별 데드라인 |
| PATCH | `/api/compliance/{deadlineId}/complete` | 데드라인 완료 처리 |

### 2-6. 법령 변경 (3단계)

| Method | Path | 설명 |
|--------|------|------|
| GET | `/api/legal-changes` | 법령 변경 목록 |
| GET | `/api/legal-changes/{id}/impacts?companyId={id}` | 사업장별 영향 분석 |
| PATCH | `/api/legal-changes/impacts/{id}/acknowledge` | 영향 확인 처리 |

### 2-7. 인증

| Method | Path | 설명 |
|--------|------|------|
| POST | `/api/auth/signup` | 회원가입 |
| POST | `/api/auth/login` | 로그인 |

---

## 3. 비즈니스 로직 상세

### 3-1. 고용허가 시뮬레이터 (1단계)

입력: `companyId` + 희망 채용 인원 + 희망 국적(선택) + 희망 시기

처리 흐름:
1. Company 정보로 지역 × 업종 확정
2. `public_data_quota`에서 해당 업종의 연간 E-9 쿼터 조회
3. `public_data_regional_industry`에서 해당 지역×업종 현재 고용 현황 → 소진율 계산
4. `public_data_keis_worker`에서 동일 지역×업종의 사업장 분포, 경쟁도 산출
5. 희망 국적 선택 시 `public_data_vietnam_e9` 등 국적별 데이터 추가 분석
6. 분석 결과(숫자) → Claude API에 전달 → 자연어 리포트 생성
7. SimulationResult 저장 후 반환

### 3-2. 사업장 벤치마크 (2단계)

트리거: 근로자 등록 후 자동 / 수동 요청

처리 흐름:
1. Company의 지역×업종×규모로 동종업계 필터링
2. **임금 경쟁력**: 워크넷 API로 동종업종 구인공고 임금 조회 → 백분위 산출
3. **인력 유출 위험**: EIS API에서 피보험자 상실 동향 + `public_data_keis_worker`의 이동이력 분석
4. **외국인 의존도**: 등록 근로자 수 vs `public_data_regional_industry` 업종 평균 비율
5. **컴플라이언스 점수**: 보험 가입률 + 데드라인 준수율 + 임금 경쟁력 → 100점 환산
6. 결과 → Claude API → 자연어 리포트
7. BenchmarkReport 저장

### 3-3. 법령 변경 감지 (3단계)

스케줄링: 일 1회 배치

처리 흐름:
1. 법제처 국가법령정보센터 API 호출 — 모니터링 대상 법령의 개정 여부 확인
2. 변경 감지 시 LegalChange 저장
3. 등록된 모든 Company 대상으로 영향 범위 판단
4. 영향 있는 경우 Claude API로 영향 분석 → LegalChangeImpact 저장

모니터링 대상 법령:
- 최저임금법
- 외국인근로자의 고용 등에 관한 법률
- 출입국관리법
- 국민연금법, 국민건강보험법, 고용보험법, 산업재해보상보험법
- 각 사회보장협정 관련 고시

---

## 4. 공공데이터 적재 파이프라인

### 4-1. CSV 적재 (4종)

| 데이터 ID | 테이블 | 갱신 주기 | 비고 |
|-----------|--------|----------|------|
| 15102368 | public_data_regional_industry | 분기 | KOSIS 17시도×8업종 |
| 15111730 | public_data_manufacturing | 연 | 제조업 중분류 25종 |
| 15111729 | public_data_vietnam_e9 | 연 | 베트남 E-9 업종별 |
| 15105236 | public_data_keis_worker | 연 | 한국고용정보원 1인 단위 마이크로데이터 |

적재 방식:
- CSV 파싱 → 정규화 → MySQL UPSERT
- 데이터 기준 시점(referenceDate) 반드시 기록
- 적재 시 기존 데이터 덮어쓰기가 아닌 버전 관리 (snapshotId)

### 4-2. HWP 수동추출 (2종)

| 데이터 ID | 내용 | 빈도 |
|-----------|------|------|
| 15002263 | 외국인력 도입계획 (업종별 E-9 쿼터) | 연 1회 |
| 15143177 | E-9 근무현황 요약 | 참고용 |

→ HWP 파일에서 수동으로 수치를 추출하여 `public_data_quota` 테이블에 입력

### 4-3. API 연동 (3종)

| API | 용도 | 캐싱 |
|-----|------|------|
| 워크넷 채용정보 API | 지역×업종 구인공고 임금 조회 | TTL 24시간 |
| EIS Open API | 피보험자 취득/상실 동향 | TTL 24시간 |
| 직업별 임금정보 | 상/중/하위 임금 벤치마크 | TTL 24시간 |

API 장애 시: 최근 캐시 데이터로 폴백

### 4-4. 법제처 API (보조)

- 국가법령정보센터 Open API — 법령 개정 여부 모니터링용
- 고용노동부 데이터가 아닌 보조 데이터로 분류

---

## 5. Claude API 연동 포인트

모든 AI 생성 콘텐츠 공통 원칙:
- 법률 용어 → 사업주 눈높이 자연어로 번역
- 숫자 나열이 아니라 "그래서 뭘 해야 하는가"를 알려줌
- 판단 근거를 항상 함께 제시
- 면책 문구 상시 포함: "본 서비스는 법률 자문이 아닌 관리 보조 도구입니다"

| 연동 포인트 | 입력 | 출력 |
|------------|------|------|
| 시뮬레이션 리포트 | 쿼터 소진율, 지역 경쟁도, 국적별 분포 (숫자) | 자연어 분석 리포트 |
| 벤치마크 리포트 | 임금 백분위, 유출 위험 지표, 의존도, 점수 (숫자) | 자연어 종합 진단 |
| 법령 영향 분석 | 변경 법령 내용 + 사업장/근로자 정보 | 맞춤 영향 분석 + 필요 조치 |
| 서류 OCR (보조) | 여권/고용허가서 이미지 | 구조화된 정보 추출 |

---

## 6. DB 마이그레이션 계획

현재 상태: H2 인메모리 (개발용, ddl-auto=create-drop)
목표 상태: MySQL (운영용)

필요 작업:
1. `build.gradle`에 `runtimeOnly 'com.mysql:mysql-connector-j'` 추가
2. `application-prod.properties` 생성 (MySQL 연결 설정)
3. `ddl-auto=validate` + Flyway/Liquibase 마이그레이션 도입
4. README "H2 (개발) / PostgreSQL (예정)" → "H2 (개발) / MySQL (운영)" 수정

---

## 7. 구현 우선순위

**최소 제출 가능 (1단계 + 보험판단):**
- 사업장 CRUD + 고용허가 시뮬레이터 API + 근로자 등록 + 4대보험 판단
- CSV 3종 적재 (15102368, 15111730, 15111729) + HWP 수동추출

**표준 (+ 2단계):**
- 위 + 벤치마크 API + 15105236 적재 + 워크넷/EIS API 연동 + 데드라인 관리

**전체 (+ 3단계):**
- 위 + 법령 변경 감지 배치 + 영향 분석 + 컴플라이언스 리포트

---

*서비스명: 고용지킴이 (CompliMate)*
*대회: 제5회 고용노동 공공데이터·AI 활용 공모전 — 제품 및 서비스 개발 부문*
