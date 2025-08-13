### 📦 패키지 구조

- 도메인별 패키지 구조로 설계했습니다.

```
board
├── global/                                   # 전역 설정 및 공통 모듈
│   ├── config/                               # Spring 설정 클래스들
│   ├── common/                               # 공통 컴포넌트
│   │   ├── dto/                              # 공통 응답 객체
│   │   │   ├── page/
│   │   │   └── ApiResponse.java
│   │   ├── exception/                        # 글로벌 예외 처리
│   │   │   ├── BadRequestException.java
│   │   │   ├── UnauthorizedException.java
│   │   ├── util/                             # 유틸리티 클래스
│   │   │   ├── CommonUtil.java
│   │   ├── entity/                           # 공통 엔티티 기반
│   │   │   ├── BaseEntity.java
│   │   └── component/                        # 범용 컴포넌트
│   ├── security/                             # 보안 관련 (JWT, 인증/인가)
│   ├── aop/                                  # AOP 관련
│   ├── annotation/                           # 커스텀 어노테이션
│   └── infrastructure/                       # 외부 서비스 연동
│       ├── slack/
│       │   ├── SlackService.java
│       │   ├── SlackChannel.java
│       │   └── dto/
│       │       └── SlackMessageRequest.java
│       └── email/
│           ├── EmailService.java
│           ├── EmailTemplate.java
│           └── dto/
│               └── EmailRequest.java
├── domain/                                  # 비즈니스 도메인
│   ├── article/                             # 사용자 도메인
│   │   ├── controller/
│   │   ├── service/
│   │   ├── entity/
│   │   ├── repository/
│   │   └── dto/
│   │       ├── request/
│   │       └── response/
│   ├── comment/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── entity/
│   │   ├── repository/
│   │   └── dto/
│   │       ├── request/
│   │       └── response/
└── Application.java
```

<br />

### 🔑 인증
- JWT 기반 인증: Authorization 헤더로 토큰 전달

```
Authorization: Bearer <access_token>
```

<br />

### 📋 응답 상태 코드

- 해당 프로젝트에서는 다음과 같은 HTTP 상태 코드를 정의합니다.

| 상태 코드 | 코드명 | 설명 | 사용 시나리오 | 예시 |
|-----------|--------|------|---------------|------|
| **200** | OK | 요청이 성공적으로 처리됨 | • 조회 성공<br>• 수정 성공<br>• 일반적인 성공 응답 | • `GET /articles/1`<br>• `PUT /comments/1`<br>• `DELETE /articles/1` |
| **400** | Bad Request | 클라이언트의 잘못된 요청 | • 필수 파라미터 누락 | • 필수 필드 누락 |
| **401** | Unauthorized | 인증 실패 | • 로그인 정보 오류<br>• 토큰 만료/무효<br>• 인증 정보 누락 | • 잘못된 아이디/비밀번호<br>• JWT 토큰 만료 |
| **403** | Forbidden | 인증된 사용자의 권한 부족 | • 리소스 소유권 없음<br>• 특정 기능 접근 불가 | • 다른 사용자 게시글 수정/삭제<br>• 계정 권한 부족 |
| **404** | Not Found | 요청한 리소스가 존재하지 않음 | • 존재하지 않는 리소스 조회<br>• 삭제된 리소스 접근 | • `GET /articles/99` (존재하지 않음)<br>• `DELETE /comments/1` (없는 댓글) |
| **409** | Conflict | 현재 리소스 상태와 요청이 충돌 | • 중복 데이터 생성<br>• 이미 처리된 상태 | • 중복 이메일 회원가입<br>• 이미 삭제된 댓글 수정 |
| **422** | Unprocessable Entity | 요청은 올바르나 비즈니스 정책 위반 | • 비즈니스 규칙 위반 | • 글자 수 제한 초과 |
| **500** | Internal Server Error | 예상치 못한 서버 내부 오류 | • 처리되지 않은 예외<br>• 시스템 오류 | • NullPointerException |

<br />

### 🧭 응답 상태 코드 선택 가이드

#### 401 vs 403 구분
- **401**: "누구인지 모르겠다" → 인증 필요
- **403**: "누구인지는 알지만 권한이 없다" → 권한 부족

#### 409 vs 422 구분
- **409**: 리소스의 현재 상태 때문에 처리 불가 (중복, 이미 처리됨)
- **422**: 비즈니스 규칙 때문에 처리 불가 (검증 실패, 정책 위반)

#### 404 vs 409 구분
- **404**: 리소스 자체가 존재하지 않음
- **409**: 리소스는 존재하지만 상태가 요청과 충돌

<br />

### 🗂️ 응답 구조

#### 성공 (2xx)
```json
{
  "status": 200,
  "success": true,
  "data": {
    // 응답 데이터
  }
}
```

#### 에러 (4xx, 5xx)
```json
{
  "status": 422,
  "success": false,
  "error": {
    "code": "LENGTH_EXCEEDED",
    "message": "글자 수 제한을 초과하였습니다. [최대 100자]"
  }
}
```
