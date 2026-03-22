# Stage 1 Infrastructure Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Rename Workplace→Company with SPEC field extensions, build Company CRUD API, create public data CSV loading pipeline, and add quota seed data.

**Architecture:** DDD layered architecture. Domain models are immutable (factory methods `create()`/`reconstitute()`). Infrastructure layer uses JPA entities + mappers for persistence. Controllers implement Swagger API interfaces.

**Tech Stack:** Java 21, Spring Boot 3.3.7, H2, JPA, jackson-dataformat-csv, JUnit 5 + AssertJ

**Spec:** `docs/superpowers/specs/2026-03-22-stage1-infra-design.md`

---

## File Map

### Phase 1: Rename + Extend (modify existing)

| Action | File |
|--------|------|
| Delete | `src/main/java/com/hr/fwc/domain/workplace/Workplace.java` |
| Delete | `src/main/java/com/hr/fwc/domain/workplace/WorkplaceRepository.java` |
| Create | `src/main/java/com/hr/fwc/domain/company/Company.java` |
| Create | `src/main/java/com/hr/fwc/domain/company/CompanyRepository.java` |
| Create | `src/main/java/com/hr/fwc/domain/company/Region.java` (enum) |
| Create | `src/main/java/com/hr/fwc/domain/company/IndustryCategory.java` (enum) |
| Create | `src/main/java/com/hr/fwc/domain/company/CompanyNotFoundException.java` |
| Create | `src/main/java/com/hr/fwc/domain/company/DuplicateBusinessNumberException.java` |
| Delete | `src/main/java/com/hr/fwc/infrastructure/persistence/entity/WorkplaceEntity.java` |
| Create | `src/main/java/com/hr/fwc/infrastructure/persistence/entity/CompanyEntity.java` |
| Delete | `src/main/java/com/hr/fwc/infrastructure/persistence/mapper/WorkplaceMapper.java` |
| Create | `src/main/java/com/hr/fwc/infrastructure/persistence/mapper/CompanyMapper.java` |
| Delete | `src/main/java/com/hr/fwc/infrastructure/persistence/repository/WorkplaceJpaRepository.java` |
| Create | `src/main/java/com/hr/fwc/infrastructure/persistence/repository/CompanyJpaRepository.java` |
| Delete | `src/main/java/com/hr/fwc/infrastructure/persistence/repository/WorkplaceRepositoryImpl.java` |
| Create | `src/main/java/com/hr/fwc/infrastructure/persistence/repository/CompanyRepositoryImpl.java` |
| Modify | `src/main/java/com/hr/fwc/domain/worker/EmploymentInfo.java` (workplaceId→companyId) |
| Modify | `src/main/java/com/hr/fwc/domain/worker/ForeignWorkerRepository.java` (findByWorkplaceId→findByCompanyId) |
| Modify | `src/main/java/com/hr/fwc/infrastructure/persistence/entity/EmploymentInfoEmbeddable.java` |
| Modify | `src/main/java/com/hr/fwc/infrastructure/persistence/mapper/ForeignWorkerMapper.java` |
| Modify | `src/main/java/com/hr/fwc/infrastructure/persistence/repository/ForeignWorkerJpaRepository.java` |
| Modify | `src/main/java/com/hr/fwc/infrastructure/persistence/repository/ForeignWorkerRepositoryImpl.java` |
| Modify | `src/main/java/com/hr/fwc/application/dto/RegisterWorkerRequest.java` |
| Modify | `src/main/java/com/hr/fwc/application/service/WorkerRegistrationService.java` |
| Modify | `src/main/java/com/hr/fwc/presentation/GlobalExceptionHandler.java` |
| Modify | `src/main/resources/data.sql` |
| Delete | `src/test/java/com/hr/fwc/domain/workplace/WorkplaceTest.java` |
| Create | `src/test/java/com/hr/fwc/domain/company/CompanyTest.java` |
| Modify | `src/test/java/com/hr/fwc/infrastructure/persistence/repository/RepositoryAdapterIntegrationTest.java` |
| Modify | `src/test/java/com/hr/fwc/infrastructure/persistence/mapper/PersistenceMapperTest.java` |
| Modify | `src/test/java/com/hr/fwc/infrastructure/persistence/PersistenceSchemaSmokeTest.java` |
| Modify | All other test files referencing Workplace/workplaceId |

### Phase 2: Company CRUD API (new files)

| Action | File |
|--------|------|
| Create | `src/main/java/com/hr/fwc/application/dto/CreateCompanyRequest.java` |
| Create | `src/main/java/com/hr/fwc/application/dto/UpdateCompanyRequest.java` |
| Create | `src/main/java/com/hr/fwc/application/dto/CompanyResponse.java` |
| Create | `src/main/java/com/hr/fwc/application/service/CompanyService.java` |
| Create | `src/main/java/com/hr/fwc/presentation/api/CompanyApi.java` |
| Create | `src/main/java/com/hr/fwc/presentation/CompanyController.java` |
| Create | `src/test/java/com/hr/fwc/application/service/CompanyServiceIntegrationTest.java` |
| Create | `src/test/java/com/hr/fwc/presentation/CompanyControllerTest.java` |

### Phase 3–4: Public Data + CSV (new files)

| Action | File |
|--------|------|
| Create | `src/main/java/com/hr/fwc/domain/publicdata/RegionalIndustry.java` |
| Create | `src/main/java/com/hr/fwc/domain/publicdata/Manufacturing.java` |
| Create | `src/main/java/com/hr/fwc/domain/publicdata/VietnamE9.java` |
| Create | `src/main/java/com/hr/fwc/domain/publicdata/Quota.java` |
| Create | `src/main/java/com/hr/fwc/domain/publicdata/RegionalIndustryRepository.java` |
| Create | `src/main/java/com/hr/fwc/domain/publicdata/ManufacturingRepository.java` |
| Create | `src/main/java/com/hr/fwc/domain/publicdata/VietnamE9Repository.java` |
| Create | `src/main/java/com/hr/fwc/domain/publicdata/QuotaRepository.java` |
| Create | `src/main/java/com/hr/fwc/infrastructure/persistence/entity/RegionalIndustryEntity.java` |
| Create | `src/main/java/com/hr/fwc/infrastructure/persistence/entity/ManufacturingEntity.java` |
| Create | `src/main/java/com/hr/fwc/infrastructure/persistence/entity/VietnamE9Entity.java` |
| Create | `src/main/java/com/hr/fwc/infrastructure/persistence/entity/QuotaEntity.java` |
| Create | `src/main/java/com/hr/fwc/infrastructure/persistence/mapper/PublicDataMapper.java` |
| Create | `src/main/java/com/hr/fwc/infrastructure/persistence/repository/RegionalIndustryJpaRepository.java` |
| Create | `src/main/java/com/hr/fwc/infrastructure/persistence/repository/RegionalIndustryRepositoryImpl.java` |
| Create | `src/main/java/com/hr/fwc/infrastructure/persistence/repository/ManufacturingJpaRepository.java` |
| Create | `src/main/java/com/hr/fwc/infrastructure/persistence/repository/ManufacturingRepositoryImpl.java` |
| Create | `src/main/java/com/hr/fwc/infrastructure/persistence/repository/VietnamE9JpaRepository.java` |
| Create | `src/main/java/com/hr/fwc/infrastructure/persistence/repository/VietnamE9RepositoryImpl.java` |
| Create | `src/main/java/com/hr/fwc/infrastructure/persistence/repository/QuotaJpaRepository.java` |
| Create | `src/main/java/com/hr/fwc/infrastructure/persistence/repository/QuotaRepositoryImpl.java` |
| Modify | `build.gradle` (add jackson-dataformat-csv) |
| Create | `src/main/java/com/hr/fwc/infrastructure/loader/CsvLoader.java` |
| Create | `src/main/java/com/hr/fwc/infrastructure/loader/PublicDataInitializer.java` |
| Create | `src/main/java/com/hr/fwc/infrastructure/loader/csv/RegionalIndustryCsvRow.java` |
| Create | `src/main/java/com/hr/fwc/infrastructure/loader/csv/ManufacturingCsvRow.java` |
| Create | `src/main/java/com/hr/fwc/infrastructure/loader/csv/VietnamE9CsvRow.java` |
| Create | `src/main/resources/data/regional_industry.csv` |
| Create | `src/main/resources/data/manufacturing.csv` |
| Create | `src/main/resources/data/vietnam_e9.csv` |
| Create | `src/test/java/com/hr/fwc/domain/publicdata/PublicDataDomainTest.java` |
| Create | `src/test/java/com/hr/fwc/infrastructure/loader/CsvLoaderTest.java` |
| Create | `src/test/java/com/hr/fwc/infrastructure/loader/PublicDataInitializerIntegrationTest.java` |

---

## Task 1: Region & IndustryCategory Enums

**Files:**
- Create: `src/main/java/com/hr/fwc/domain/company/Region.java`
- Create: `src/main/java/com/hr/fwc/domain/company/IndustryCategory.java`

- [ ] **Step 1: Create Region enum**

```java
package com.hr.fwc.domain.company;

public enum Region {
    SEOUL("서울특별시"),
    BUSAN("부산광역시"),
    DAEGU("대구광역시"),
    INCHEON("인천광역시"),
    GWANGJU("광주광역시"),
    DAEJEON("대전광역시"),
    ULSAN("울산광역시"),
    SEJONG("세종특별자치시"),
    GYEONGGI("경기도"),
    CHUNGBUK("충청북도"),
    CHUNGNAM("충청남도"),
    JEONBUK("전북특별자치도"),
    JEONNAM("전라남도"),
    GYEONGBUK("경상북도"),
    GYEONGNAM("경상남도"),
    GANGWON("강원특별자치도"),
    JEJU("제주특별자치도");

    private final String koreanName;

    Region(String koreanName) {
        this.koreanName = koreanName;
    }

    public String koreanName() {
        return koreanName;
    }
}
```

