# Git Flow Workflow

이 프로젝트는 **Git Flow** 브랜치 전략을 사용합니다.

## 브랜치 구조

```
main (배포)
  ↑
develop (통합 개발)
  ↑
feature/* (기능 개발)
  ↑
hotfix/* (긴급 수정)
```

## 브랜치 설명

| 브랜치 | 목적 | 기준 브랜치 | 병합 대상 |
|--------|------|------------|----------|
| **main** | 프로덕션 배포 | - | - |
| **develop** | 다음 릴리즈 통합 개발 | main | main |
| **feature/*** | 새로운 기능 개발 | develop | develop |
| **hotfix/*** | 프로덕션 긴급 수정 | main | main, develop |
| **release/*** | 릴리즈 준비 | develop | main, develop |

---

## 작업 흐름

### 1. 새로운 기능 개발

```bash
# 1. develop 브랜치에서 최신 코드 가져오기
git checkout develop
git pull origin develop

# 2. feature 브랜치 생성
git checkout -b feature/add-mysql-support

# 3. 작업 수행
# ... 코딩 ...

# 4. 커밋
git add .
git commit -m "feat: Add MySQL database support

- Add MySQL driver dependency
- Update application.properties for MySQL
- Add docker-compose for local development"

# 5. 원격에 푸시
git push origin feature/add-mysql-support

# 6. Pull Request 생성 (GitHub에서)
# feature/add-mysql-support → develop
```

### 2. Pull Request 규칙

**PR 제목 형식:**
```
[TYPE] 간단한 설명

예시:
[FEATURE] MySQL 데이터베이스 지원 추가
[FIX] 국민연금 면제국 판단 오류 수정
[DOCS] README 업데이트
```

**PR 본문 템플릿:**
```markdown
## 변경사항
- 변경사항 1
- 변경사항 2

## 테스트
- [ ] 단위 테스트 통과
- [ ] 통합 테스트 통과
- [ ] 수동 테스트 완료

## 관련 이슈
Closes #123
```

**리뷰 규칙:**
- 최소 1명의 리뷰어 승인 필요
- CI 테스트 통과 필수
- 충돌(conflict) 해결 필수

### 3. develop → main 병합 (릴리즈)

```bash
# 1. release 브랜치 생성
git checkout develop
git pull origin develop
git checkout -b release/v0.2.0

# 2. 버전 번호 업데이트 등 릴리즈 준비
# ...

# 3. release 브랜치를 main과 develop에 병합
git checkout main
git merge release/v0.2.0
git tag -a v0.2.0 -m "Release version 0.2.0"
git push origin main --tags

git checkout develop
git merge release/v0.2.0
git push origin develop

# 4. release 브랜치 삭제
git branch -d release/v0.2.0
git push origin --delete release/v0.2.0
```

### 4. 긴급 수정 (Hotfix)

```bash
# 1. main에서 hotfix 브랜치 생성
git checkout main
git pull origin main
git checkout -b hotfix/fix-critical-bug

# 2. 긴급 수정 작업
# ...

# 3. main과 develop 모두에 병합
git checkout main
git merge hotfix/fix-critical-bug
git tag -a v0.1.1 -m "Hotfix version 0.1.1"
git push origin main --tags

git checkout develop
git merge hotfix/fix-critical-bug
git push origin develop

# 4. hotfix 브랜치 삭제
git branch -d hotfix/fix-critical-bug
git push origin --delete hotfix/fix-critical-bug
```

---

## 커밋 메시지 규칙

### 형식
```
[TYPE] 제목 (50자 이내)

본문 (선택사항, 72자 이내)

Refs: #이슈번호
```

### 타입

| 타입 | 설명 | 예시 |
|------|------|------|
| **feat** | 새로운 기능 | `[FEATURE] Add MySQL support` |
| **fix** | 버그 수정 | `[FIX] Correct SSA country detection` |
| **docs** | 문서 변경 | `[DOCS] Update README` |
| **style** | 코드 스타일 (공백, 세미콜론 등) | `[STYLE] Fix indentation` |
| **refactor** | 리팩토링 | `[REFACTOR] Extract service layer` |
| **test** | 테스트 추가/수정 | `[TEST] Add integration tests` |
| **chore** | 기타 변경 | `[CHORE] Update dependencies` |

### 예시

```bash
[FEATURE] Add E-8 seasonal worker visa support

- Add E-8 visa type to VisaType enum
- Add seasonal worker insurance policies
- Update documentation

Refs: #42
```

---

## 브랜치 네이밍 규칙

| 브랜치 타입 | 네이밍 | 예시 |
|------------|--------|------|
| feature | `feature/기능-설명` | `feature/add-mysql-support` |
| hotfix | `hotfix/문제-설명` | `hotfix/fix-encoding-issue` |
| release | `release/v버전` | `release/v0.2.0` |

---

## Git Flow 설정 (선택사항)

Git Flow 확장 도구 설치:

```bash
# macOS
brew install git-flow-avh

# Ubuntu/Debian
sudo apt-get install git-flow

# Windows (Git for Windows 포함)
```

초기화:
```bash
git flow init
```

기능 개발:
```bash
git flow feature start mysql-support
# ... 작업 ...
git flow feature finish mysql-support
```

---

## 체크리스트

### PR 생성 전
- [ ] 코드 컴파일/빌드 성공
- [ ] 테스트 통과
- [ ] 불필요한 파일 제거 (주석, 디버그 코드)
- [ ] 커밋 메시지 규칙 준수

### PR 리뷰 시
- [ ] 코드 로직 검토
- [ ] 테스트 커버리지 확인
- [ ] 문서 업데이트 확인
- [ ] 성능 영향 검토

### 릴리즈 전
- [ ] CHANGELOG.md 업데이트
- [ ] 버전 번호 업데이트
- [ ] 모든 테스트 통과
- [ ] 문서 최신화

---

## 참고자료

- [A successful Git branching model](https://nvie.com/posts/a-successful-git-branching-model/)
- [Git Flow Cheat Sheet](https://danielkummer.github.io/git-flow-cheatsheet/)
