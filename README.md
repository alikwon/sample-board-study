# N:1 관계  

---
# Spring boot프로젝트 생성

- Java Version : 8
- Type : gradle
- packaging : war
- 의존성 설정
  - Spring Boot
  - DevTools
  - Lombok
  - Spring Web
  - Thymeleaf
  - MariaDB

---

# 연관관계와 관계형 DB설계

## PK로 설계, FK로 연관관계 해석

- ERD 다이어그램

  ![img](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/d4fcad81-5d82-4f08-8e61-8ad037659b16/Untitled.png)

- 관계형 DB에서는 PK와 FK만으로 표현되었던 관계가 객체지향으로 옮겨지면 다양한 선택지가 존재하게 된다.
  - 회원 엔티티가 게시물 엔티티를 참조하게 해야하는가?
  - 게시물 엔티티가 회원 엔티티를 참조하게 해야하는가?
  - 회원, 게시물 객체 양쪽에서 서로 참조해야하는가?
- 실제 DB에서 는 **양방향이라는 말은 존재하지 않으므로** 객체지향에서만 겪는 문제

## 엔티티 클래스 추가

### 1. `Member` 엔티티 작성

- Member.java

    ```java
    import lombok.*;
    
    import javax.persistence.Entity;
    import javax.persistence.Id;
    import javax.persistence.Table;
    
    @Entity
    @Table(name = "MEMBER")
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @ToString
    @Builder
    public class Member {
        @Id
        private String email;
    
        private String password;
    
        private String name;
    }
    ```


### `Board` 엔티티 작성

- Board.java

    ```java
    @Entity
    @Table(name = "BOARD")
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @ToString
    public class Board {
    
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long bno;
    
        private String title;
    
        private String content;
    }
    ```


### `Reply` 엔티티 작성

- Reply.java

    ```java
    @Entity
    @Table(name = "REPLY")
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @ToString
    public class Reply {
    
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long rno;
    
        private String text;
    
        private String replyer;
    }
    ```
## `@ManyToOne` 어노테이션

- `Member` 테이블의 email을 `Board` 에서 FK로 참조하는 구조 → **N:1의 관계**
- JPA에서는 N:1 관계를 의미하는 `@ManyToOne` 을 적용해야함 → **외래키의 관계**로 이해하면 됨.
  - `Board` 엔티티에 `Member`  엔티티를 참조하는 부분 추가

      ```java
      ...
          @ManyToOne
          private Member writer;
      }
      ```

  - `Reply` 엔티티에도 `Board` 엔티티를 참조하는 부분 추가
- 실행시 **DDL**

    ```sql
    -- create 후 alter 문으로 FK 추가됨
    Hibernate: 
        alter table board 
         add constraint FK1iu8rhoim4thb0y12cpt01oiu 
         foreign key (writer_email) 
         references member (email)
    
    ...
    
    Hibernate:
    		alter table reply
    			add constraint FKr1bmblqir7dalmh47ngwo7mcs
    			foreign key (board_bno)
    			references board (bno)
    ```


## `Repository` interfase 추가

### `MemberRepository` 인터페이스

- `MemberRepository` 와 같이 `BoardRepository` 와 `ReplyRepository`도 추가할 것
- 소스코드

    ```java
    import org.alikwon.sampleboardstudy.entity.Member;
    import org.springframework.data.jpa.repository.JpaRepository;
    
    public interface MemberRepository extends JpaRepository<Member, String> {
    }
    ```


---

# 단위 테스트

## 1. 테스트 데이터 추가