- [ ] **Step 2: Create IndustryCategory enum**

```java
package com.hr.fwc.domain.company;

public enum IndustryCategory {
    MANUFACTURING("제조업"),
    CONSTRUCTION("건설업"),
    AGRICULTURE("농축산업"),
    FISHING("어업"),
    SERVICE("서비스업"),
    MINING("광업"),
    TRANSPORTATION("운수업"),
    ACCOMMODATION("숙박음식점업");

    private final String koreanName;

    IndustryCategory(String koreanName) {
        this.koreanName = koreanName;
    }

    public String koreanName() {
        return koreanName;
    }
}
```

- [ ] **Step 3: Commit**

```bash
git add src/main/java/com/hr/fwc/domain/company/Region.java \
        src/main/java/com/hr/fwc/domain/company/IndustryCategory.java
git commit -m "feat: add Region and IndustryCategory enums for Company domain"
```

---

## Task 2: Company Domain Model + Tests

**Files:**
- Create: `src/main/java/com/hr/fwc/domain/company/Company.java`
- Create: `src/main/java/com/hr/fwc/domain/company/CompanyRepository.java`
- Create: `src/main/java/com/hr/fwc/domain/company/CompanyNotFoundException.java`
- Create: `src/main/java/com/hr/fwc/domain/company/DuplicateBusinessNumberException.java`
- Create: `src/test/java/com/hr/fwc/domain/company/CompanyTest.java`
- Delete: `src/main/java/com/hr/fwc/domain/workplace/Workplace.java`
- Delete: `src/main/java/com/hr/fwc/domain/workplace/WorkplaceRepository.java`
- Delete: `src/test/java/com/hr/fwc/domain/workplace/WorkplaceTest.java`

- [ ] **Step 1: Write CompanyTest**

```java
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
```

- [ ] **Step 2: Run test to verify it fails**

```bash
./gradlew test --tests "com.hr.fwc.domain.company.CompanyTest" 2>&1 | tail -5
```

Expected: FAIL — `Company` class does not exist

- [ ] **Step 3: Create Company domain model**

```java
package com.hr.fwc.domain.company;

import java.time.LocalDateTime;
import java.util.Objects;

public class Company {

    private Long id;
    private String name;
    private String businessNumber;
    private Region region;
    private String subRegion;
    private IndustryCategory industryCategory;
    private String industrySubCategory;
    private int employeeCount;
    private int foreignWorkerCount;
    private String address;
    private String contactPhone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected Company() {
    }

    private Company(String name, String businessNumber,
                    Region region, String subRegion,
                    IndustryCategory industryCategory, String industrySubCategory,
                    int employeeCount, int foreignWorkerCount,
                    String address, String contactPhone) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.businessNumber = Objects.requireNonNull(businessNumber, "Business number cannot be null");
        this.region = Objects.requireNonNull(region, "Region cannot be null");
        this.industryCategory = Objects.requireNonNull(industryCategory, "Industry category cannot be null");
        validateEmployeeCounts(employeeCount, foreignWorkerCount);
        this.subRegion = subRegion;
        this.industrySubCategory = industrySubCategory;
        this.employeeCount = employeeCount;
        this.foreignWorkerCount = foreignWorkerCount;
        this.address = address;
        this.contactPhone = contactPhone;
        this.createdAt = LocalDateTime.now();
    }

    public static Company create(String name, String businessNumber,
                                  Region region, String subRegion,
                                  IndustryCategory industryCategory, String industrySubCategory,
                                  int employeeCount, int foreignWorkerCount,
                                  String address, String contactPhone) {
        return new Company(name, businessNumber, region, subRegion,
            industryCategory, industrySubCategory,
            employeeCount, foreignWorkerCount, address, contactPhone);
    }

    public static Company reconstitute(Long id, String name, String businessNumber,
                                        Region region, String subRegion,
                                        IndustryCategory industryCategory, String industrySubCategory,
                                        int employeeCount, int foreignWorkerCount,
                                        String address, String contactPhone,
                                        LocalDateTime createdAt, LocalDateTime updatedAt) {
        Company c = new Company(name, businessNumber, region, subRegion,
            industryCategory, industrySubCategory,
            employeeCount, foreignWorkerCount, address, contactPhone);
        c.id = id;
        c.createdAt = createdAt;
        c.updatedAt = updatedAt;
        return c;
    }

    public Company updateInfo(String name, Region region, String subRegion,
                               IndustryCategory industryCategory, String industrySubCategory,
                               int employeeCount, int foreignWorkerCount,
                               String address, String contactPhone) {
        Company updated = new Company(name, this.businessNumber, region, subRegion,
            industryCategory, industrySubCategory,
            employeeCount, foreignWorkerCount, address, contactPhone);
        updated.id = this.id;
        updated.createdAt = this.createdAt;
        updated.updatedAt = LocalDateTime.now();
        return updated;
    }

    private static void validateEmployeeCounts(int employeeCount, int foreignWorkerCount) {
        if (employeeCount < 1) {
            throw new IllegalArgumentException("Employee count must be at least 1");
        }
        if (foreignWorkerCount < 0) {
            throw new IllegalArgumentException("Foreign worker count cannot be negative");
        }
        if (foreignWorkerCount > employeeCount) {
            throw new IllegalArgumentException("Foreign worker count cannot exceed employee count");
        }
    }

    // Note: address, contactPhone은 도메인에서 null 허용 (기존 Workplace 패턴 유지)
    // API 계층에서 @NotBlank로 필수 입력을 강제함 (DTO 검증)

    public Long id() { return id; }
    public String name() { return name; }
    public String businessNumber() { return businessNumber; }
    public Region region() { return region; }
    public String subRegion() { return subRegion; }
    public IndustryCategory industryCategory() { return industryCategory; }
    public String industrySubCategory() { return industrySubCategory; }
    public int employeeCount() { return employeeCount; }
    public int foreignWorkerCount() { return foreignWorkerCount; }
    public String address() { return address; }
    public String contactPhone() { return contactPhone; }
    public LocalDateTime createdAt() { return createdAt; }
    public LocalDateTime updatedAt() { return updatedAt; }
}
```

- [ ] **Step 4: Create CompanyRepository interface**

```java
package com.hr.fwc.domain.company;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository {
    Company save(Company company);
    Optional<Company> findById(Long id);
    Optional<Company> findByBusinessNumber(String businessNumber);
    List<Company> findAll();
}
```

- [ ] **Step 5: Create exception classes**

```java
// CompanyNotFoundException.java
package com.hr.fwc.domain.company;

public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException(Long id) {
        super("사업장을 찾을 수 없습니다: ID=" + id);
    }
}
```

```java
// DuplicateBusinessNumberException.java
package com.hr.fwc.domain.company;

public class DuplicateBusinessNumberException extends RuntimeException {
    public DuplicateBusinessNumberException(String businessNumber) {
        super("이미 등록된 사업자등록번호입니다: " + businessNumber);
    }
}
```

- [ ] **Step 6: Delete old Workplace files**

```bash
rm src/main/java/com/hr/fwc/domain/workplace/Workplace.java
rm src/main/java/com/hr/fwc/domain/workplace/WorkplaceRepository.java
rmdir src/main/java/com/hr/fwc/domain/workplace
rm src/test/java/com/hr/fwc/domain/workplace/WorkplaceTest.java
rmdir src/test/java/com/hr/fwc/domain/workplace
```

- [ ] **Step 7: Run CompanyTest to verify it passes**

```bash
./gradlew test --tests "com.hr.fwc.domain.company.CompanyTest" 2>&1 | tail -5
```

Expected: PASS (all 9 tests)

- [ ] **Step 8: Commit**

```bash
git add -A
git commit -m "feat: replace Workplace with Company domain model

Add Region enum (17 시도), IndustryCategory enum (8 대분류).
Company has new fields: region, subRegion, industryCategory,
industrySubCategory, employeeCount, foreignWorkerCount, updatedAt.
Domain validation: employeeCount≥1, foreignWorkerCount≤employeeCount."
```

---

## Task 3: Company Infrastructure Layer

**Files:**
- Create: `src/main/java/com/hr/fwc/infrastructure/persistence/entity/CompanyEntity.java`
- Create: `src/main/java/com/hr/fwc/infrastructure/persistence/mapper/CompanyMapper.java`
- Create: `src/main/java/com/hr/fwc/infrastructure/persistence/repository/CompanyJpaRepository.java`
- Create: `src/main/java/com/hr/fwc/infrastructure/persistence/repository/CompanyRepositoryImpl.java`
- Delete: old Workplace infrastructure files

- [ ] **Step 1: Create CompanyEntity**

