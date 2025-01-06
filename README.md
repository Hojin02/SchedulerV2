# Spring - JPA 프로젝트 SchedulerV2

## 🗒︎ Index
- 🎞️ 구조
  - API 명세
  -  [ERD](#erd)
  -  ✈️ [기능](#feature)
  -  - [유저](#유저-기능)
     - [일정](#일정-기능)
     - [댓글](#댓글-기능)
-  ⚠️ [트러블 슈팅](#troubleshooting)
-  😼 [후기](#review)
-  🛠︎ [수정사항](#refactor)
-  [수정후기](#refactorreview)

## SchedulerV2 API 명세
![image](https://github.com/user-attachments/assets/3c74748e-4b0f-4207-bd75-27c8439cfd57)

## Class Diagram
![image](https://github.com/user-attachments/assets/1b0bf63c-57d8-46e2-aa19-67aa58ad3c7a)

## ERD

```mermaid
erDiagram
    USER {
        bigint id PK "Primary Key"
        datetime created_at
        datetime updated_at
        varchar(255) email "Unique Key"
        varchar(255) password
        varchar(255) user_name
    }
    SCHEDULE {
        bigint id PK "Primary Key"
        datetime created_at
        datetime updated_at
        varchar(255) title
        longtext contents
        bigint user_id FK "Foreign Key"
    }
    COMMENT {
        bigint id PK "Primary Key"
        datetime created_at
        datetime updated_at
        varchar(255) contents
        bigint schedule_id FK "Foreign Key"
        bigint user_id FK "Foreign Key"
    }
    
    USER ||--o| SCHEDULE : "1:N"
    USER ||--o| COMMENT : "1:N"
    SCHEDULE ||--o| COMMENT : "1:N"
```
<a id="feature"></a>
# ✈️ 기능
### 1. 화이트 리스트
> 일정,댓글 조회는 로그인 없이 이용가능 함.(GET방식일 경우는 WhiteList)  
> 로그인,회원가입 기능 역시 로그인 없이 이용 가능함.  
> 나머지는 로그인 후 세션에 로그인이 저장되어야 이용 가능함.   

<details>
  <summary>코드 보기</summary>
  
``` java

public class LoginFilter implements Filter {
    private static final String[] WHITE_LIST = {"/", "/users/register","/users/login","/schedules*","/comments*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();


        if (!isWhiteList(requestURI, method)) {
            HttpSession session = httpRequest.getSession(false);

            if (session == null || session.getAttribute("userEmail") == null) {
                httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"error\": \"Log in is required.\"}");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isWhiteList(String requestURI, String method) {
        Boolean isScheduleNonGetRequest = requestURI.startsWith("/schedules") && !"GET".equals(method);
        Boolean isCommentNonGetRequest = requestURI.startsWith("/comments") && !"GET".equals(method);

        if (isScheduleNonGetRequest || isCommentNonGetRequest) {
            return false;
        }
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}

```
</details>

> 로그인 없이 화이트리스트에 없는 기능을 하려고하면 401에러처리 된다.
![image](https://github.com/user-attachments/assets/4aef82df-7828-411d-9f99-29114aa2deb8)

## 유저 기능

<details>
  <summary>펼쳐보기</summary>
  
### 2. 유저 회원가입
> 일정 추가, 수정, 삭제 기능은 회원 전용 서비스 이기 때문에 회원가입이 필요함.  
 ---
![image](https://github.com/user-attachments/assets/50aa5970-22c2-4d9f-865f-3bc09167abf9)

> 이때 비밀번호는 암호화 되어 데이터베이스에 저장된다.  
![image](https://github.com/user-attachments/assets/da8464c4-f394-4858-a5d2-2ccec8e1dcee)

### 3. 유저 로그인
> 화이트리스트에 없는 기능을 이용하려면 로그인이 필수다.  
![image](https://github.com/user-attachments/assets/07723894-3913-4fcf-bc7a-c3f4c66eccb0)

### 4. 유저 정보 수정
> 로그인 필수. 파마리터에 유저고유식별자 id가 들어감. 
![image](https://github.com/user-attachments/assets/6afc5e46-0135-4513-a190-8f5a3e268cd6)
> 추후 파라미터에 id가 아닌 세션에서 가져오고, 이름과 비밀번호 수정으로 변경해야 함.  
  
### 5. 유저 삭제
> 해당 유저가 삭제되면, 해당 유저가 작성한 일정,댓글이 모두 삭제되어야함.
```java
Class User
  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
```

![image](https://github.com/user-attachments/assets/6ce38968-180e-4068-9797-970f9345291e)
> 삭제 기능 또한, 파라미터로 유저id를 받는것이 아닌 세션에서 유저객체를 가져와야 하고, 비밀번호 검증을 한번더 하면 좋을 듯 하다.

### 6. 유저 로그아웃 
> 로그인 된 상태에서 유저가 로그아웃을 하면, 세션에 저장된 유저 정보가 무효화됨.  
![image](https://github.com/user-attachments/assets/39717c81-e902-4b0c-8b0a-82e6776a9c20)  

</details>

## 일정 기능

<details>
  <summary>펼쳐보기</summary>
  
### 7. 일정 추가
> 로그인 필수, 사용자가 입력한 일정 정보와 세션에 있는 유저정보가 일정테이블에 저장된다.  
![image](https://github.com/user-attachments/assets/ae7b69b3-2571-4fc4-81c4-0abb3f1a4819)

### 8. 일정 수정
> 로그인 필수, 일정의 아이디값을 PathVariable로 넣고 요청body에 수정할 제목과 내용을 넣어준다.  
![image](https://github.com/user-attachments/assets/fa9e2782-3e6e-4918-a6d4-ea62a2068439)

### 9. 일정 삭제
> 로그인 필수, 일정의 아이디로 일정의 정보를 삭제함.
![image](https://github.com/user-attachments/assets/cfaf31e4-419b-44c8-885c-0a00eac3f442)

### 10. 일정 전체조회(필터링 및 페이징)
> 전체 이용가능 기능, 일정 제목,수정일로 필터링 하여 조회 할 수있으며, 페이지넘버와 사이즈로 페이징가능.  
![image](https://github.com/user-attachments/assets/74f0d01e-7c08-49e0-926b-f8166bece9a3)
>> 100개의 데이터중 1페이지,사이즈(디폴트 10) 아이디가11~20인 글들을 조회

### 11. 일정 단건 조회
> 전체 이용가능 기능, 일정id로 해당 일정의 데이터를 불러옴.
![image](https://github.com/user-attachments/assets/93f987c0-457c-4df6-9bc9-e0eb7b7df252)

</details>

## 댓글 기능

<details>
  <summary>펼쳐보기</summary>
  
### 12. 댓글 추가
> 로그인 필수, 요청 body에 일정id와 댓글 내용을 보내 댓글을 추가함.  
![image](https://github.com/user-attachments/assets/2c2cf8d6-86cf-4a57-aadf-c6d505f338ba)

### 13. 댓글 전체 조회
> 전체 이용가능 기능. 요청값 없이 조회 가능
![image](https://github.com/user-attachments/assets/adc328a4-dffb-4307-afa8-565254f9a941)

### 14. 댓글 단건 조회
> 전체 이용가능 기능, PathVarialbe로 댓글 id를 넘겨 해당 id의 댓글을 조회  
![image](https://github.com/user-attachments/assets/00a74cff-491b-4014-918a-5aa94925e60d)

### 17. 댓글 수정
> 로그인 필수, PathVarialbe로 댓글 id를 넘겨 해당 id의 댓글을 수정
![image](https://github.com/user-attachments/assets/8093ba56-1663-41da-90c3-510731c95336)

### 16. 댓글 삭제
> 로그인 필수, PathVarialbe로 댓글 id를 넘겨 해당 id의 댓글을 삭제  
![image](https://github.com/user-attachments/assets/1d3f8576-6360-44e4-94f8-098be5ab8d8f)

</details>

<a id="troubleshooting"></a>
# ⚠️ 트러블 슈팅
### 발생한 문제
> 유저삭제 시 유저가 작성한 일정이나, 댓글이 있으면 에러가 발생했다.  
>  o.h.engine.jdbc.spi.SqlExceptionHelper   : Cannot delete or update a parent row: a foreign key constraint fails (schedulerv2.comment, CONSTRAINT .....  

### 발생 원인
> 일정테이블과 댓글테이블에서 유저를 외래키로 받고있기 때문에 유저가 삭제되면 문제가 발생한다.  

### 해결방법
> 유저삭제 시 유저아이디를 외래키로 가지고있는 데이터도 함께 삭제 해야함.

```java
Class User
  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
```

<a id="review"></a>
# 😼 후기
일단 JPA를 하면서 신세계를 경험했다..
repository에서 직접 메소드를 구현안해도 되고 무엇보다 sql을 직접 안써도 되는게 어메이징 했다..

또 필요한 부분에 대하여 직접 퀴리를 쓸 수도 있지만. 메소드명의 명명규칙에 따라 JPA가 쿼리를 자동으로 생성해서 날려준다는게 너무신기해따

> JPA는 메서드 명명규첵에 따라 쿼리를 자동생성함. 
[접두어] + By + [조건1] + [연산자] + [조건2] + ...
접두어: find, read, query, get, count, delete 등
By: 필수 키워드로 조건 시작을 알림
조건: 엔티티의 필드명을 사용 (카멜 케이스)
연산자: 필드에 적용되는 조건 (예: GreaterThan, Like 등)


그리고 이번에는 기능별로 DTO를 나눴다. 저번에는 엔티티별로 나눴지만 이번에는 기능별로 필요한값만 요청 받고 응답 할 수 있도록 DTO를 분리하였다.

또 테이블을 직접 생성하는것이 아니라 JPA로 Entity클래스에 설정해줘서 편하게 테이블을 만들고
연관관계도 설정할 수 있어서 좋았다!

그리고 Update쿼리에서도 Update메소드를 구현하거나 사용하는게 아닌
트렌젝션 안에서 Entity객체의 데이터가 변경되면 update를 호출할 필요없이
jpa데이터 수정됨을 감지 후 자동으로 쿼리를 날려주는것도 신기하고 편리했다.

``` java
@Override
    @Transactional
    public ScheduleResponseDto modifyScheduleById(Long id, ScheduleRequestDto dto) {
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(id);
        schedule.UpdateTitleAndContents(dto);
        em.flush();//변경사항 즉시 반영
        return ScheduleResponseDto.toDto(schedule);
    }
```

#### 아직 구현 못한것과 리펙토링이 필요한부분
먼저 예외처리가 제대로 되어있지않다. 예를 들어 전체조회 시 빈 배열일 경우 NOT_FOUND로 한다던가.. 등등
또 게시글 수정 및 삭제 시 해당 게시글의 댓글이 같이 삭제되도록 구현해야한다.
(트러블 슈팅쓰면서 발견하게됨..)

어설픈 기능도 많다 유저 수정이나 삭제 시 세션에 있는 값을 이용해서 구현해야하는데
유저고유식별자를 입력받아 수정이나 삭제 되도록했다.
세션에 유저를 가져와 비밀번호 검증을 한번더 해서 수정을 하던지 삭제를 하던지~ 해야한다.

JPA를 학습하면서 새로운 기술(?)과 신세계를 많이 경험했다.
리펙토링을 거쳐서 좀더 완벽한 프로젝트가 되도록 해야게따


<a id="refactor"></a>
# 🛠︎ 수정사항

###  Enum형태로 error코드를 관리
- 이전 코드에서는 new ResponseStatusException를 발생시켜 모든 에러를 따로 관리했지만.
- 수정 후 에러 코드로 공통된 예러를 처리하고, 상황별 에러를 명확히 분리했다.
- 또, 전역 예외 처리로 CustomException발생시 컨트롤러에서 에러를 잡아 상요자 친화적인 응답을 제공한다.

```java
@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "ACCOUNT-001", "사용자를 찾을 수 없습니다."),
    HAS_EMAIL(HttpStatus.BAD_REQUEST, "ACCOUNT-002", "이미 존재하는 이메일입니다."),
    INVALID_EMAIL_OR_PASSWORD(HttpStatus.BAD_REQUEST, "ACCOUNT-003", "이메일 또는 비밀번호가 일치하지 않습니다."),
    LOGINED_USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "ACCOUNT-004", "로그인한 사용자를 찾을 수 없습니다. 다시 로그인 해주세요.");

    private final HttpStatus httpStatus;	// HttpStatus
    private final String code;				// ACCOUNT-001
    private final String message;			// 설명

}
```

### 불필요한 코드 제거
- 사용되지 않는 Import문 이나, 수정 후 주석으로 처리된 코드를 제거 항.

### 관심사 분리(Repository에서 orElseThrow 없애기)
- Repository는 DB와 소통 하는 부분이기 때문에 Exception은 비지니스 로직은 Service에서 처리 하도록 수정했다.
-  Before(ScheduleRepository.java)  

  ``` java
   default Schedule findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no schedule for this id."));
    }
  ```

  - After (ScheduleServiceImpl.java)  

   ```java
   Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
    }
  ```

 ### 데이터 조회시  @Transactional(readOnly = true) 사용
 - 성능최적화 : 읽기 전용 트랙잭션은 데이터 변경을 방지하므로, 불필요한 쓰기 락이 걸리지 않도록 함.
 - 의도 명시 : 메서드가 데이터를 조회만 한다는 의도록 명확하게 명시.


   -  Before(ScheduleServiceImpl.java)  

  ``` java
    @Override// 일정 단건조회
    public ScheduleResponseDto findScheduleById(Long id) {
        //일정 id를 이용하여 불러옴
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        // 조회된 일정을 DTO타입으로 변환화여 반환
        return ScheduleResponseDto.toDto(schedule);
    }
    
  ```

  - After (ScheduleServiceImpl.java)  

   ```java
    @Override// 일정 단건조회
    @Transactional(readOnly = true)
    public ScheduleResponseDto findScheduleById(Long id) {
        :
        :
    }
  ```

 ### 로그인 한 사용자의 일정 또는 댓글만 수정 삭제 되도록 변경
 - 이전 코드에서는 본인이 작성한 일정이나 댓글이 아니어도 id만 입력받아 수정 삭제 되게끔 했지만,
 - 작성하려는 일정 또는 댓글이 로그인 한 사용자가 작성한게 아니라면 에러처리되도록 수정하였다.

```java
public Schedule ownerCheck(Long scheduleId){
        // 세션에서 로그인 된 유저의 id를 가져와 유저 불러옴.
        User user = userRepository.findByEmail((String) session.getAttribute("userEmail"))
                .orElseThrow(() -> new CustomException(UserErrorCode.LOGINED_USER_NOT_FOUND));
        // 일정 id로 일정 불러옴.
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        // 일정정보의 유저와 로그인 한 유저가 같은 지 확인.
        if(!schedule.getUser().equals(user)){
            throw new CustomException((ScheduleErrorCode.SCHEDULE_PERMISSION_DENIED));
        }
        return schedule;
    }

@Override
    @Transactional //일정 수정
    public ScheduleResponseDto modifyScheduleById(Long id, ScheduleRequestDto dto) {
        // 일정 id로 일정 불러옴.
        Schedule schedule = ownerCheck(id); // 수정하고자 하는 일정이 본인의 글이 아닐경우 예외처리
        schedule.UpdateTitleAndContents(dto);
        em.flush();//변경사항 즉시 반영
        return ScheduleResponseDto.toDto(schedule);
    }
```
<a id="refactorreview"></a>
# 수정 후기
 이번과제에서는 몸살+게으름 이슈로 도전 기능도 못했다... (아직도 힘듬...)
 이번주는 정신 꽉잡고 저번주 못했던 공부와 프로젝트 같이 병행해야게따...ㅠ
 
 뉴스피드 팀 프로젝트 이후로 실력이 많이 늘어난거같다!
 팀프로젝트에서 도메인 별로 패키징 하는법을 알게되었고, 이번 과제에서는 에러코드를 사용하여 예외 처리하는 방법도 써봤다.
 점점 갈수록 완벽해 지는 너낌..? JWT랑 test코드 부분을 좀더 공부해서 더더 완벽한 프로젝트를 완성할 수 있도록 좀더 열심히.. 공부해야겠다..
