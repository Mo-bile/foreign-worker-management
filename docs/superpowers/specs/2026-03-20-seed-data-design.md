# Seed Data Design

## Overview

H2 인메모리 DB에 개발/테스트용 시드 데이터를 채워넣는다.
두 파트로 구성: data.sql(Workplace 기초 데이터) + .http 파일(Worker API 호출).

## Goals

- `bootRun` 후 API를 즉시 테스트할 수 있는 환경 제공
- 모든 도메인 케이스(국적×비자 조합, 데드라인 상태)를 커버하는 50+ Worker 데이터
- 도메인 로직(보험 자격 판정, 데드라인 생성)이 자동 적용되는 방식 사용

## Architecture

### Part 1: data.sql (Workplace)

- 위치: `src/main/resources/data.sql`
- Spring Boot가 JPA 스키마 생성 후 자동 실행
- `application.yml`에 `spring.jpa.defer-datasource-initialization: true` 추가 필요
- 5~10개 사업장 INSERT (제조업, 농업, 건설업, 서비스업 등)

### Part 2: .http 파일 (Worker)

- 위치: `http/seed-workers.http`
- IntelliJ HTTP Client 형식
- `POST http://localhost:8080/api/workers` 요청 50+건
- 앱 실행 후 수동 실행 → 보험 자격 판정 + 데드라인 자동 생성

## Data Design

### Workplace (data.sql)

| ID | 사업장명 | 사업자번호 | 업종 |
|----|----------|-----------|------|
| 1 | 한국제조(주) | 123-45-67890 | 제조업 |
| 2 | 그린농장 | 234-56-78901 | 농업 |
| 3 | 대한건설(주) | 345-67-89012 | 건설업 |
| 4 | 서울식품(주) | 456-78-90123 | 식품제조 |
| 5 | 글로벌IT(주) | 567-89-01234 | IT서비스 |
| 6 | 해양수산(주) | 678-90-12345 | 수산업 |
| 7 | 스마트물류(주) | 789-01-23456 | 물류 |
| 8 | 코리아호텔 | 890-12-34567 | 숙박업 |

### Worker (.http) - 케이스별 분류

#### 보험 자격 판정 커버리지

| # | 케이스 | 국적 | SSA | 비자 | 국민연금 | 건강보험 | 고용보험 | 산재보험 |
|---|--------|------|-----|------|----------|----------|----------|----------|
| 1 | SSA+E9 | VIETNAM | Y | E9 | EXEMPT | MANDATORY | MANDATORY | MANDATORY |
| 2 | 비SSA+E9 | INDONESIA | N | E9 | MANDATORY | MANDATORY | MANDATORY | MANDATORY |
| 3 | SSA+E7 | USA | Y | E7 | EXEMPT | MANDATORY | OPTIONAL | MANDATORY |
| 4 | 비SSA+E7 | THAILAND | N | E7 | MANDATORY | MANDATORY | OPTIONAL | MANDATORY |
| 5 | SSA+E7_4 | JAPAN | Y | E7_4 | EXEMPT | MANDATORY | OPTIONAL | MANDATORY |
| 6 | 비SSA+H2 | CAMBODIA | N | H2 | MANDATORY | MANDATORY | OPTIONAL | MANDATORY |
| 7 | SSA+F2 | CHINA | Y | F2 | EXEMPT | MANDATORY | MANDATORY | MANDATORY |
| 8 | 비SSA+F2 | NEPAL | N | F2 | MANDATORY | MANDATORY | MANDATORY | MANDATORY |
| 9 | SSA+F5 | GERMANY | Y | F5 | EXEMPT | MANDATORY | MANDATORY | MANDATORY |
| 10 | SSA+F6 | FRANCE | Y | F6 | EXEMPT | MANDATORY | MANDATORY | MANDATORY |

나머지 40+건은 위 10개 핵심 케이스를 기반으로 국적/비자를 다양하게 조합하고, 비자 만료일을 분산 배치.

#### 데드라인 상태 커버리지

비자 만료일을 기준일(2026-03-20) 대비 다양하게 설정:

| 상태 | 비자 만료일 범위 | 건수 |
|------|-----------------|------|
| OVERDUE | 과거 (2026-03-01 이전) | 10건 |
| URGENT | D-7 이내 (2026-03-21~27) | 8건 |
| APPROACHING | D-30~D-7 (2026-03-28~04-19) | 12건 |
| PENDING | D-90+ (2026-06-20 이후) | 20+건 |

## Configuration Changes

### application.yml

```yaml
spring:
  jpa:
    defer-datasource-initialization: true  # 추가
```

## File Changes Summary

| 변경 유형 | 파일 | 설명 |
|-----------|------|------|
| 신규 | `src/main/resources/data.sql` | Workplace INSERT 8건 |
| 신규 | `http/seed-workers.http` | Worker 등록 API 호출 50+건 |
| 수정 | `src/main/resources/application.yml` | defer-datasource-initialization 추가 |

## Constraints

- 코드 변경 없음 (신규 엔드포인트 불필요)
- H2 create-drop이므로 앱 재시작 시 Workplace는 자동 복원, Worker는 .http 재실행 필요
- .http 파일은 IntelliJ HTTP Client 형식 (VS Code REST Client와도 호환)
