package org.alikwon.sampleboardstudy.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
    
    private Long rno;
    private String text;
    private String replyer;
    private Long bno; // 게시글 번호
    private LocalDateTime regDate;
    private LocalDateTime modDate;

}