- `Member` : 100 개 데이터
- `Board` : member 1명당 1개의 게시글 (100개)
- `Reply` : 300 개 reply insert (random bno 생성)
- 테스트데이터 추가 소스코드

    ```java
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private BoardRepository boardRepository;
    
    @Autowired
    private ReplyRepository replyRepository;
    
    // 100 개 member insert
    @Test
    public void insertMembers() {
        IntStream.rangeClosed(1,100).forEach(i -> {
            Member member = Member.builder()
                    .email("user" + i + "@test.com")
                    .password("1111")
                    .name("유저" + i)
                    .build();
            memberRepository.save(member);
        });
    }
    
    // 100 개 board insert (member 1명당 1개의 게시글)
    @Test
    public void insertBoard() {
        IntStream.rangeClosed(1,100).forEach(i -> {
            Member member = Member.builder().email("user" + i + "@test.com").build();
            Board board = Board.builder()
                    .title("Title - " + i)
                    .content("Content - " + i)
                    .writer(member)
                    .build();
            boardRepository.save(board);
        });
    }
    
    // 300 개 reply insert (random bno 생성)
    @Test
    public void insertReply() {
        IntStream.rangeClosed(1, 300).forEach(i -> {
            long bno = (long) (Math.random() * 100) + 1;
            Board board = Board.builder().bno(bno).build();
    
            Reply reply = Reply.builder()
                    .text("Reply - " + i)
                    .replyer("guest")
                    .board(board)
                    .build();
            replyRepository.save(reply);
        });
    }
    
    ```


### - 필요한 쿼리 기능 정리

- 목록 : 게시글 번호, 제목, 댓글의 개수, 작성자 이름/이메일
- 조회 : 게시글 번호, 제목, 내용, 댓글 개수, 작성자 이름/이메일

## 2. `@ManyToOne` 과 Eager/Lazy loading

- 연관관계를 맺고 있다는 것은 DB입장에서는 JOIN이 필요하다는 것
- `@ManyToOne` 의 경우 **FK** 쪽을 가져올때 **PK** 쪽의 엔티티도 가져옴
  - 테스트 코드

      ```java
      @Test
      public void testRead1(){
          Optional<Board> result = boardRepository.findById(1L);
          Board board = result.get();
          System.out.println(board);
          System.out.println(board.getWriter());
      }
      ```

    - 실행 시 **자동으로 조인**처리 되는 것을 확인

      ```sql
      Hibernate: 
          select
              board0_.bno as bno1_0_0_,
              board0_.mod_dt as mod_dt2_0_0_,
              board0_.reg_dt as reg_dt3_0_0_,
              board0_.content as content4_0_0_,
              board0_.title as title5_0_0_,
              board0_.writer_mno as writer_m6_0_0_,
              member1_.mno as mno1_2_1_,
              member1_.mod_dt as mod_dt2_2_1_,
              member1_.reg_dt as reg_dt3_2_1_,
              member1_.email as email4_2_1_,
              member1_.name as name5_2_1_,
              member1_.password as password6_2_1_ 
          from
              board board0_ 
          left outer join
              member member1_ 
                  on board0_.writer_mno=member1_.mno 
          where
              board0_.bno=?
      ```

- `Board` 의 ID 를 FK로 가지고 있는 `Reply` 엔티티의 경우, `Member` , `Board` , `Reply` 가 모두 JOIN 처리 된다
  - 이렇게 ***연관관계를 가진 모든 엔티티를 같이 로딩*** 하는것을 `**Eager loading**` 이라고 함
  - 연관관계가 복잡할수록 JOIN 으로 인한 성능저하가 있어 비효율 적이다


### `fetch` 는 Lazy loading 을 권장

- JPA 에서 연관관계의 데이터를 가져오는 방식
- 연관관계의 어노테이션 속성으로 `fetch` 모드를 지정한다.
  - 위의 `Board` 엔티티의 `@ManyToOne` 어노테이션에 속성 추가

    ```java
    ...
    	@ManyToOne (fetch = FetchType.LAZY)
    	private Member writer; // 연관 관계 지정
    }
    ```

  - `**Lazy loading**`
    - 지연로딩
    - 장점 : 조인없이 단순히 하나의 테이블을 이용하는 경우 빠른 속도 처리가 가능
    - 단점 : 연관관계가 복잡할 수록 여러번의 쿼리가 실행됨

      <aside>
      💡 **테스트 코드 실행 시 `@Transactional` 을 사용**

    1. Lazy loading을 사용하여 join이 발생하지 않음
    2. 따라서  `board.getWriter()`에서 `Member` 테이블을 로딩해야하는데 이미 DB와의 연결은 끝난 상태 → 예외 발생
    - 해당 메서드를 하나의 트렌젝션으로 처리하므로써 `board.getWriter()` 시에 member 테이블을 로딩하게됨
      </aside>


