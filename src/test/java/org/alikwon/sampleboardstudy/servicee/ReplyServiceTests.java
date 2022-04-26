package org.alikwon.sampleboardstudy.servicee;

import org.alikwon.sampleboardstudy.dto.ReplyDTO;
import org.alikwon.sampleboardstudy.entity.Board;
import org.alikwon.sampleboardstudy.entity.Reply;
import org.alikwon.sampleboardstudy.repository.ReplyRepository;
import org.alikwon.sampleboardstudy.service.ReplyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class ReplyServiceTests {

    @Autowired
    ReplyService replyService;

    @Test
    public void testGetList() {
        Long bno = 99L;
        List<ReplyDTO> list = replyService.getList(bno);
        list.forEach(replyDTO -> System.out.println(replyDTO));
    }

}