```java
package com.hr.fwc.infrastructure.persistence.entity;

import com.hr.fwc.domain.company.IndustryCategory;
import com.hr.fwc.domain.company.Region;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "companies")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "business_number", nullable = false, unique = true, length = 20)
    private String businessNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false, length = 30)
    private Region region;

    @Column(name = "sub_region", length = 50)
    private String subRegion;

    @Enumerated(EnumType.STRING)
    @Column(name = "industry_category", nullable = false, length = 30)
    private IndustryCategory industryCategory;

    @Column(name = "industry_sub_category", length = 50)
    private String industrySubCategory;

    @Column(name = "employee_count", nullable = false)
    private int employeeCount;

    @Column(name = "foreign_worker_count", nullable = false)
    private int foreignWorkerCount;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and setters for each field (same pattern as WorkplaceEntity)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBusinessNumber() { return businessNumber; }
    public void setBusinessNumber(String businessNumber) { this.businessNumber = businessNumber; }
    public Region getRegion() { return region; }
    public void setRegion(Region region) { this.region = region; }
    public String getSubRegion() { return subRegion; }
    public void setSubRegion(String subRegion) { this.subRegion = subRegion; }
    public IndustryCategory getIndustryCategory() { return industryCategory; }
    public void setIndustryCategory(IndustryCategory industryCategory) { this.industryCategory = industryCategory; }
    public String getIndustrySubCategory() { return industrySubCategory; }
    public void setIndustrySubCategory(String industrySubCategory) { this.industrySubCategory = industrySubCategory; }
    public int getEmployeeCount() { return employeeCount; }
    public void setEmployeeCount(int employeeCount) { this.employeeCount = employeeCount; }
    public int getForeignWorkerCount() { return foreignWorkerCount; }
    public void setForeignWorkerCount(int foreignWorkerCount) { this.foreignWorkerCount = foreignWorkerCount; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
```

- [ ] **Step 2: Create CompanyMapper**

```java
package com.hr.fwc.infrastructure.persistence.mapper;

import com.hr.fwc.domain.company.Company;
import com.hr.fwc.infrastructure.persistence.entity.CompanyEntity;

public final class CompanyMapper {

    private CompanyMapper() {
    }

    public static CompanyEntity toEntity(Company domain) {
        CompanyEntity entity = new CompanyEntity();
        entity.setId(domain.id());
        entity.setName(domain.name());
        entity.setBusinessNumber(domain.businessNumber());
        entity.setRegion(domain.region());
        entity.setSubRegion(domain.subRegion());
        entity.setIndustryCategory(domain.industryCategory());
        entity.setIndustrySubCategory(domain.industrySubCategory());
        entity.setEmployeeCount(domain.employeeCount());
        entity.setForeignWorkerCount(domain.foreignWorkerCount());
        entity.setAddress(domain.address());
        entity.setContactPhone(domain.contactPhone());
        entity.setCreatedAt(domain.createdAt());
        entity.setUpdatedAt(domain.updatedAt());
        return entity;
    }

    public static Company toDomain(CompanyEntity entity) {
        return Company.reconstitute(
            entity.getId(),
            entity.getName(),
            entity.getBusinessNumber(),
            entity.getRegion(),
            entity.getSubRegion(),
            entity.getIndustryCategory(),
            entity.getIndustrySubCategory(),
            entity.getEmployeeCount(),
            entity.getForeignWorkerCount(),
            entity.getAddress(),
            entity.getContactPhone(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
```

- [ ] **Step 3: Create CompanyJpaRepository**

```java
package com.hr.fwc.infrastructure.persistence.repository;

import com.hr.fwc.infrastructure.persistence.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, Long> {
    Optional<CompanyEntity> findByBusinessNumber(String businessNumber);
}
```

- [ ] **Step 4: Create CompanyRepositoryImpl**

```java
package com.hr.fwc.infrastructure.persistence.repository;

import com.hr.fwc.domain.company.Company;
import com.hr.fwc.domain.company.CompanyRepository;
import com.hr.fwc.infrastructure.persistence.mapper.CompanyMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {

    private final CompanyJpaRepository jpaRepository;

    public CompanyRepositoryImpl(CompanyJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Company save(Company company) {
        return CompanyMapper.toDomain(jpaRepository.save(CompanyMapper.toEntity(company)));
    }

    @Override
    public Optional<Company> findById(Long id) {
        return jpaRepository.findById(id).map(CompanyMapper::toDomain);
    }

    @Override
    public Optional<Company> findByBusinessNumber(String businessNumber) {
        return jpaRepository.findByBusinessNumber(businessNumber).map(CompanyMapper::toDomain);
    }

    @Override
    public List<Company> findAll() {
        return jpaRepository.findAll().stream().map(CompanyMapper::toDomain).toList();
    }
}
```

- [ ] **Step 5: Delete old Workplace infrastructure files**

```bash
rm src/main/java/com/hr/fwc/infrastructure/persistence/entity/WorkplaceEntity.java
rm src/main/java/com/hr/fwc/infrastructure/persistence/mapper/WorkplaceMapper.java
rm src/main/java/com/hr/fwc/infrastructure/persistence/repository/WorkplaceJpaRepository.java
rm src/main/java/com/hr/fwc/infrastructure/persistence/repository/WorkplaceRepositoryImpl.java
```

- [ ] **Step 6: Commit**

```bash
git add -A
git commit -m "feat: add Company infrastructure layer (entity, mapper, repository)

Replace WorkplaceEntity/Mapper/JpaRepository/RepositoryImpl with Company equivalents.
Add CompanyEntity with new columns: region, sub_region, industry_category,
industry_sub_category, employee_count, foreign_worker_count, updated_at."
```

---

## Task 4: Rename workplaceId → companyId in Worker Domain

**Files:**
- Modify: `src/main/java/com/hr/fwc/domain/worker/EmploymentInfo.java`
- Modify: `src/main/java/com/hr/fwc/domain/worker/ForeignWorkerRepository.java`
- Modify: `src/main/java/com/hr/fwc/infrastructure/persistence/entity/EmploymentInfoEmbeddable.java`
- Modify: `src/main/java/com/hr/fwc/infrastructure/persistence/mapper/ForeignWorkerMapper.java`
- Modify: `src/main/java/com/hr/fwc/infrastructure/persistence/repository/ForeignWorkerJpaRepository.java`
- Modify: `src/main/java/com/hr/fwc/infrastructure/persistence/repository/ForeignWorkerRepositoryImpl.java`
- Modify: `src/main/java/com/hr/fwc/application/dto/RegisterWorkerRequest.java`
- Modify: `src/main/java/com/hr/fwc/application/service/WorkerRegistrationService.java`

- [ ] **Step 1: EmploymentInfo — workplaceId → companyId**

In `EmploymentInfo.java`:
- Field: `workplaceId` → `companyId`
- Factory method parameter: `workplaceId` → `companyId`
- Getter: `workplaceId()` → `companyId()`

- [ ] **Step 2: EmploymentInfoEmbeddable — workplaceId → companyId**

In `EmploymentInfoEmbeddable.java`:
- Field: `workplaceId` → `companyId`
- Column: `@Column(name = "workplace_id")` → `@Column(name = "company_id")`
- Getter/setter: `getWorkplaceId()/setWorkplaceId()` → `getCompanyId()/setCompanyId()`

- [ ] **Step 3: ForeignWorkerMapper — update references**

In `ForeignWorkerMapper.java`:
- `toEmploymentInfoEmbeddable()`: `embeddable.setWorkplaceId(...)` → `embeddable.setCompanyId(...)`
- `toEmploymentInfo()`: `embeddable.getWorkplaceId()` → `embeddable.getCompanyId()`

- [ ] **Step 4: ForeignWorkerRepository — findByWorkplaceId → findByCompanyId**

In `ForeignWorkerRepository.java`:
- `findByWorkplaceId(Long workplaceId)` → `findByCompanyId(Long companyId)`

In `ForeignWorkerJpaRepository.java`:
- `findByEmploymentInfoWorkplaceId(Long)` → `findByEmploymentInfoCompanyId(Long)` (no underscores — Spring Data convention)

In `ForeignWorkerRepositoryImpl.java`:
- Update method name and delegation

- [ ] **Step 5: RegisterWorkerRequest — workplaceId → companyId**

In `RegisterWorkerRequest.java`:
- `Long workplaceId` → `Long companyId`
- `@Schema` description 업데이트

- [ ] **Step 6: WorkerRegistrationService — update reference**

In `WorkerRegistrationService.java` line 51:
- `request.workplaceId()` → `request.companyId()`

- [ ] **Step 7: Commit**

```bash
git add -A
git commit -m "refactor: rename workplaceId to companyId across worker domain

Update EmploymentInfo, EmploymentInfoEmbeddable, ForeignWorkerMapper,
ForeignWorkerRepository, RegisterWorkerRequest, WorkerRegistrationService."
```

---

## Task 5: Update data.sql + GlobalExceptionHandler

**Files:**
- Modify: `src/main/resources/data.sql`
- Modify: `src/main/java/com/hr/fwc/presentation/GlobalExceptionHandler.java`

- [ ] **Step 1: Update data.sql — workplaces → companies with new fields**

