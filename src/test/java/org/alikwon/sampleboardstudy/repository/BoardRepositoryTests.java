package org.alikwon.sampleboardstudy.repository;

import org.alikwon.sampleboardstudy.dto.BoardDTO;
import org.alikwon.sampleboardstudy.dto.PageRequestDTO;
import org.alikwon.sampleboardstudy.dto.PageResultDTO;
import org.alikwon.sampleboardstudy.entity.Board;
import org.alikwon.sampleboardstudy.entity.Member;
import org.alikwon.sampleboardstudy.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardRepositoryTests {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardService boardService;

    @Test
    public void insertBoard() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = Member.builder().email("user" + i + "@test.com").build();
            Board board = Board.builder()
                    .title("Title - " + i)
                    .content("Content - " + i)
                    .writer(member)
                    .build();
            boardRepository.save(board);
        });
    }

    @Transactional
    @Test
    public void testBoardRead() {
        Optional<Board> result = boardRepository.findById(100L);

        Board board = result.get();

        System.out.println(board);
        System.out.println(board.getWriter());
    }

    @Test
    public void testReadWithWriterJPQL() {
        Object result = boardRepository.getBoardWithWriter(100L);
        Object[] arr = (Object[]) result;

        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void testGetBoardWithReplyJPQL() {
        List<Object[]> result = boardRepository.getBoardWithReply(100L);

        for (Object[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }
    }

    @Test
    public void testGetBoardWithReplyCountJPQL() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);
        result.get().forEach(row -> {
            Object[] arr = row;
            System.out.println(Arrays.toString(arr));
        });
    }

    @Test
    public void testGetBoardByBnoJPQL() {
        Object result = boardRepository.getBoardByBno(100L);
        Object[] arr = (Object[]) result;
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void testRegister() {
        BoardDTO dto = BoardDTO.builder()
                .title("Test Register")
                .content("Test ..........")
                .writerEmail("user1@test.com")
                .build();
        Long bno  = boardService.register(dto);
    }

    @Test
    public void testGetList() {
        PageRequestDTO requestDTO = new PageRequestDTO();

        PageResultDTO<BoardDTO, Object[]> resultDTO = boardService.getList(requestDTO);

        for (BoardDTO dto : resultDTO.getDtoList()) {
            System.out.println(dto);
        }
    }

    @Test
    public void testGet() {
        Long bno = 100L;

        BoardDTO dto = boardService.get(bno);

        System.out.println(dto);
    }

    @Test
    public void testDelete() {
        Long bno = 100L;

        boardService.removeWithReplies(bno);
    }

    @Test
    public void testModify() {
        BoardDTO dto = BoardDTO.builder()
                .bno(1L)
                .title("제목 변경할거임")
                .content("내용도 변경할거임")
                .build();
        boardService.modify(dto);
    }
}
