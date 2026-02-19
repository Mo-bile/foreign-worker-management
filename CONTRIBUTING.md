# Contributing Guidelines

외국인 근로자 컴플라이언스 관리 시스템에 기여해 주셔서 감사합니다!

## 시작하기

### 개발 환경 설정

```bash
# 1. 저장소 포크 (Fork)
# GitHub에서 Fork 버튼 클릭

# 2. 로컬 클론
git clone https://github.com/YOUR_USERNAME/foreign-worker-management.git
cd foreign-worker-management

# 3. upstream 설정
git remote add upstream https://github.com/Mo-bile/foreign-worker-management.git

# 4. develop 브랜치 확인
git checkout develop
git pull upstream develop
```

### 빌드 및 테스트

```bash
# 빌드
./gradlew build

# 테스트
./gradlew test

# 애플리케이션 실행
./gradlew bootRun
```

---

## 기여 프로세스

### 1. 이슈 확인 또는 생성

- 기존 이슈 확인: [Issues](https://github.com/Mo-bile/foreign-worker-management/issues)
- 새로운 기능/버그: 이슈 생성
- 이슈에 자신을 assign

### 2. 브랜치 생성

```bash
# develop에서 최신 코드 가져오기
git checkout develop
git pull upstream develop

# feature 브랜치 생성
git checkout -b feature/이슈번호-간단설명

# 예시
git checkout -b feature/15-mysql-support
```

### 3. 개발 및 커밋

```bash
# 작업 후 변경사항 확인
git status

# 커밋
./gradlew test  # 테스트 먼저!
git add .
git commit -m "[TYPE] 커밋 메시지

상세 설명

Refs: #이슈번호"
```

### 4. 푸시 및 PR 생성

```bash
# 원격에 푸시
git push origin feature/15-mysql-support

# GitHub에서 Pull Request 생성
# base: develop ← compare: feature/15-mysql-support
```

---

## 코드 스타일

### Java

- **Google Java Style Guide** 준수
- 인덴트: 4 spaces (탭 금지)
- 줄 길이: 120자 이내
- 클래스/메서드: JavaDoc 주석 필수

### 객체지향 생활체조 9원칙

1. 한 메서드에 오직 한 단계의 들여쓰기만 한다
2. else 예약어를 쓰지 않는다
3. 모든 원시값과 문자열을 포장한다
4. 한 줄에 점을 하나만 찍는다
5. 줄여쓰지 않는다 (축약 금지)
6. 모든 엔티티를 작게 유지한다 (클스 50줄, 메서드 10줄)
7. 2개 이상의 인스턴스 변수를 가진 클래스를 쓰지 않는다
8. 일급 콜렉션을 쓴다
9. getter/setter/프로퍼티를 쓰지 않는다

### 테스트

- 테스트 클래스: `*Test.java`
- 통합 테스트: `*IntegrationTest.java`
- BDD 스타일: given-when-then

```java
@Test
@DisplayName("베트남 근로자는 국민연금 의무가입 대상")
void vietnamWorkerShouldBeMandatoryForNationalPension() {
    // given
    ForeignWorker worker = createWorker(Nationality.VIETNAM, VisaType.E9);
    
    // when
    InsuranceEligibility result = policy.determineEligibility(worker);
    
    // then
    assertThat(result.status()).isEqualTo(EligibilityStatus.MANDATORY);
}
```

---

## 이슈 라벨

| 라벨 | 설명 | 색상 |
|------|------|------|
| `bug` | 버그 수정 | 🔴 빨강 |
| `feature` | 새로운 기능 | 🟢 초록 |
| `documentation` | 문서 작업 | 🔵 파랑 |
| `refactor` | 리팩토링 | 🟡 노랑 |
| `test` | 테스트 관련 | 🟣 복숭아 |
| `legal` | 법률/정책 변경 | 🟠 주황 |
| `priority:high` | 높은 우선순위 | ⭕ 빨강 |
| `priority:low` | 낮은 우선순위 | ⚪ 회색 |

---

## 질문하기

- **일반 질문**: Discussions 탭 사용
- **버그 리포트**: Issues 탭에서 `bug` 라벨
- **기능 제안**: Issues 탭에서 `feature` 라벨

---

## 라이선스

기여하는 코드는 [MIT License](LICENSE) 하에 배포됩니다.

---

## 감사의 글

기여해 주신 모든 분들께 감사드립니다! 🙏
