# 결제 시스템 시뮬레이터 작업 항목 목록

## 커밋 관리
### 커밋 포인트
각 작업 항목은 다음과 같은 단위로 커밋합니다:

1. **기능 단위 커밋**
   - 각 API 엔드포인트 구현 완료 시
   - 각 서비스 로직 구현 완료 시
   - 각 테스트 케이스 구현 완료 시

2. **리팩토링 커밋**
   - 코드 구조 개선 시
   - 성능 최적화 시
   - 중복 코드 제거 시

3. **문서화 커밋**
   - API 문서 업데이트 시
   - README 수정 시
   - 주석 추가/수정 시

### 커밋 전 확인 사항
각 커밋 전에 다음 항목들을 확인합니다:

#### 1. 코드 품질
- [ ] TDD 원칙 준수 여부 (테스트 코드 작성 완료)
- [ ] 코드 스타일 가이드 준수
- [ ] 불필요한 주석 및 디버그 코드 제거
- [ ] 중복 코드 제거

#### 2. 테스트
- [ ] 단위 테스트 실행 및 통과
- [ ] 통합 테스트 실행 및 통과
- [ ] 테스트 커버리지 80% 이상 유지

#### 3. 문서화
- [ ] API 문서 업데이트
- [ ] README.md 업데이트
- [ ] 코드 인라인 주석 추가/수정

#### 4. 보안
- [ ] 민감 정보 제거 (API 키, 비밀번호 등)
- [ ] 보안 취약점 검사
- [ ] 입력값 검증 로직 확인

#### 5. 성능
- [ ] 데이터베이스 쿼리 최적화
- [ ] 불필요한 API 호출 제거
- [ ] 캐시 적용 여부 확인

#### 6. 에러 처리
- [ ] 예외 처리 로직 확인
- [ ] 에러 메시지 명확성 확인
- [ ] 로깅 적절성 확인

## 개요
결제 시스템 시뮬레이터의 개발 작업을 4주 타임라인으로 나누어 체계적으로 정리합니다. 각 작업은 부산은행 백엔드 개발자 지원 포트폴리오를 위한 **Java**, **Kotlin**, **Spring Boot**, **Oracle/H2** 기반 REST API 구현에 초점을 맞춥니다. 작업은 PRD의 기능 요구사항(인증, 결제 시작, 확인, 상태 조회)과 비기능 요구사항(성능, 보안, 테스트)을 반영하며, 부산은행의 기술 요구사항(JSP, Java, Oracle, CURL)과 데이터 분석 역량을 고려합니다.

## 1주차: 프로젝트 기본 구성

### 1.1 프로젝트 설정
- [ ] **Spring Boot + Kotlin** 프로젝트 생성.
- [ ] 의존성 추가: Spring Web, JPA, Security, JWT, H2, Oracle JDBC.
- [ ] `application.yml` 구성: 데이터베이스, 포트, JWT 설정.
- [ ] 로컬 개발용 **H2** 데이터베이스 설정.
- [ ] 패키지 구조 설계: `controller`, `service`, `repository`, `model`, `dto`, `config`.

### 1.2 데이터베이스 스키마 설계
- [ ] **User** 엔티티 설계: `id`, `username`, `password_hash`, `created_at`.
- [ ] **Account** 엔티티 설계: `account_id`, `user_id`, `balance`, `created_at`.
- [ ] **Transaction** 엔티티 설계: `transaction_id`, `account_id`, `amount`, `status`, `timestamp`.
- [ ] **JPA**로 H2 데이터베이스 스키마 생성 및 테스트.

### 1.3 보안 구성
- [ ] **Spring Security** 기본 설정.
- [ ] **JWT** 인증 구현:
  - [ ] `JwtTokenProvider` 클래스: 토큰 생성/검증.
  - [ ] `JwtAuthenticationFilter` 클래스: 요청 필터링.
  - [ ] `SecurityConfig` 클래스: 인증 경로 설정.
- [ ] 비밀번호 **bcrypt** 암호화 설정.

## 2주차: 사용자 인증 및 결제 초기화 API

### 2.1 사용자 관리 기능
- [ ] **User** 엔티티 및 **JPA** 매핑 구현.
- [ ] `UserRepository` 구현: 기본 CRUD 작업.
- [ ] `UserService` 구현: 회원가입, 로그인 로직.
- [ ] `AuthController` 구현:
  - [ ] `POST /auth/signup`: 회원가입 API.
  - [ ] `POST /auth/login`: JWT 토큰 발급 API.
- [ ] DTO 클래스:
  - [ ] `SignupRequestDto`: `username`, `password`.
  - [ ] `LoginRequestDto`: `username`, `password`.
  - [ ] `TokenResponseDto`: `token`, `expiresAt`.

### 2.2 결제 초기화 API
- [ ] `PaymentStatus` 열거형 정의: `PENDING`, `SUCCESS`, `FAILED`.
- [ ] **Transaction** 엔티티 및 **JPA** 매핑 구현.
- [ ] `TransactionRepository` 구현: 트랜잭션 CRUD.
- [ ] `PaymentService` 구현:
  - [ ] 결제 초기화 로직: 금액, 계좌 잔액 검증.
  - [ ] 모의 계좌 데이터 검증.
- [ ] `PaymentController` 구현:
  - [ ] `POST /payment/init`: 결제 요청 생성.
- [ ] DTO 클래스:
  - [ ] `PaymentInitRequestDto`: `accountId`, `amount`.
  - [ ] `PaymentResponseDto`: `transactionId`, `status`.

