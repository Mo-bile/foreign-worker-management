# SPEC 1단계 인프라 설계: Company CRUD + 공공데이터 CSV 적재

> 날짜: 2026-03-22
> 범위: SPEC 1단계 중 인프라 우선 — 사업장 CRUD, 공공데이터 테이블 3종 CSV 적재, 쿼터 시드
> Claude API 연동은 이번 범위에서 제외

---

## 1. 배경 및 목표

### 현재 상태
- 근로자 등록/조회, 4대보험 자격 판정, 컴플라이언스 데드라인 관리 구현 완료
- `Workplace` 엔티티: 기본 정보(사업자번호, 주소, 연락처)만 존재, REST API 없음
- 공공데이터 테이블/CSV 적재 미구현

### 목표
1. `Workplace → Company` 리네이밍 + SPEC 필드 확장 (region, industryCode, employeeCount, foreignWorkerCount)
2. Company CRUD REST API 제공
3. 공공데이터 3종 CSV 자동 적재 파이프라인 구축 (부팅 시 로드)
4. 쿼터(quota) 데이터 data.sql 시드

### 범위 외
- Claude API 연동 (시뮬레이션 자연어 리포트)
- 고용허가 시뮬레이터 비즈니스 로직
- REST API를 통한 CSV 업로드 (갱신용)

---

## 2. Workplace → Company 리네이밍 + 필드 확장

### 2-1. 도메인 모델

```java
// domain/company/Company.java
public class Company {
    private Long id;
    private String name;
    private String businessNumber;      // 사업자등록번호
    private String region;              // 시도 (e.g. "경기도")
    private String industryCode;        // 업종 대분류 코드
    private int employeeCount;          // 상시 근로자 수
    private int foreignWorkerCount;     // 외국인 근로자 수
    private String address;             // 기존 유지
    private String contactNumber;       // 기존 유지
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 팩토리 메서드: create(), reconstruct()
    // 불변 업데이트: updateInfo() → 새 Company 반환
}
```

### 2-2. 리네이밍 영향 범위

| 기존 | 변경 후 |
|------|---------|
| `domain/workplace/Workplace` | `domain/company/Company` |
| `domain/workplace/WorkplaceRepository` | `domain/company/CompanyRepository` |
| `infrastructure/.../WorkplaceEntity` | `infrastructure/.../CompanyEntity` |
| `infrastructure/.../WorkplaceJpaRepository` | `infrastructure/.../CompanyJpaRepository` |
| `infrastructure/.../WorkplaceRepositoryImpl` | `infrastructure/.../CompanyRepositoryImpl` |
| `infrastructure/.../WorkplaceMapper` | `infrastructure/.../CompanyMapper` |
| `EmploymentInfo.workplaceId` | `EmploymentInfo.companyId` |
| `RegisterWorkerRequest.workplaceId` | `RegisterWorkerRequest.companyId` |
| `WorkerRegistrationService` 내 workplace 참조 | company 참조 |
| `data.sql` 테이블명/컬럼명 | company 기준으로 변경 |
| 관련 테스트 전부 | 리네이밍 반영 |

### 2-3. Company CRUD API

| Method | Path | 설명 |
|--------|------|------|
| POST | `/api/companies` | 사업장 등록 |
| GET | `/api/companies` | 사업장 목록 조회 |
| GET | `/api/companies/{id}` | 사업장 상세 조회 |
| PUT | `/api/companies/{id}` | 사업장 정보 수정 |

#### DTO

```java
// CreateCompanyRequest
{ name, businessNumber, region, industryCode, employeeCount,
  foreignWorkerCount, address, contactNumber }

// UpdateCompanyRequest
{ name, region, industryCode, employeeCount,
  foreignWorkerCount, address, contactNumber }
// businessNumber는 수정 불가 (사업자등록번호는 불변)

// CompanyResponse
{ id, name, businessNumber, region, industryCode, employeeCount,
  foreignWorkerCount, address, contactNumber, createdAt, updatedAt }
```

---

## 3. 공공데이터 테이블 + CSV 적재

### 3-1. 도메인 모델 (읽기 전용)

모든 공공데이터 엔티티에 `snapshotId` + `referenceDate` 포함.

```java
// RegionalIndustry (15102368 — 분기별 지역×업종)
{ id, snapshotId, region, industry, quarter, workerCount, referenceDate }

// Manufacturing (15111730 — 제조업 중분류)
{ id, snapshotId, subIndustry, workerCount, year, referenceDate }

// VietnamE9 (15111729 — 베트남 E-9)
{ id, snapshotId, industry, totalCount, maleCount, femaleCount, referenceDate }

// Quota (HWP 수동추출 — data.sql 시드)
{ id, year, industry, quotaCount, source }
```

### 3-2. 패키지 구조

