<a name="top">

# Karrot-Bank-Management

</a>

당근페이의 뱅킹시스템을 이용한 계좌이체 API

## 목차

#### [1. 프로젝트 설명](#about_project)
#### [2. 계좌이체 API 서버 기능](#functions)
#### [3. 문제 해결 전략](#problem_solving_strategy)
#### [4. API 명세](#API_specifications)
#### [5. 로컬 환경 서버 구동 가이드](#server_driving_guide)


<br>

---

<a name="about_project">  

### 1. 프로젝트 설명
</a>

    당근페이 서버 개발 인턴 과제
    뱅킹시스템(외부시스템)을 이용한 계좌이체 REST API 서버 구현
- 기획
  - 다중 서버로 실행 할 수 있도록 설계 (단, 다중 서버의 거래 충돌 관리는 외부 뱅킹시스템으로 처리)
    - 계좌번호 생성, tx_id 생성을 랜덤하게 만들어 진행( 공통 전역변수 사용 중 충돌하지 않기 위해서)
    - REST API 구현
  - 서버에서 외부 뱅킹시스템 연결은 RestTemplate 사용
    - (요청 결과가 나오거나 timeout 될때까지 코드 블록됨)
- 기술 스택
    - Java / Spring Boot / h2
<br>

---
    
<a name="functions">  

### 2. 계좌이체 API 서버 기능

</a>

#### 계좌 등록
   - 사용자는 3개의 은행("D001", "D002", "D003")에 계좌를 등록할 수 있어요.
   - 원하는 은행에 요청을 보내면 계좌번호를 생성 및 등록해 반환한다.
   
#### 계좌 이체
   - 사용자는 등록된 계좌를 사용해 다른 사람 계좌번호로 계좌이체를 할 수 있어요.

#### 거래 내역 조회
   - 거래 내역 고유 값을 통해 계좌이체가 성공했는지 확인할수 있어요.

<br>

---

<a name="problem_solving_strategy">  

### 3. 문제 해결 전략

</a>

1. 서버 to 서버 통신 문제 
   ```markdown
   사용자(client)의 요청을 해결하기 위해 뱅킹시스템(외부API)의 결과가 필요합니다. 
   이때, Spring Framework가 제공하는 RestTemplate, WebClient 두개의 기능으로 해결 가능합니다. 
    
   이 프로젝트에서는 RestTemplate이 사용되었습니다. 그 이유는 * Blocking 방식을 선택했기 때문입니다. 
   먼저 Blocking 방식은 뱅킹시스템의 결과에 따라 service에서 반응이 분기 하기때문에 뱅킹시스템 요청을 기다릴 필요가 있었습니다. 
   또한, 뱅킹시스템이 어떠한 사유로 응답이 늦어지면 무한정 기다릴수 없어 RestTemplate의 Connection Pool 등록시 설정해 일정시간 이후 timeout 처리하도록 진행했습니다.
   ```
2. 가상 외부 API TEST 문제
   ```markdown
   외부 API가 가상으로 존재해 요청 상황마다 알맞은 결과를 도출하지 않아 문제가 발생했습니다.  
   그래서 저는 기능에 맞는 코드를 작성 후 단위 테스트를 통해 점검 했습니다.  
   
   1. Request  
   기능마다 테스트를 진행하고 해당 테스트에서는 원하는 곳으로 Request 보낸는 것은 Interceptor 통한 Logging으로 확인했습니다._ _
   2. Response  
   추가로 프로젝트 각 기능이 원하는 응답은 성공과 예외상황을 나누어 MockRestServiceServer Response 직접 작성해 로직이 제대로 동작하는지 테스트를 진행했습니다.
   ```
3. 기타 (뱅킹시스템 능력 설정) 문제
    ```markdown
   은행의 계좌는 여러 사람이 동시에 접근했을 경우 많은 고려사항이 있습니다.   
   이를 계좌이체 서버가 책임지는지 아니면 뱅킹시스템(외부API)가 책임이 있는지 설정할 필요가 있었습니다.   
   1. 많은 트레픽에서 생기는 계좌 관리는 뱅킹시스템에게 책임있다.   
   2. 계좌이체 서버는 정확한 요청 전달과 결과 응답 및 편의(계좌번호 생성, 이체내역 저장)를 제공한다.
   ```

<br>

---

<a name="API_specifications">  

### 4. API 명세

</a>

#### Swagger 이용
#### [링크 클릭 Swagger](http://localhost:8080/swagger-ui.html#/)
참고 : 로컬에서 서버가 구동 && 8080포트로 작동중 일때 확인가능

<br>

---

<a name="server_driving_guide">  

### 5. 로컬 환경 서버 구동 가이드

</a>

#### 1. IntelliJ 사용
   - 로컬 환경에 git repogistory를 clone 받아 IntelliJ IDE 환경으로 프로젝트를 연다.
   - java(openJDK corretto-11.0.13) 11버전으로 세팅한다.
   - BankApplication.java 실행한다.
   - 'http://localhost:8080/' 서버가 구동됨

<br>

#### 2. docker compose 사용
1. 애플리케이션 빌드<br>
   `$ ./gradlew build`
2. docker-compose.yml 파일 실행<br>
   `$ docker-compose up -d`
3. 테스트 및 API doc 확인 후
4. 종료 `$ docker-compose down`

### 추가!! 로컬 환경 서버 구동 테스트를 위한 임시 서버
(사용해도 되고 안해도 됩니다.)
#### 단, 사용시 application.yml의 my.rootUri값을 임시 뱅킹서버 주소로 일치시켜줘야한다.

- [github 주소](https://github.com/dawit95/BankingSystem)
- docker image : dawit95/demo-bank-system
---

[맨 위로 가기](#top)
 