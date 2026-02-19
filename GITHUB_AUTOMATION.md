# GitHub 자동화 설정 가이드

## 설정된 자동화 파이프라인

```
git push feature/xxx
    ↓
[GitHub Actions]
    ↓
┌─────────────┐   ┌─────────────┐   ┌─────────────┐
│  1. CI      │ → │ 2. Auto PR  │ → │ 3. Review   │
│   Build     │   │  + AI       │   │  + Merge    │
│   Test      │   │   Review    │   │             │
└─────────────┘   └─────────────┘   └─────────────┘
```

## 1. CI 파이프라인 (`ci.yml`)

**트리거:** push, pull_request (main, develop)

**동작:**
- Gradle 빌드
- 테스트 실행
- 코드 품질 체크
- 테스트 리포트 생성

**결과 확인:**
- PR에 체크 표시 ✓
- Actions 탭에서 상세 로그

---

## 2. 자동 PR 생성 + AI 리뷰 (`auto-pr-review.yml`)

**트리거:** feature/* 브랜치 push

**동작:**
1. PR 자동 생성 (feature → develop)
2. 라벨 자동 부여 (feature/fix/docs 등)
3. AI 코드 리뷰 (CodeRabbit)

**필요한 설정:**
```bash
# GitHub Secrets에 추가해야 함:
Settings → Secrets and variables → Actions → New repository secret

Name: OPENAI_API_KEY
Value: {OpenAI API Key}
```

**AI 리뷰 예시:**
- 코드 스타일 제안
- 잠재적 버그 감지
- 성능 최적화 제안
- 보안 이슈 경고

---

## 3. 자동 머지 (`auto-merge.yml`)

**조건:**
- [ ] CI 통과 (빌드 + 테스트)
- [ ] 최소 1명의 승인 (리뷰)
- [ ] 라벨: `auto-merge` 붙어있음
- [ ] 라벨: `wip`, `do-not-merge` 없음
- [ ] 충돌 없음

**동작:**
- 조건 충족 시 자동으로 `squash merge`
- 머지 후 브랜치 자동 삭제 (설정 시)

---

## 사용 방법

### 일반적인 개발 플로우

```bash
# 1. feature 브랜치 생성
git checkout -b feature/add-payment

# 2. 작업 후 푸시
git add .
git commit -m "feat: Add payment module"
git push origin feature/add-payment
```

**그 이후는 자동:**
1. ✅ PR 자동 생성됨
2. ✅ AI 코드 리뷰 시작
3. ✅ CI 빌드/테스트 실행
4. ✅ (조건 충족 시) 자동 머지

### 라벨로 제어

| 라벨 | 효과 |
|------|------|
| `auto-merge` | ✅ 머지 가능 |
| `wip` | ❌ 머지 불가 |
| `do-not-merge` | ❌ 머지 불가 |

---

## GitHub 설정

### 1. Branch Protection Rules

**Settings → Branches → Add rule**

**develop 브랜치:**
- ✅ Require a pull request before merging
  - ✅ Require approvals: 1
- ✅ Require status checks to pass
  - CI Pipeline
- ✅ Require conversation resolution before merging

**main 브랜치:**
- ✅ Require a pull request before merging
  - ✅ Require approvals: 2
- ✅ Require status checks to pass
- ✅ Include administrators

### 2. Secrets 설정

**Settings → Secrets and variables → Actions**

| Secret | 설명 | 필요 여부 |
|--------|------|----------|
| `OPENAI_API_KEY` | AI 리뷰용 | 선택 |
| `GITHUB_TOKEN` | 자동 생성됨 | 자동 |

---

## 커스터마이징

### AI 리뷰 강도 조절

`.github/workflows/auto-pr-review.yml` 수정:

```yaml
with:
  review_simple_changes: true  # 단순 변경도 리뷰
  review_comment_lgtm: true    # LGTM 코멘트
  openai_light_model: gpt-4o-mini
  openai_heavy_model: gpt-4o
```

### 자동 머지 조건 변경

`.github/workflows/auto-merge.yml` 수정:

```yaml
env:
  MERGE_REQUIRED_APPROVALS: "2"  # 승인 2명 필요
  MERGE_LABELS: "ready,!draft"   # 커스텀 라벨
  MERGE_METHOD: "merge"          # merge 방식
```

---

## 모니터링

### Actions 상태 확인

1. **GitHub → Actions 탭**
2. 각 workflow 실행 기록 확인
3. 실패 시 로그 확인

### 알림 설정

**Settings → Notifications:**
- Actions → "Send notifications for failed workflows only"
- Pull Requests → "Participating, @mentions and custom"

---

## 문제 해결

### CI 실패

```bash
# 로컬에서 재현
./gradlew clean build
./gradlew test
```

### AI 리뷰 안 됨

- `OPENAI_API_KEY` 설정 확인
- Actions 로그에서 "AI Code Review" 단계 확인

### 자동 머지 안 됨

- `auto-merge` 라벨 붙었는지 확인
- Required approvals 충족 확인
- CI 통과 확인

---

## 참고

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [CodeRabbit AI Reviewer](https://coderabbit.ai/)
- [Auto-merge Action](https://github.com/pascalgn/automerge-action)