```sql
-- Company seed data
-- 앱 시작 시 JPA 스키마 생성 후 자동 실행 (defer-datasource-initialization=true 필요)

INSERT INTO companies (name, business_number, region, sub_region, industry_category, industry_sub_category, employee_count, foreign_worker_count, address, contact_phone, created_at)
VALUES ('한국제조(주)', '123-45-67890', 'GYEONGGI', '안산시', 'MANUFACTURING', '금속가공제품제조업', 150, 45, '경기도 안산시 단원구 공단1로 10', '031-123-4567', NOW());

INSERT INTO companies (name, business_number, region, sub_region, industry_category, industry_sub_category, employee_count, foreign_worker_count, address, contact_phone, created_at)
VALUES ('그린농장', '234-56-78901', 'CHUNGNAM', '논산시', 'AGRICULTURE', NULL, 30, 12, '충남 논산시 연산면 농장길 25', '041-234-5678', NOW());

INSERT INTO companies (name, business_number, region, sub_region, industry_category, industry_sub_category, employee_count, foreign_worker_count, address, contact_phone, created_at)
VALUES ('대한건설(주)', '345-67-89012', 'SEOUL', '강남구', 'CONSTRUCTION', NULL, 200, 60, '서울 강남구 테헤란로 100', '02-345-6789', NOW());

INSERT INTO companies (name, business_number, region, sub_region, industry_category, industry_sub_category, employee_count, foreign_worker_count, address, contact_phone, created_at)
VALUES ('서울식품(주)', '456-78-90123', 'GYEONGGI', '화성시', 'MANUFACTURING', '식료품제조업', 80, 25, '경기도 화성시 동탄대로 50', '031-456-7890', NOW());

INSERT INTO companies (name, business_number, region, sub_region, industry_category, industry_sub_category, employee_count, foreign_worker_count, address, contact_phone, created_at)
VALUES ('글로벌IT(주)', '567-89-01234', 'SEOUL', '구로구', 'SERVICE', NULL, 50, 8, '서울 구로구 디지털로 300', '02-567-8901', NOW());

INSERT INTO companies (name, business_number, region, sub_region, industry_category, industry_sub_category, employee_count, foreign_worker_count, address, contact_phone, created_at)
VALUES ('해양수산(주)', '678-90-12345', 'BUSAN', '사하구', 'FISHING', NULL, 40, 15, '부산 사하구 낙동남로 200', '051-678-9012', NOW());

INSERT INTO companies (name, business_number, region, sub_region, industry_category, industry_sub_category, employee_count, foreign_worker_count, address, contact_phone, created_at)
VALUES ('스마트물류(주)', '789-01-23456', 'INCHEON', '서구', 'TRANSPORTATION', NULL, 120, 30, '인천 서구 청라대로 150', '032-789-0123', NOW());

INSERT INTO companies (name, business_number, region, sub_region, industry_category, industry_sub_category, employee_count, foreign_worker_count, address, contact_phone, created_at)
VALUES ('코리아호텔', '890-12-34567', 'JEJU', '제주시', 'ACCOMMODATION', NULL, 60, 18, '제주 제주시 관광로 80', '064-890-1234', NOW());
```

- [ ] **Step 2: Add Company exceptions to GlobalExceptionHandler**

Add handlers for `CompanyNotFoundException` and `DuplicateBusinessNumberException`:

```java
@ExceptionHandler(CompanyNotFoundException.class)
public ResponseEntity<ErrorResponse> handleCompanyNotFound(CompanyNotFoundException ex) {
    log.warn("사업장 조회 실패: {}", ex.getMessage());
    return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
}

@ExceptionHandler(DuplicateBusinessNumberException.class)
public ResponseEntity<ErrorResponse> handleDuplicateBusinessNumber(DuplicateBusinessNumberException ex) {
    log.warn("중복 사업자등록번호: {}", ex.getMessage());
    return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
}
```

- [ ] **Step 3: Commit**

```bash
git add -A
git commit -m "refactor: update data.sql for companies table and add exception handlers

Migrate workplaces seed → companies with region, industryCategory, counts.
Add CompanyNotFoundException and DuplicateBusinessNumberException handlers."
```

---

## Task 6: Update All Tests for Renaming + Regression

**Files:**
- Modify: `src/test/java/com/hr/fwc/infrastructure/persistence/repository/RepositoryAdapterIntegrationTest.java`
- Modify: All other test files referencing Workplace/workplaceId

- [ ] **Step 1: Update RepositoryAdapterIntegrationTest**

Replace all `Workplace` → `Company`, `WorkplaceRepository` → `CompanyRepository`, `workplaceId` → `companyId` references. `Workplace.create(name, bizNum, addr, phone)` calls become `Company.create(name, bizNum, Region.X, null, IndustryCategory.X, null, count, fwCount, addr, phone)`. Also `findByWorkplaceId()` → `findByCompanyId()`.

- [ ] **Step 2: Update PersistenceMapperTest**

- Replace `import ...workplace.Workplace` → `import ...company.Company`
- Replace `import ...WorkplaceMapper` → `import ...CompanyMapper`
- Rewrite `workplaceMapperShouldPreserveFields()` → `companyMapperShouldPreserveFields()`:
  - `Company.reconstitute(25L, "Sample Factory", "123-45-67890", Region.GYEONGGI, null, IndustryCategory.MANUFACTURING, null, 100, 20, "Seoul", "02-1234-5678", createdAt, null)`
  - Assert all new fields (region, industryCategory, employeeCount, etc.)
- In `foreignWorkerMapperShouldPreserveFields()`:
  - `.workplaceId()` → `.companyId()` (line 40)

- [ ] **Step 3: Update PersistenceSchemaSmokeTest**

- Rename test: `workplaceTableShouldContainBusinessColumns()` → `companyTableShouldContainBusinessColumns()`
- Change `columnsOf("WORKPLACES")` → `columnsOf("COMPANIES")`
- Add new column assertions: `REGION`, `SUB_REGION`, `INDUSTRY_CATEGORY`, `INDUSTRY_SUB_CATEGORY`, `EMPLOYEE_COUNT`, `FOREIGN_WORKER_COUNT`, `UPDATED_AT`
- In `foreignWorkerTableShouldContainEmbeddedAndAuditColumns()`:
  - `WORKPLACE_ID` → `COMPANY_ID`

- [ ] **Step 4: Update all remaining tests**

Search for any remaining `Workplace`, `workplaceId`, `workplace` references in test files and update.

- [ ] **Step 3: Run full test suite**

```bash
./gradlew test 2>&1 | tail -20
```

Expected: ALL tests pass (regression confirmed)

- [ ] **Step 4: Commit**

```bash
git add -A
git commit -m "test: update all tests for Workplace→Company renaming

Regression confirmed: all existing tests pass with new Company model."
```

---

## Task 7: Update CLAUDE.md Package Structure

**Files:**
- Modify: `CLAUDE.md`

- [ ] **Step 1: Update package structure in CLAUDE.md**

Replace `workplace/` references with `company/` in the architecture section. Add `domain/publicdata/` placeholder.

- [ ] **Step 2: Commit**

```bash
git add CLAUDE.md
git commit -m "docs: sync CLAUDE.md with Workplace→Company renaming"
```

---

## Task 8: Company CRUD — DTOs

**Files:**
- Create: `src/main/java/com/hr/fwc/application/dto/CreateCompanyRequest.java`
- Create: `src/main/java/com/hr/fwc/application/dto/UpdateCompanyRequest.java`
- Create: `src/main/java/com/hr/fwc/application/dto/CompanyResponse.java`

- [ ] **Step 1: Create CreateCompanyRequest**

```java
package com.hr.fwc.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "사업장 등록 요청")
public record CreateCompanyRequest(
    @NotBlank @Size(max = 100)
    @Schema(description = "사업장명", example = "한국제조(주)", requiredMode = Schema.RequiredMode.REQUIRED)
    String name,

    @NotBlank @Pattern(regexp = "\\d{3}-\\d{2}-\\d{5}", message = "사업자등록번호 형식이 올바르지 않습니다 (XXX-XX-XXXXX)")
    @Schema(description = "사업자등록번호", example = "123-45-67890", requiredMode = Schema.RequiredMode.REQUIRED)
    String businessNumber,

    @NotNull
    @Schema(description = "시도", example = "GYEONGGI", requiredMode = Schema.RequiredMode.REQUIRED)
    String region,

    @Schema(description = "시군구", example = "안산시")
    String subRegion,

    @NotNull
    @Schema(description = "업종 대분류", example = "MANUFACTURING", requiredMode = Schema.RequiredMode.REQUIRED)
    String industryCategory,

    @Schema(description = "업종 중분류", example = "금속가공제품제조업")
    String industrySubCategory,

    @Min(1)
    @Schema(description = "상시 근로자 수", example = "100", requiredMode = Schema.RequiredMode.REQUIRED)
    int employeeCount,

    @Min(0)
    @Schema(description = "외국인 근로자 수", example = "20", requiredMode = Schema.RequiredMode.REQUIRED)
    int foreignWorkerCount,

    @NotBlank
    @Schema(description = "주소", example = "경기도 안산시 단원구 공단1로 10", requiredMode = Schema.RequiredMode.REQUIRED)
    String address,

    @NotBlank
    @Schema(description = "연락처", example = "031-123-4567", requiredMode = Schema.RequiredMode.REQUIRED)
    String contactPhone
) {}
```

- [ ] **Step 2: Create UpdateCompanyRequest**

Same as Create but without `businessNumber` (immutable).

```java
package com.hr.fwc.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "사업장 수정 요청")
public record UpdateCompanyRequest(
    @NotBlank @Size(max = 100)
    @Schema(description = "사업장명", example = "한국제조(주)")
    String name,

    @NotNull
    @Schema(description = "시도", example = "GYEONGGI")
    String region,

    @Schema(description = "시군구", example = "안산시")
    String subRegion,

    @NotNull
    @Schema(description = "업종 대분류", example = "MANUFACTURING")
    String industryCategory,

    @Schema(description = "업종 중분류", example = "금속가공제품제조업")
    String industrySubCategory,

    @Min(1)
    @Schema(description = "상시 근로자 수", example = "100")
    int employeeCount,

    @Min(0)
    @Schema(description = "외국인 근로자 수", example = "20")
    int foreignWorkerCount,

    @NotBlank
    @Schema(description = "주소", example = "경기도 안산시 단원구 공단1로 10")
    String address,

    @NotBlank
    @Schema(description = "연락처", example = "031-123-4567")
    String contactPhone
) {}
```

- [ ] **Step 3: Create CompanyResponse**

```java
package com.hr.fwc.application.dto;

import com.hr.fwc.domain.company.Company;

import java.time.LocalDateTime;

public record CompanyResponse(
    Long id,
    String name,
    String businessNumber,
    String region,
    String regionName,
    String subRegion,
    String industryCategory,
    String industryCategoryName,
    String industrySubCategory,
    int employeeCount,
    int foreignWorkerCount,
    String address,
    String contactPhone,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static CompanyResponse from(Company company) {
        return new CompanyResponse(
            company.id(),
            company.name(),
            company.businessNumber(),
            company.region().name(),
            company.region().koreanName(),
            company.subRegion(),
            company.industryCategory().name(),
            company.industryCategory().koreanName(),
            company.industrySubCategory(),
            company.employeeCount(),
            company.foreignWorkerCount(),
            company.address(),
            company.contactPhone(),
            company.createdAt(),
            company.updatedAt()
        );
    }
}
```

