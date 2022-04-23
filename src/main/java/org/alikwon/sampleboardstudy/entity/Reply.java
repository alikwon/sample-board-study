package org.alikwon.sampleboardstudy.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "REPLY")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String text;

    private String replyer;

    @ManyToOne
    private Board board;
}
