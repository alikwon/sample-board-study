package org.alikwon.sampleboardstudy.service;

import org.alikwon.sampleboardstudy.dto.ReplyDTO;
import org.alikwon.sampleboardstudy.entity.Board;
import org.alikwon.sampleboardstudy.entity.Member;
import org.alikwon.sampleboardstudy.entity.Reply;

import java.util.List;

public interface ReplyService {

    Long register(ReplyDTO dto);

    List<ReplyDTO> getList(Long bno);

    void modify(ReplyDTO dto);

    void remove(Long rno);

    default Reply dtoToEntity(ReplyDTO dto) {
        Board board = Board.builder().bno(dto.getBno()).build();

        Reply reply = Reply.builder()
                .rno(dto.getRno())
                .text(dto.getText())
                .replyer(dto.getReplyer())
                .board(board)
                .build();
        return reply;
    }

    default ReplyDTO entityToDto(Reply entity) {
        ReplyDTO dto = ReplyDTO.builder()
                .rno(entity.getRno())
                .text(entity.getText())
                .replyer(entity.getReplyer())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto;
    }
}
