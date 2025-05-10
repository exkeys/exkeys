# 백엔드 개발자 프로필
- 학교: 동아대학교
- 이름: 박건우

## 요약

1년 이상의 백엔드 개발 경험을 가진 개발자로, Java, Spring Boot, C#.Net, ASP.Net을 활용한 금융 시스템 개발에 강점을 보유. 부산은행의 안정적이고 효율적인 금융 서비스를 위해 RESTful API 설계, Oracle/MS-SQL 데이터베이스 최적화, AWS 기반 클라우드 인프라 구축 경험을 활용하고자 함. 정보처리기사 자격증 보유, 웹/앱 개발 및 데이터 분석 역량을 바탕으로 팀워크와 문제 해결에 기여할 수 있습니다다.

## 핵심 역량

- **프로그래밍 언어**: Java, C#, Python, Kotlin, PRO-C
- **프레임워크 및 도구**: Spring Boot, ASP.Net, JSP, Django, CURL, Docker, Jenkins, Git, Eclipse
- **데이터베이스**: Oracle, MS-SQL, MySQL, PostgreSQL
- **클라우드**: AWS (EC2, S3, RDS)
- **설계 및 아키텍처**: RESTful API, Microservices, TDD
- **추가 역량**:
  - 웹 및 앱 개발: JSP, ASP.Net 기반 웹 애플리케이션, 하이브리드 앱 개발
  - OA 활용: MS Excel (VBA 포함), MS Word, PowerPoint
  - 데이터 분석: SQL 기반 분석, Python (Pandas, NumPy)
  - 프리젠테이션: 프로젝트 발표, 고객 보고서 작성
- **자격증**: 정보처리기사 

## 프로젝트 경험

### 금융 거래 관리 시스템 

- **역할**: 백엔드 개발자
- **기술 스택**: Java, Spring Boot, Oracle, AWS EC2, CURL
- **설명**:
  - 실시간 거래 내역 관리용 RESTful API 개발 (CURL로 외부 API 연동)
  - Spring Security와 JWT 기반 인증 시스템 구현
  - Oracle 데이터베이스 트랜잭션 최적화로 데이터 무결성 보장
  - Jenkins를 사용한 CI/CD 파이프라인 구축 및 AWS EC2 배포
- **성과**: 거래 처리 속도 20% 개선, 시스템 가동률 99.9% 달성

### 은행 고객 분석 대시보드 

- **역할**: 백엔드 개발자
- **기술 스택**: C#, ASP.Net, MS-SQL, Python (Pandas)
- **설명**:
  - 고객 데이터 분석용 API 엔드포인트 설계 (ASP.Net Web API)
  - MS-SQL 데이터 웨어하우스 구축 및 쿼리 최적화
  - Python Pandas로 데이터 전처리 및 분석
  - 대시보드 프론트엔드와 연동하여 데이터 시각화 지원
- **성과**: 데이터 조회 시간 30% 단축, 사용자 피드백 기반 기능 개선

### 뱅킹 웹 애플리케이션 

- **역할**: 백엔드 개발자
- **기술 스택**: JSP, Java, Oracle, Eclipse
- **설명**:
  - 계좌 조회 및 이체 기능 제공 웹 애플리케이션 개발
  - JSP와 Java Servlet으로 서버사이드 렌더링 구현
  - Oracle 데이터베이스와 연동하여 계좌 데이터 관리
  - Eclipse 환경에서 디버깅 및 성능 최적화
- **성과**: 응답 속도 15% 개선, 사용자 만족도 90% 이상

# Payment Simulator

결제 시뮬레이터는 계좌 간 거래와 결제를 시뮬레이션하는 REST API 서비스입니다.

## 주요 기능

- 사용자 관리 (회원가입, 로그인)
- 계좌 관리 (생성, 조회)
- 거래 처리
- 결제 처리
- JWT 기반 인증

## 기술 스택

- Kotlin
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- H2 Database
- JWT
- Swagger/OpenAPI
- JUnit 5
- Mockito

## 시작하기

### 요구사항

- JDK 17 이상
- Gradle 7.x 이상

### 빌드 및 실행

```bash
# 프로젝트 빌드
./gradlew build

# 애플리케이션 실행
./gradlew bootRun
```

### API 문서

애플리케이션 실행 후 다음 URL에서 API 문서를 확인할 수 있습니다:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

### H2 콘솔

H2 데이터베이스 콘솔은 다음 URL에서 접근할 수 있습니다:
- http://localhost:8080/h2-console

## API 엔드포인트

### 인증
- POST /api/auth/signup - 회원가입
- POST /api/auth/login - 로그인

### 계좌
- POST /api/accounts - 계좌 생성
- GET /api/accounts - 계좌 목록 조회
- GET /api/accounts/{accountNumber} - 계좌 상세 조회

### 거래
- POST /api/transactions - 거래 생성
- GET /api/transactions/account/{accountNumber} - 계좌별 거래 내역 조회

### 결제
- POST /api/payments/init - 결제 초기화
- POST /api/payments/confirm - 결제 확인
- GET /api/payments/{transactionId} - 결제 상태 조회

## 테스트

```bash
# 단위 테스트 실행
./gradlew test

# 통합 테스트 실행
./gradlew integrationTest
```

## 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다.