- [ ] **Step 4: Commit**

```bash
git add src/main/java/com/hr/fwc/application/dto/CreateCompanyRequest.java \
        src/main/java/com/hr/fwc/application/dto/UpdateCompanyRequest.java \
        src/main/java/com/hr/fwc/application/dto/CompanyResponse.java
git commit -m "feat: add Company CRUD DTOs with validation rules"
```

---

## Task 9: CompanyService + Integration Test

**Files:**
- Create: `src/main/java/com/hr/fwc/application/service/CompanyService.java`
- Create: `src/test/java/com/hr/fwc/application/service/CompanyServiceIntegrationTest.java`

- [ ] **Step 1: Write CompanyServiceIntegrationTest**

```java
package com.hr.fwc.application.service;

import com.hr.fwc.application.dto.CompanyResponse;
import com.hr.fwc.application.dto.CreateCompanyRequest;
import com.hr.fwc.application.dto.UpdateCompanyRequest;
import com.hr.fwc.domain.company.CompanyNotFoundException;
import com.hr.fwc.domain.company.DuplicateBusinessNumberException;
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
            "테스트 제조", "999-99-99999", "GYEONGGI", "안산시",
            "MANUFACTURING", "금속가공", 100, 20, "경기도 안산시", "031-000-0000"
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
        // data.sql에서 8개 시드 데이터가 있으므로
        List<CompanyResponse> all = companyService.getAllCompanies();
        assertThat(all).hasSizeGreaterThanOrEqualTo(8);
    }

    @Test
    @DisplayName("사업장 정보를 수정할 수 있다")
    void 사업장_정보_수정() {
        CreateCompanyRequest request = new CreateCompanyRequest(
            "원래이름", "888-88-88888", "SEOUL", null,
            "SERVICE", null, 50, 5, "서울", "02-000-0000"
        );
        CompanyResponse created = companyService.createCompany(request);

        UpdateCompanyRequest update = new UpdateCompanyRequest(
            "변경이름", "BUSAN", "해운대구",
            "ACCOMMODATION", null, 80, 15, "부산", "051-000-0000"
        );
        CompanyResponse updated = companyService.updateCompany(created.id(), update);

        assertThat(updated.name()).isEqualTo("변경이름");
        assertThat(updated.businessNumber()).isEqualTo("888-88-88888"); // 불변
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
            "첫번째", "777-77-77777", "SEOUL", null,
            "SERVICE", null, 10, 0, "서울", "02-000"
        );
        companyService.createCompany(request);

        CreateCompanyRequest duplicate = new CreateCompanyRequest(
            "두번째", "777-77-77777", "BUSAN", null,
            "CONSTRUCTION", null, 20, 0, "부산", "051-000"
        );
        assertThatThrownBy(() -> companyService.createCompany(duplicate))
            .isInstanceOf(DuplicateBusinessNumberException.class);
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

```bash
./gradlew test --tests "com.hr.fwc.application.service.CompanyServiceIntegrationTest" 2>&1 | tail -5
```

Expected: FAIL — `CompanyService` does not exist

- [ ] **Step 3: Create CompanyService**

```java
package com.hr.fwc.application.service;