```
domain/publicdata/
├── RegionalIndustry.java
├── Manufacturing.java
├── VietnamE9.java
├── Quota.java
├── RegionalIndustryRepository.java
├── ManufacturingRepository.java
├── VietnamE9Repository.java
└── QuotaRepository.java

infrastructure/persistence/entity/
├── RegionalIndustryEntity.java
├── ManufacturingEntity.java
├── VietnamE9Entity.java
└── QuotaEntity.java

infrastructure/persistence/repository/
├── RegionalIndustryJpaRepository.java
├── RegionalIndustryRepositoryImpl.java
├── ManufacturingJpaRepository.java
├── ManufacturingRepositoryImpl.java
├── VietnamE9JpaRepository.java
├── VietnamE9RepositoryImpl.java
├── QuotaJpaRepository.java
└── QuotaRepositoryImpl.java

infrastructure/persistence/mapper/
└── PublicDataMapper.java

infrastructure/loader/
├── CsvLoader.java                    # CSV 파싱 공통 유틸
├── PublicDataInitializer.java        # ApplicationRunner
└── csv/
    ├── RegionalIndustryCsvParser.java
    ├── ManufacturingCsvParser.java
    └── VietnamE9CsvParser.java
```

### 3-3. CSV 적재 흐름

```
애플리케이션 시작
  → PublicDataInitializer (ApplicationRunner)
    → snapshotId 생성 (UUID)
    → RegionalIndustryCsvParser.parse("data/regional_industry.csv")
    → ManufacturingCsvParser.parse("data/manufacturing.csv")
    → VietnamE9CsvParser.parse("data/vietnam_e9.csv")
    → 각각 Repository.saveAll() (독립 트랜잭션)
    → 로드 결과 로깅 (건수, 소요시간)
```

### 3-4. CSV 파일 위치

```
src/main/resources/data/
├── regional_industry.csv    # 15102368
├── manufacturing.csv        # 15111730
└── vietnam_e9.csv           # 15111729
```

### 3-5. 에러 처리

- CSV 파일 누락: 경고 로그 + 해당 테이블 스킵 (애플리케이션 기동 계속)
- 파싱 실패 행: 개별 행 스킵 + 경고 로그 (전체 적재 계속)
- 각 CSV는 독립 트랜잭션 — 하나 실패해도 나머지 적재

### 3-6. 라이브러리

`build.gradle`에 `jackson-dataformat-csv` 추가. Spring Boot에 Jackson이 이미 포함되어 있으므로 추가 의존성 최소화.

---

## 4. data.sql 변경

### 4-1. 기존 사업장 시드 → company 테이블로 변경

기존 `data.sql`의 workplace INSERT를 company 테이블 기준으로 변환하고, region/industryCode/employeeCount/foreignWorkerCount 필드 추가.

### 4-2. quota 시드 추가

```sql
INSERT INTO quota (year, industry, quota_count, source) VALUES
(2025, '제조업', 36000, '도입계획'),
(2025, '농축산업', 8000, '도입계획'),
(2025, '어업', 5000, '도입계획'),
(2025, '건설업', 2000, '도입계획'),
(2025, '서비스업', 1500, '도입계획');
-- 실제 수치는 HWP에서 추출한 데이터로 대체
```

---

## 5. 테스트 전략

| 레벨 | 대상 | 테스트 |
|------|------|--------|
| 단위 | Company 도메인 모델 | 생성, 불변 업데이트, 유효성 검증 |
| 단위 | CsvParser 3종 | 정상 파싱, 누락 필드, 잘못된 형식 |
| 단위 | 공공데이터 도메인 모델 | 생성, snapshotId 포함 확인 |
| 통합 | CompanyService | CRUD 전체 흐름 |
| 통합 | PublicDataInitializer | 부팅 시 CSV 로드 → DB 저장 확인 |
| 통합 | CompanyController | REST API 요청/응답 검증 |
| 회귀 | 기존 Worker/Compliance 테스트 | 리네이밍 후 깨지지 않는지 확인 |

---

## 6. 구현 순서

```
Phase 1: Workplace → Company 리네이밍 + 필드 확장
  → 기존 테스트 전부 통과 확인 (회귀)

Phase 2: Company CRUD API
  → 테스트 작성 → 구현 → 통과

Phase 3: 공공데이터 도메인 모델 + 테이블
  → 테스트 작성 → 구현 → 통과

Phase 4: CSV 적재 파이프라인
  → 테스트 작성 → 구현 → 통과

Phase 5: data.sql에 quota 시드 추가 + company 시드 업데이트
```

---

## 7. 의존성 추가

```gradle
// build.gradle
implementation 'com.fasterxml.jackson.dataformat:jackson-dataformat-csv'
```

---

## 8. 향후 확장 포인트

이번 구현이 완료되면 다음 단계에서:
- **시뮬레이터 비즈니스 로직**: publicdata 리포지토리에서 데이터 조회 → 쿼터 소진율, 경쟁도 산출
- **Claude API 연동**: 분석 결과(숫자) → 자연어 리포트 생성
- **REST API 업로드**: CSV 갱신용 관리자 API (snapshotId 버전 관리 활용)
