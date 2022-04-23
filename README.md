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
