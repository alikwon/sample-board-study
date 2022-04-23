package org.alikwon.sampleboardstudy.entity;

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
public class Member extends BaseEntity {
    @Id
    private String email;

    private String password;

    private String name;
}
