package org.alikwon.sampleboardstudy.repository;

import org.alikwon.sampleboardstudy.entity.Board;
import org.alikwon.sampleboardstudy.entity.Member;
import org.alikwon.sampleboardstudy.entity.Reply;
import org.alikwon.sampleboardstudy.service.ReplyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class ReplyRepositoryTests {

    @Autowired
    ReplyRepository replyRepository;


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

    @Test
    public void testListByBoard() {
        List<Reply> replyList = replyRepository.getRepliesByBoardOrderByRno(Board.builder().bno(99L).build());
        replyList.forEach(reply -> {
            System.out.println(reply);
        });
    }

}