import com.hr.fwc.application.dto.CompanyResponse;
import com.hr.fwc.application.dto.CreateCompanyRequest;
import com.hr.fwc.application.dto.UpdateCompanyRequest;
import com.hr.fwc.domain.company.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional
    public CompanyResponse createCompany(CreateCompanyRequest request) {
        companyRepository.findByBusinessNumber(request.businessNumber())
            .ifPresent(existing -> {
                throw new DuplicateBusinessNumberException(request.businessNumber());
            });

        Company company = Company.create(
            request.name(),
            request.businessNumber(),
            Region.valueOf(request.region()),
            request.subRegion(),
            IndustryCategory.valueOf(request.industryCategory()),
            request.industrySubCategory(),
            request.employeeCount(),
            request.foreignWorkerCount(),
            request.address(),
            request.contactPhone()
        );

        return CompanyResponse.from(companyRepository.save(company));
    }

    public CompanyResponse getCompany(Long id) {
        Company company = companyRepository.findById(id)
            .orElseThrow(() -> new CompanyNotFoundException(id));
        return CompanyResponse.from(company);
    }

    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll().stream()
            .map(CompanyResponse::from)
            .toList();
    }

    @Transactional
    public CompanyResponse updateCompany(Long id, UpdateCompanyRequest request) {
        Company existing = companyRepository.findById(id)
            .orElseThrow(() -> new CompanyNotFoundException(id));

        Company updated = existing.updateInfo(
            request.name(),
            Region.valueOf(request.region()),
            request.subRegion(),
            IndustryCategory.valueOf(request.industryCategory()),
            request.industrySubCategory(),
            request.employeeCount(),
            request.foreignWorkerCount(),
            request.address(),
            request.contactPhone()
        );

        return CompanyResponse.from(companyRepository.save(updated));
    }
}
```

- [ ] **Step 4: Run test to verify it passes**

```bash
./gradlew test --tests "com.hr.fwc.application.service.CompanyServiceIntegrationTest" 2>&1 | tail -10
```

Expected: PASS (all 5 tests)

- [ ] **Step 5: Commit**

```bash
git add -A
git commit -m "feat: add CompanyService with CRUD operations and integration tests"
```

---

## Task 10: CompanyController + API Interface + Test

**Files:**
- Create: `src/main/java/com/hr/fwc/presentation/api/CompanyApi.java`
- Create: `src/main/java/com/hr/fwc/presentation/CompanyController.java`
- Create: `src/test/java/com/hr/fwc/presentation/CompanyControllerTest.java`

- [ ] **Step 1: Write CompanyControllerTest**

```java
package com.hr.fwc.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hr.fwc.application.dto.CreateCompanyRequest;
import com.hr.fwc.application.dto.UpdateCompanyRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/companies — 사업장 등록 성공")
    void 사업장_등록_성공() throws Exception {
        CreateCompanyRequest request = new CreateCompanyRequest(
            "테스트회사", "111-11-11111", "SEOUL", null,
            "MANUFACTURING", null, 50, 10, "서울시", "02-0000-0000"
        );

        mockMvc.perform(post("/api/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.name").value("테스트회사"))
            .andExpect(jsonPath("$.regionName").value("서울특별시"));
    }

    @Test
    @DisplayName("GET /api/companies — 사업장 목록 조회")
    void 사업장_목록_조회() throws Exception {
        mockMvc.perform(get("/api/companies"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(8)))); // seed data
    }

    @Test
    @DisplayName("GET /api/companies/{id} — 사업장 상세 조회")
    void 사업장_상세_조회() throws Exception {
        mockMvc.perform(get("/api/companies/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.businessNumber").value("123-45-67890"));
    }

    @Test
    @DisplayName("PUT /api/companies/{id} — 사업장 수정 성공")
    void 사업장_수정_성공() throws Exception {
        UpdateCompanyRequest request = new UpdateCompanyRequest(
            "변경이름", "BUSAN", null,
            "CONSTRUCTION", null, 200, 50, "부산", "051-000"
        );

        mockMvc.perform(put("/api/companies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("변경이름"))
            .andExpect(jsonPath("$.businessNumber").value("123-45-67890")); // 불변
    }

    @Test
    @DisplayName("POST /api/companies — 유효성 검증 실패 시 400")
    void 유효성_검증_실패() throws Exception {
        CreateCompanyRequest invalid = new CreateCompanyRequest(
            "", "invalid", "SEOUL", null,
            "MANUFACTURING", null, 0, -1, "", ""
        );

        mockMvc.perform(post("/api/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/companies/99999 — 존재하지 않는 사업장 404")
    void 존재하지_않는_사업장_404() throws Exception {
        mockMvc.perform(get("/api/companies/99999"))
            .andExpect(status().isNotFound());
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

```bash
./gradlew test --tests "com.hr.fwc.presentation.CompanyControllerTest" 2>&1 | tail -5
```

Expected: FAIL — endpoint not found

- [ ] **Step 3: Create CompanyApi interface**

```java
package com.hr.fwc.presentation.api;

import com.hr.fwc.application.dto.CompanyResponse;
import com.hr.fwc.application.dto.CreateCompanyRequest;
import com.hr.fwc.application.dto.UpdateCompanyRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "사업장 관리", description = "사업장 등록 및 관리 API")
public interface CompanyApi {

    @Operation(summary = "사업장 등록")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "등록 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "409", description = "중복 사업자등록번호")
    })
    ResponseEntity<CompanyResponse> createCompany(CreateCompanyRequest request);

    @Operation(summary = "사업장 목록 조회")
    ResponseEntity<List<CompanyResponse>> getAllCompanies();

    @Operation(summary = "사업장 상세 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "사업장을 찾을 수 없음")
    })
    ResponseEntity<CompanyResponse> getCompanyById(
        @Parameter(description = "사업장 ID", required = true) Long id
    );

    @Operation(summary = "사업장 정보 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "404", description = "사업장을 찾을 수 없음")
    })
    ResponseEntity<CompanyResponse> updateCompany(
        @Parameter(description = "사업장 ID", required = true) Long id,
        UpdateCompanyRequest request
    );
}
```

- [ ] **Step 4: Create CompanyController**

```java
package com.hr.fwc.presentation;

import com.hr.fwc.application.dto.CompanyResponse;
import com.hr.fwc.application.dto.CreateCompanyRequest;
import com.hr.fwc.application.dto.UpdateCompanyRequest;
import com.hr.fwc.application.service.CompanyService;
import com.hr.fwc.presentation.api.CompanyApi;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController implements CompanyApi {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@Valid @RequestBody CreateCompanyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.createCompany(request));
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompany(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(@PathVariable Long id,
                                                          @Valid @RequestBody UpdateCompanyRequest request) {
        return ResponseEntity.ok(companyService.updateCompany(id, request));
    }
}
```

- [ ] **Step 5: Add MethodArgumentNotValidException + IllegalArgumentException handlers to GlobalExceptionHandler**

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getFieldErrors().stream()
        .map(e -> e.getField() + ": " + e.getDefaultMessage())
        .reduce((a, b) -> a + ", " + b)
        .orElse("유효성 검증 실패");
    log.warn("유효성 검증 실패: {}", message);
    return buildResponse(HttpStatus.BAD_REQUEST, message);
}

@ExceptionHandler(IllegalArgumentException.class)
public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
    log.warn("잘못된 요청 파라미터: {}", ex.getMessage());
    return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
}
```

> `IllegalArgumentException` 핸들러: `Region.valueOf()`, `IndustryCategory.valueOf()` 실패 시 500 대신 400 반환.

- [ ] **Step 6: Run test to verify it passes**

```bash
./gradlew test --tests "com.hr.fwc.presentation.CompanyControllerTest" 2>&1 | tail -10
```

Expected: PASS (all 6 tests)

- [ ] **Step 7: Run full test suite**

```bash
./gradlew test 2>&1 | tail -10
```

Expected: ALL tests pass

- [ ] **Step 8: Commit**

```bash
git add -A
git commit -m "feat: add Company CRUD REST API with validation

POST/GET/PUT /api/companies endpoints.
CompanyApi Swagger interface, CompanyController, validation error handling."
```

---

## Task 11: Public Data Domain Models + Tests

**Files:**
- Create: `src/main/java/com/hr/fwc/domain/publicdata/RegionalIndustry.java`
- Create: `src/main/java/com/hr/fwc/domain/publicdata/Manufacturing.java`
- Create: `src/main/java/com/hr/fwc/domain/publicdata/VietnamE9.java`
- Create: `src/main/java/com/hr/fwc/domain/publicdata/Quota.java`
- Create: `src/main/java/com/hr/fwc/domain/publicdata/RegionalIndustryRepository.java`
- Create: `src/main/java/com/hr/fwc/domain/publicdata/ManufacturingRepository.java`
- Create: `src/main/java/com/hr/fwc/domain/publicdata/VietnamE9Repository.java`
- Create: `src/main/java/com/hr/fwc/domain/publicdata/QuotaRepository.java`
- Create: `src/test/java/com/hr/fwc/domain/publicdata/PublicDataDomainTest.java`

- [ ] **Step 1: Write PublicDataDomainTest**

```java
package com.hr.fwc.domain.publicdata;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

class PublicDataDomainTest {

    @Test
    @DisplayName("RegionalIndustry 생성 시 snapshotId와 필드가 올바르게 설정됨")
    void RegionalIndustry_생성() {
        RegionalIndustry ri = RegionalIndustry.create(
            "snap-1", "서울특별시", "제조업", "2025Q1", 12500, LocalDate.of(2025, 3, 31)
        );
        assertThat(ri.snapshotId()).isEqualTo("snap-1");
        assertThat(ri.region()).isEqualTo("서울특별시");
        assertThat(ri.industry()).isEqualTo("제조업");
        assertThat(ri.quarter()).isEqualTo("2025Q1");
        assertThat(ri.workerCount()).isEqualTo(12500);
        assertThat(ri.referenceDate()).isEqualTo(LocalDate.of(2025, 3, 31));
    }

    @Test
    @DisplayName("Manufacturing 생성 시 snapshotId와 필드가 올바르게 설정됨")
    void Manufacturing_생성() {
        Manufacturing m = Manufacturing.create(
            "snap-1", "식료품제조업", 8500, 2024, LocalDate.of(2024, 12, 31)
        );
        assertThat(m.snapshotId()).isEqualTo("snap-1");
        assertThat(m.subIndustry()).isEqualTo("식료품제조업");
        assertThat(m.workerCount()).isEqualTo(8500);
        assertThat(m.year()).isEqualTo(2024);
    }

    @Test
    @DisplayName("VietnamE9 생성 시 snapshotId와 필드가 올바르게 설정됨")
    void VietnamE9_생성() {
        VietnamE9 v = VietnamE9.create(
            "snap-1", "제조업", 45000, 32000, 13000, LocalDate.of(2024, 12, 31)
        );
        assertThat(v.snapshotId()).isEqualTo("snap-1");
        assertThat(v.totalCount()).isEqualTo(45000);
        assertThat(v.maleCount()).isEqualTo(32000);
        assertThat(v.femaleCount()).isEqualTo(13000);
    }

    @Test
    @DisplayName("Quota 생성 시 snapshotId 없이 필드가 올바르게 설정됨")
    void Quota_생성() {
        Quota q = Quota.create(2025, "제조업", 36000, "도입계획");
        assertThat(q.year()).isEqualTo(2025);
        assertThat(q.industry()).isEqualTo("제조업");
        assertThat(q.quotaCount()).isEqualTo(36000);
        assertThat(q.source()).isEqualTo("도입계획");
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

```bash
./gradlew test --tests "com.hr.fwc.domain.publicdata.PublicDataDomainTest" 2>&1 | tail -5
```

Expected: FAIL — classes don't exist

- [ ] **Step 3: Create RegionalIndustry domain model**

```java
package com.hr.fwc.domain.publicdata;

import java.time.LocalDate;
import java.util.Objects;

public class RegionalIndustry {

    private Long id;
    private String snapshotId;
    private String region;
    private String industry;
    private String quarter;
    private int workerCount;
    private LocalDate referenceDate;

    protected RegionalIndustry() {}

    private RegionalIndustry(String snapshotId, String region, String industry,
                              String quarter, int workerCount, LocalDate referenceDate) {
        this.snapshotId = Objects.requireNonNull(snapshotId);
        this.region = Objects.requireNonNull(region);
        this.industry = Objects.requireNonNull(industry);
        this.quarter = Objects.requireNonNull(quarter);
        this.workerCount = workerCount;
        this.referenceDate = Objects.requireNonNull(referenceDate);
    }

    public static RegionalIndustry create(String snapshotId, String region, String industry,
                                           String quarter, int workerCount, LocalDate referenceDate) {
        return new RegionalIndustry(snapshotId, region, industry, quarter, workerCount, referenceDate);
    }

    public static RegionalIndustry reconstitute(Long id, String snapshotId, String region, String industry,
                                                 String quarter, int workerCount, LocalDate referenceDate) {
        RegionalIndustry ri = new RegionalIndustry(snapshotId, region, industry, quarter, workerCount, referenceDate);
        ri.id = id;
        return ri;
    }

    public Long id() { return id; }
    public String snapshotId() { return snapshotId; }
    public String region() { return region; }
    public String industry() { return industry; }
    public String quarter() { return quarter; }
    public int workerCount() { return workerCount; }
    public LocalDate referenceDate() { return referenceDate; }
}
```

- [ ] **Step 4: Create Manufacturing domain model**

```java
package com.hr.fwc.domain.publicdata;

import java.time.LocalDate;
import java.util.Objects;

public class Manufacturing {

    private Long id;
    private String snapshotId;
    private String subIndustry;
    private int workerCount;
    private int year;
    private LocalDate referenceDate;

    protected Manufacturing() {}

    private Manufacturing(String snapshotId, String subIndustry, int workerCount,
                           int year, LocalDate referenceDate) {
        this.snapshotId = Objects.requireNonNull(snapshotId);
        this.subIndustry = Objects.requireNonNull(subIndustry);
        this.workerCount = workerCount;
        this.year = year;
        this.referenceDate = Objects.requireNonNull(referenceDate);
    }

    public static Manufacturing create(String snapshotId, String subIndustry, int workerCount,
                                        int year, LocalDate referenceDate) {
        return new Manufacturing(snapshotId, subIndustry, workerCount, year, referenceDate);
    }

    public static Manufacturing reconstitute(Long id, String snapshotId, String subIndustry,
                                              int workerCount, int year, LocalDate referenceDate) {
        Manufacturing m = new Manufacturing(snapshotId, subIndustry, workerCount, year, referenceDate);
        m.id = id;
        return m;
    }

    public Long id() { return id; }
    public String snapshotId() { return snapshotId; }
    public String subIndustry() { return subIndustry; }
    public int workerCount() { return workerCount; }
    public int year() { return year; }
    public LocalDate referenceDate() { return referenceDate; }
}
```

- [ ] **Step 5: Create VietnamE9 domain model**

```java
package com.hr.fwc.domain.publicdata;

import java.time.LocalDate;
import java.util.Objects;

public class VietnamE9 {

    private Long id;
    private String snapshotId;
    private String industry;
    private int totalCount;
    private int maleCount;
    private int femaleCount;
    private LocalDate referenceDate;

    protected VietnamE9() {}

    private VietnamE9(String snapshotId, String industry, int totalCount,
                       int maleCount, int femaleCount, LocalDate referenceDate) {
        this.snapshotId = Objects.requireNonNull(snapshotId);
        this.industry = Objects.requireNonNull(industry);
        this.totalCount = totalCount;
        this.maleCount = maleCount;
        this.femaleCount = femaleCount;
        this.referenceDate = Objects.requireNonNull(referenceDate);
    }

    public static VietnamE9 create(String snapshotId, String industry, int totalCount,
                                    int maleCount, int femaleCount, LocalDate referenceDate) {
        return new VietnamE9(snapshotId, industry, totalCount, maleCount, femaleCount, referenceDate);
    }

    public static VietnamE9 reconstitute(Long id, String snapshotId, String industry, int totalCount,
                                          int maleCount, int femaleCount, LocalDate referenceDate) {
        VietnamE9 v = new VietnamE9(snapshotId, industry, totalCount, maleCount, femaleCount, referenceDate);
        v.id = id;
        return v;
    }

    public Long id() { return id; }
    public String snapshotId() { return snapshotId; }
    public String industry() { return industry; }
    public int totalCount() { return totalCount; }
    public int maleCount() { return maleCount; }
    public int femaleCount() { return femaleCount; }
    public LocalDate referenceDate() { return referenceDate; }
}
```

- [ ] **Step 6: Create Quota domain model**

```java
package com.hr.fwc.domain.publicdata;

import java.util.Objects;

public class Quota {

    private Long id;
    private int year;
    private String industry;
    private int quotaCount;
    private String source;

    protected Quota() {}

    private Quota(int year, String industry, int quotaCount, String source) {
        this.industry = Objects.requireNonNull(industry);
        this.source = Objects.requireNonNull(source);
        this.year = year;
        this.quotaCount = quotaCount;
    }

    public static Quota create(int year, String industry, int quotaCount, String source) {
        return new Quota(year, industry, quotaCount, source);
    }

    public static Quota reconstitute(Long id, int year, String industry, int quotaCount, String source) {
        Quota q = new Quota(year, industry, quotaCount, source);
        q.id = id;
        return q;
    }

    public Long id() { return id; }
    public int year() { return year; }
    public String industry() { return industry; }
    public int quotaCount() { return quotaCount; }
    public String source() { return source; }
}
```

- [ ] **Step 7: Create repository interfaces**

```java
// RegionalIndustryRepository.java
package com.hr.fwc.domain.publicdata;
import java.util.List;
public interface RegionalIndustryRepository {
    List<RegionalIndustry> saveAll(List<RegionalIndustry> items);
    List<RegionalIndustry> findAll();
}

// ManufacturingRepository.java
package com.hr.fwc.domain.publicdata;
import java.util.List;
public interface ManufacturingRepository {
    List<Manufacturing> saveAll(List<Manufacturing> items);
    List<Manufacturing> findAll();
}

// VietnamE9Repository.java
package com.hr.fwc.domain.publicdata;
import java.util.List;
public interface VietnamE9Repository {
    List<VietnamE9> saveAll(List<VietnamE9> items);
    List<VietnamE9> findAll();
}

// QuotaRepository.java
package com.hr.fwc.domain.publicdata;
import java.util.List;
public interface QuotaRepository {
    List<Quota> findAll();
    List<Quota> findByYear(int year);
}
```

- [ ] **Step 8: Run test to verify it passes**

- [ ] **Step 9: Commit**

```bash
git add -A
git commit -m "feat: add public data domain models (RegionalIndustry, Manufacturing, VietnamE9, Quota)"
```

---

## Task 12: Public Data Infrastructure Layer

**Files:**
- Create: JPA entities (4), mapper, JPA repositories (4), repository impls (4)

- [ ] **Step 1: Create RegionalIndustryEntity**

```java
package com.hr.fwc.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "public_data_regional_industry")
public class RegionalIndustryEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "snapshot_id", nullable = false, length = 50) private String snapshotId;
    @Column(name = "region", nullable = false, length = 30) private String region;
    @Column(name = "industry", nullable = false, length = 30) private String industry;
    @Column(name = "quarter", nullable = false, length = 10) private String quarter;
    @Column(name = "worker_count", nullable = false) private int workerCount;
    @Column(name = "reference_date", nullable = false) private LocalDate referenceDate;
    // getter/setter for each field (standard JavaBean pattern)
}
```

- [ ] **Step 2: Create ManufacturingEntity, VietnamE9Entity, QuotaEntity**

Same pattern. Table names: `public_data_manufacturing`, `public_data_vietnam_e9`, `quota`.
QuotaEntity has no `snapshotId`/`referenceDate`. Fields map directly from domain models.

- [ ] **Step 3: Create PublicDataMapper**

```java
package com.hr.fwc.infrastructure.persistence.mapper;

import com.hr.fwc.domain.publicdata.*;
import com.hr.fwc.infrastructure.persistence.entity.*;

public final class PublicDataMapper {
    private PublicDataMapper() {}

    // RegionalIndustry
    public static RegionalIndustryEntity toEntity(RegionalIndustry d) {
        RegionalIndustryEntity e = new RegionalIndustryEntity();
        e.setId(d.id()); e.setSnapshotId(d.snapshotId()); e.setRegion(d.region());
        e.setIndustry(d.industry()); e.setQuarter(d.quarter());
        e.setWorkerCount(d.workerCount()); e.setReferenceDate(d.referenceDate());
        return e;
    }
    public static RegionalIndustry toDomain(RegionalIndustryEntity e) {
        return RegionalIndustry.reconstitute(e.getId(), e.getSnapshotId(), e.getRegion(),
            e.getIndustry(), e.getQuarter(), e.getWorkerCount(), e.getReferenceDate());
    }

    // Manufacturing — same pattern
    public static ManufacturingEntity toEntity(Manufacturing d) { /* ... */ }
    public static Manufacturing toDomain(ManufacturingEntity e) { /* ... */ }

    // VietnamE9 — same pattern
    public static VietnamE9Entity toEntity(VietnamE9 d) { /* ... */ }
    public static VietnamE9 toDomain(VietnamE9Entity e) { /* ... */ }

    // Quota — same pattern (no snapshotId)
    public static QuotaEntity toEntity(Quota d) { /* ... */ }
    public static Quota toDomain(QuotaEntity e) { /* ... */ }
}
```

> 각 `toEntity()`/`toDomain()`은 RegionalIndustry 패턴을 따라 전체 필드를 매핑합니다.

- [ ] **Step 4: Create JPA repositories and implementations**

```java
// RegionalIndustryJpaRepository.java
public interface RegionalIndustryJpaRepository extends JpaRepository<RegionalIndustryEntity, Long> {}

// RegionalIndustryRepositoryImpl.java
@Repository
public class RegionalIndustryRepositoryImpl implements RegionalIndustryRepository {
    private final RegionalIndustryJpaRepository jpaRepository;
    // constructor
    @Override
    public List<RegionalIndustry> saveAll(List<RegionalIndustry> items) {
        return jpaRepository.saveAll(items.stream().map(PublicDataMapper::toEntity).toList())
            .stream().map(PublicDataMapper::toDomain).toList();
    }
    @Override
    public List<RegionalIndustry> findAll() {
        return jpaRepository.findAll().stream().map(PublicDataMapper::toDomain).toList();
    }
}
```

> Manufacturing, VietnamE9, Quota도 동일 패턴. QuotaRepositoryImpl은 `findByYear(int)` 추가 — `QuotaJpaRepository`에 `List<QuotaEntity> findByYear(int year)` 선언.

- [ ] **Step 4: Verify schema creation**

```bash
./gradlew test --tests "*.PersistenceSchemaSmokeTest" 2>&1 | tail -5
```

Expected: PASS (H2 creates tables from entities)

- [ ] **Step 5: Commit**

```bash
git add -A
git commit -m "feat: add public data infrastructure layer (entities, mappers, repositories)"
```

---

## Task 13: CSV Loading Pipeline + Tests

**Files:**
- Modify: `build.gradle`
- Create: `src/main/java/com/hr/fwc/infrastructure/loader/CsvLoader.java`
- Create: `src/main/java/com/hr/fwc/infrastructure/loader/PublicDataInitializer.java`
- Create: `src/main/resources/data/regional_industry.csv`
- Create: `src/main/resources/data/manufacturing.csv`
- Create: `src/main/resources/data/vietnam_e9.csv`
- Create: `src/test/java/com/hr/fwc/infrastructure/loader/CsvLoaderTest.java`
- Create: `src/test/java/com/hr/fwc/infrastructure/loader/PublicDataInitializerIntegrationTest.java`

- [ ] **Step 1: Add jackson-dataformat-csv to build.gradle**

```gradle
implementation 'com.fasterxml.jackson.dataformat:jackson-dataformat-csv'
```

- [ ] **Step 2: Create sample CSV files in `src/main/resources/data/`**

Each file with header row + sample data rows matching the public data schema.

`regional_industry.csv`:
```csv
region,industry,quarter,worker_count,reference_date
서울특별시,제조업,2025Q1,12500,2025-03-31
경기도,제조업,2025Q1,45200,2025-03-31
경기도,건설업,2025Q1,8300,2025-03-31
충청남도,농축산업,2025Q1,3200,2025-03-31
부산광역시,어업,2025Q1,1800,2025-03-31
```

`manufacturing.csv`:
```csv
sub_industry,worker_count,year,reference_date
식료품제조업,8500,2024,2024-12-31
음료제조업,1200,2024,2024-12-31
섬유제품제조업,3400,2024,2024-12-31
금속가공제품제조업,6700,2024,2024-12-31
전자부품제조업,4500,2024,2024-12-31
```

`vietnam_e9.csv`:
```csv
industry,total_count,male_count,female_count,reference_date
제조업,45000,32000,13000,2024-12-31
농축산업,8500,6000,2500,2024-12-31
어업,3200,2800,400,2024-12-31
건설업,5600,5400,200,2024-12-31
서비스업,2100,1200,900,2024-12-31
```

- [ ] **Step 3: Write CsvLoaderTest**

Test CsvLoader with valid CSV, missing file, malformed rows.

- [ ] **Step 4: Run test to verify it fails**

- [ ] **Step 5: Create CsvLoader**

Generic CSV parsing utility using `jackson-dataformat-csv`. Takes a classpath resource path and a target class, returns `List<T>`. Handles missing file (warn + empty list) and bad rows (skip + warn).

```java
package com.hr.fwc.infrastructure.loader;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvLoader {

    private static final Logger log = LoggerFactory.getLogger(CsvLoader.class);

    public <T> List<T> load(String resourcePath, Class<T> type) {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        if (!resource.exists()) {
            log.warn("CSV 파일을 찾을 수 없습니다: {}. 해당 테이블 적재를 스킵합니다.", resourcePath);
            return List.of();
        }

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        List<T> results = new ArrayList<>();

        try (InputStream is = resource.getInputStream()) {
            var iterator = mapper.readerFor(type).with(schema).readValues(is);
            int lineNum = 1;
            while (iterator.hasNext()) {
                lineNum++;
                try {
                    results.add(iterator.next());
                } catch (Exception e) {
                    log.warn("CSV 파싱 실패 ({}:{} 행): {}", resourcePath, lineNum, e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("CSV 파일 읽기 실패: {}", resourcePath, e);
            return List.of();
        }

        log.info("CSV 적재 완료: {} → {}건", resourcePath, results.size());
        return results;
    }
}
```

- [ ] **Step 6: Create CSV row DTOs for jackson-dataformat-csv deserialization**

CSV는 도메인 모델로 직접 역직렬화하지 않고, 별도 CSV row DTO를 사용합니다. `CsvLoader`가 row DTO로 파싱 → `PublicDataInitializer`가 도메인 모델로 변환.

```java
// src/main/java/com/hr/fwc/infrastructure/loader/csv/RegionalIndustryCsvRow.java
package com.hr.fwc.infrastructure.loader.csv;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegionalIndustryCsvRow {
    @JsonProperty("region") public String region;
    @JsonProperty("industry") public String industry;
    @JsonProperty("quarter") public String quarter;
    @JsonProperty("worker_count") public int workerCount;
    @JsonProperty("reference_date") public String referenceDate;
}

// ManufacturingCsvRow.java — same pattern:
// sub_industry, worker_count, year, reference_date

// VietnamE9CsvRow.java — same pattern:
// industry, total_count, male_count, female_count, reference_date
```

- [ ] **Step 7: Create PublicDataInitializer**

```java
package com.hr.fwc.infrastructure.loader;

import com.hr.fwc.domain.publicdata.*;
import com.hr.fwc.infrastructure.loader.csv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class PublicDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PublicDataInitializer.class);

    private final CsvLoader csvLoader;
    private final RegionalIndustryRepository regionalIndustryRepository;
    private final ManufacturingRepository manufacturingRepository;
    private final VietnamE9Repository vietnamE9Repository;

    public PublicDataInitializer(CsvLoader csvLoader,
                                  RegionalIndustryRepository regionalIndustryRepository,
                                  ManufacturingRepository manufacturingRepository,
                                  VietnamE9Repository vietnamE9Repository) {
        this.csvLoader = csvLoader;
        this.regionalIndustryRepository = regionalIndustryRepository;
        this.manufacturingRepository = manufacturingRepository;
        this.vietnamE9Repository = vietnamE9Repository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        String snapshotId = UUID.randomUUID().toString();
        log.info("공공데이터 적재 시작 (snapshotId={})", snapshotId);

        long start = System.currentTimeMillis();
        loadRegionalIndustry(snapshotId);
        loadManufacturing(snapshotId);
        loadVietnamE9(snapshotId);
        log.info("공공데이터 적재 완료 ({}ms)", System.currentTimeMillis() - start);
    }

    private void loadRegionalIndustry(String snapshotId) {
        List<RegionalIndustryCsvRow> rows = csvLoader.load("data/regional_industry.csv", RegionalIndustryCsvRow.class);
        List<RegionalIndustry> domains = rows.stream()
            .map(r -> RegionalIndustry.create(snapshotId, r.region, r.industry,
                r.quarter, r.workerCount, LocalDate.parse(r.referenceDate)))
            .toList();
        regionalIndustryRepository.saveAll(domains);
        log.info("  지역×업종 현황: {}건 적재", domains.size());
    }

    private void loadManufacturing(String snapshotId) {
        List<ManufacturingCsvRow> rows = csvLoader.load("data/manufacturing.csv", ManufacturingCsvRow.class);
        List<Manufacturing> domains = rows.stream()
            .map(r -> Manufacturing.create(snapshotId, r.subIndustry,
                r.workerCount, r.year, LocalDate.parse(r.referenceDate)))
            .toList();
        manufacturingRepository.saveAll(domains);
        log.info("  제조업 중분류: {}건 적재", domains.size());
    }

    private void loadVietnamE9(String snapshotId) {
        List<VietnamE9CsvRow> rows = csvLoader.load("data/vietnam_e9.csv", VietnamE9CsvRow.class);
        List<VietnamE9> domains = rows.stream()
            .map(r -> VietnamE9.create(snapshotId, r.industry,
                r.totalCount, r.maleCount, r.femaleCount, LocalDate.parse(r.referenceDate)))
            .toList();
        vietnamE9Repository.saveAll(domains);
        log.info("  베트남 E-9: {}건 적재", domains.size());
    }
}
```

- [ ] **Step 7: Run CsvLoaderTest to verify passes**

- [ ] **Step 8: Write and run PublicDataInitializerIntegrationTest**

Verify that after app context loads, public data tables have records.

- [ ] **Step 9: Run full test suite**

```bash
./gradlew test 2>&1 | tail -10
```

Expected: ALL tests pass

- [ ] **Step 10: Commit**

```bash
git add -A
git commit -m "feat: add CSV loading pipeline for public data

CsvLoader (jackson-dataformat-csv), PublicDataInitializer (ApplicationRunner).
3 CSV files: regional_industry, manufacturing, vietnam_e9.
Auto-loads on startup with snapshotId versioning."
```

---

## Task 14: Quota Seed Data

**Files:**
- Modify: `src/main/resources/data.sql`

- [ ] **Step 1: Add quota INSERT statements to data.sql**

```sql
-- Quota seed data (HWP 수동추출 — 플레이스홀더 수치)
INSERT INTO quota (year, industry, quota_count, source) VALUES (2025, '제조업', 36000, '도입계획');
INSERT INTO quota (year, industry, quota_count, source) VALUES (2025, '농축산업', 8000, '도입계획');
INSERT INTO quota (year, industry, quota_count, source) VALUES (2025, '어업', 5000, '도입계획');
INSERT INTO quota (year, industry, quota_count, source) VALUES (2025, '건설업', 2000, '도입계획');
INSERT INTO quota (year, industry, quota_count, source) VALUES (2025, '서비스업', 1500, '도입계획');
INSERT INTO quota (year, industry, quota_count, source) VALUES (2024, '제조업', 34000, 'E-9현황');
INSERT INTO quota (year, industry, quota_count, source) VALUES (2024, '농축산업', 7500, 'E-9현황');
INSERT INTO quota (year, industry, quota_count, source) VALUES (2024, '어업', 4800, 'E-9현황');
INSERT INTO quota (year, industry, quota_count, source) VALUES (2024, '건설업', 1800, 'E-9현황');
INSERT INTO quota (year, industry, quota_count, source) VALUES (2024, '서비스업', 1200, 'E-9현황');
```

- [ ] **Step 2: Verify app starts with seed data**

```bash
./gradlew bootRun &
sleep 10
curl -s http://localhost:8080/api/companies | python3 -m json.tool | head -20
kill %1
```

Expected: JSON array with 8 companies

- [ ] **Step 3: Run full test suite**

```bash
./gradlew test 2>&1 | tail -10
```

Expected: ALL tests pass

- [ ] **Step 4: Commit**

```bash
git add src/main/resources/data.sql
git commit -m "feat: add quota seed data to data.sql

10 rows: 2024/2025 × 5 industries. Placeholder values from HWP extraction."
```

---

## Task 15: CLAUDE.md Final Sync + FE Changeset

**Files:**
- Modify: `CLAUDE.md`

- [ ] **Step 1: Update CLAUDE.md**

- Package structure: `workplace/` → `company/`, add `publicdata/`, add `infrastructure/loader/`
- API endpoints: add Company CRUD section
- Domain terms: add Region, IndustryCategory references
- Note: `workplaceId` → `companyId` breaking change in worker APIs

- [ ] **Step 2: Run full test suite one final time**

```bash
./gradlew test 2>&1 | tail -10
```

Expected: ALL tests pass

- [ ] **Step 3: Commit**

```bash
git add CLAUDE.md
git commit -m "docs: final CLAUDE.md sync for stage 1 implementation

Update package structure, API endpoints, domain terms.
Note FE breaking change: workplaceId → companyId."
```

---

## Summary

| Task | Description | Phase |
|------|-------------|-------|
| 1 | Region & IndustryCategory enums | 1 |
| 2 | Company domain model + tests | 1 |
| 3 | Company infrastructure layer | 1 |
| 4 | Rename workplaceId → companyId | 1 |
| 5 | Update data.sql + exception handlers | 1 |
| 6 | Update all tests + regression | 1 |
| 7 | CLAUDE.md package structure sync | 1 |
| 8 | Company CRUD DTOs | 2 |
| 9 | CompanyService + integration test | 2 |
| 10 | CompanyController + API + test | 2 |
| 11 | Public data domain models + tests | 3 |
| 12 | Public data infrastructure layer | 3 |
| 13 | CSV loading pipeline + tests | 4 |
| 14 | Quota seed data | 5 |
| 15 | CLAUDE.md final sync | 6 |