> **지연 로딩을 기본으로 사용하고, 상황에 맞게 필요한 방법을 찾는다’**
>

### 지연로딩시 연관관계에서는 `@ToString` 주의

- `@ToString` 은 해당 클래스의 모든 멤버변수를 출력한다
- 따라서 `Board` 클래스 출력 시, **writer** 변수를 출력하기 위해서 `Member` 객체역시 출력해야하므로 DB연결이 필요하다
- `@ToString` 의 **exclude** 속성을 사용하여 **writer** 변수를 제외시켜 준다.

    ```java
    ...
    @ToString(exclude = "writer")
    public class Board extends BaseEntity {
    	...
    }
    ```


## 3. JPQL과 LEFT ( OUTER ) JOIN

- 스프링 부트 2버전 이후에 포함되는 JPA 버전은 **엔티티 클래스 내에 전혀 연관관계가 없다라도 JOIN** 을 이용할 수 있음.

### 엔티티 클래스 내부에 연관 관계가 있는경우

- `BoardRepository` 인터페이스
  - 한개의 로우(Object) 내에 Object[]로 나옴
  - `@Query` 내용을 보면 JOIN 시 ON을 이용하는 부분이 없지만, 엔티티 내부에 연관관계가 있기 때문에 실행시 자동으로 ON 이 붙는다

    ```java
    public interface BoardRepository extends JpaRepository<Board, Long>, QuerydslPredicateExecutor {
        // 한개의 로우(Object) 내에 Object[]로 나옴
        @Query("select b,w from Board b left join b.writer w where b.bno = :bno")
        Object getBoardWithWriter(@Param("bno") Long bno);
    }
    ```

- 테스트 코드

    ```java
    @Test
    public void testReadWithWriter(){
        Object result = boardRepository.getBoardWithWriter(50L);
        Object[] arr = (Object[]) result;
        System.out.println("=================================");
        System.out.println(Arrays.toString(arr));
    }
    
    /*
    - 출력
    	[Board(bno=50, title=Title - 50, content=Content - 50)
    		, Member(mno=50, email=user50@test.com, name=유저50, password=1234)]
    */
    
    ```

  - 실행 DML을 보면 ON이 생성된다

    ```sql
    select
            ...
        from
            board board0_ 
        left outer join
            member member1_ 
                on board0_.writer_mno=member1_.mno 
        where
            board0_.bno=?
    ```


### 연관관계가 없는 엔티티 조인 처리에는 on

- `Board` 입장에서는 `Reply` 객체를 참조하고 있지 않고 있다
- 이런경우에는 on을 이용하여 작성해야함

    ```java
    @Query("SELECT b, r FROM Board b LEFT JOIN Reply r ON r.board = b WHERE b.bno = :bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);
    ```


### 목록화면에 필요한 JPQL

- 게시물 : 게시물번호, 제목, 게시물 작성시간
- 회원 : 회원의 이름/이메일
- 댓글 : 게시물의 댓글 수
- `Board`내에서 `Reply`는 연관관계가 없는 상황
  - 하나의 라인으로 처리될 수 있도록 GROUP BY처리
- `Pageable`을 파라미터로 전달해서, `Page<Object[]>` 를 리턴타입으로 JPQL 정의
- 소스코드

    ```java
    @Query(value = "SELECT b, w, count(r)" +
                "FROM  Board b " +
                "LEFT JOIN b.writer w" +
                "LEFT JOIN Reply r ON r.board = b" +
                "GROUP BY b",
                countQuery = "SELECT COUNT(b) FROM Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);
    ```


### 조회화면에 필요한 JPQL

- 실제 댓글은 주로 화면에서 AJAX 를 통해서 필요한 순간에 동적으로 데이터 가져오는 방식
- 소스코드

    ```java
    @Query("SELECT b, w, COUNT(r) " +
            "FROM Board b " +
            "LEFT JOIN b.writer w " +
            "LEFT JOIN Reply r ON r.board = b " +
            "WHERE b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);
    ```