### 2.3 기본 예외 처리
- [ ] 글로벌 예외 핸들러: `@RestControllerAdvice`.
- [ ] 커스텀 예외 클래스:
  - [ ] `InvalidRequestException`: 유효하지 않은 입력.
  - [ ] `ResourceNotFoundException`: 존재하지 않는 리소스.
  - [ ] `AuthenticationException`: 인증 실패.

## 3주차: 결제 확인 및 상태 조회 API

### 3.1 결제 확인 API
- [ ] `PaymentService` 확장:
  - [ ] 결제 확인 로직: 잔액 검증, 상태 업데이트.
  - [ ] 성공/실패 시뮬레이션: 모의 잔액 체크.
  - [ ] **JPA** 트랜잭션 관리: 실패 시 롤백.
- [ ] `PaymentController` 확장:
  - [ ] `POST /payment/confirm/{transactionId}`: 결제 확인.
- [ ] DTO 클래스:
  - [ ] `PaymentConfirmRequestDto`: `transactionId`.
  - [ ] `PaymentConfirmResponseDto`: `status`, `message`.

### 3.2 결제 상태 조회 API
- [ ] `PaymentService` 확장:
  - [ ] 트랜잭션 조회 로직: ID 기반 데이터 조회.
- [ ] `PaymentController` 확장:
  - [ ] `GET /payment/status/{transactionId}`: 상태 조회.
- [ ] DTO 클래스:
  - [ ] `TransactionStatusResponseDto`: `transactionId`, `amount`, `status`, `timestamp`.

### 3.3 트랜잭션 로깅
- [ ] `TransactionLog` 엔티티: `logId`, `transactionId`, `action`, `timestamp`.
- [ ] `TransactionLogRepository` 구현.
- [ ] `LoggingService` 구현: 트랜잭션 이벤트 기록.
- [ ] **AOP**로 API 호출 자동 로깅.

### 3.4 단위 테스트
- [ ] `UserService` 단위 테스트: 회원가입, 로그인.
- [ ] `AuthController` 단위 테스트: API 응답 검증.
- [ ] `PaymentService` 단위 테스트: 결제 초기화, 확인, 조회.
- [ ] `PaymentController` 단위 테스트: API 상태 코드 확인.

## 4주차: 테스트, 배포, 문서화

### 4.1 통합 테스트
- [ ] 인증 API 통합 테스트: `/auth/login`, `/auth/signup`.
- [ ] 결제 API 통합 테스트: `/payment/init`, `/payment/confirm`, `/payment/status`.
- [ ] **Postman** 컬렉션 작성: 모든 엔드포인트 테스트.

### 4.2 성능 최적화
- [ ] **Oracle/H2** 데이터베이스 인덱스 추가: `transaction_id`, `account_id`.
- [ ] JPA 쿼리 최적화: 불필요한 조회 제거.
- [ ] 동시성 처리: **JPA** 격리 수준 설정.

### 4.3 보안 강화
- [ ] 입력값 검증: `@Valid` 및 커스텀 validator.
- [ ] **CORS** 설정: API 접근 제한.
- [ ] 보안 헤더: X-XSS-Protection, Content-Security-Policy.

### 4.4 API 문서화
- [ ] **Swagger** 설정: OpenAPI 3.0.
- [ ] API 엔드포인트 문서화: 요청/응답 예제 포함.
- [ ] 문서 접근 경로: `/swagger-ui`.

### 4.5 배포 설정
- [ ] **AWS EC2**에 Spring Boot 애플리케이션 배포.
- [ ] **Docker** 컨테이너화: 애플리케이션 패키징.
- [ ] **Oracle RDS** 연결 설정(포트폴리오용).

### 4.6 프로젝트 문서화
- [ ] `README.md` 작성:
  - 프로젝트 개요, 설치/실행 가이드.
  - API 사용 예제(Postman 스크립트).
- [ ] 코드 인라인 주석 추가.
- [ ] GitHub 리포지토리 정리.

### 4.7 최종 테스트 및 점검
- [ ] 성능 테스트: 200ms 응답, 10개 동시 요청.
- [ ] 보안 테스트: SQL 인젝션, 유효하지 않은 토큰.
- [ ] 코드 리팩토링: 클린 코드 준수.

## 추가 개선사항 (선택)

### A. 모니터링 시스템
- [ ] 결제 성공/실패율 로깅.
- [ ] API 응답 시간 모니터링: **AWS CloudWatch**.
- [ ] 에러 알림 설정.

### B. 추가 기능
- [ ] 결제 취소 API: `POST /payment/cancel/{transactionId}`.
- [ ] 계좌 잔액 관리 API: `GET /account/balance`.
- [ ] **CURL**로 모의 결제 게이트웨이 연동.

### C. Oracle 전환
- [ ] **Oracle** 데이터베이스 설정: RDS 구성.
- [ ] H2 → Oracle 데이터 마이그레이션 스크립트.
- [ ] Oracle 쿼리 성능 테스트.

## 작업 항목 간 의존성
1. **1주차**: 프로젝트 설정(1.1) → 데이터베이스 스키마(1.2) → 보안 구성(1.3).
2. **2주차**: 데이터베이스(1.2) → 사용자 관리(2.1) → 결제 초기화(2.2) → 예외 처리(2.3).
3. **3주차**: 결제 초기화(2.2) → 결제 확인(3.1) → 상태 조회(3.2) → 로깅(3.3) → 테스트(3.4).
4. **4주차**: 모든 API(2.1~3.2) → 통합 테스트(4.1) → 최적화(4.2) → 보안(4.3) → 문서화(4.4) → 배포(4.5).
