package org.alikwon.sampleboardstudy.repository;

import org.alikwon.sampleboardstudy.entity.Board;
import org.alikwon.sampleboardstudy.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    //Board 삭제시 댓글들 모두 삭제
    @Modifying
    @Query("DELETE FROM Reply r WHERE r.board.bno = :bno")
    void deleteReplyByBno(Long bno);

    //게시물로 댓글 목록 가져오기 -> 쿼리 메서드
    List<Reply> getRepliesByBoardOrderByRno(Board board);
}
