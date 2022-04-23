package org.alikwon.sampleboardstudy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.alikwon.sampleboardstudy.dto.BoardDTO;
import org.alikwon.sampleboardstudy.dto.PageRequestDTO;
import org.alikwon.sampleboardstudy.dto.PageResultDTO;
import org.alikwon.sampleboardstudy.entity.Board;
import org.alikwon.sampleboardstudy.entity.Member;
import org.alikwon.sampleboardstudy.repository.BoardRepository;
import org.alikwon.sampleboardstudy.repository.ReplyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    @Override
    public Long register(BoardDTO dto) {
        log.info(dto);

        Board board = dtoToEntity(dto);
        boardRepository.save(board);

        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        log.info(pageRequestDTO);

        Function<Object[], BoardDTO> fn = (en ->
                entityToDto((Board) en[0], (Member) en[1], (Long) en[2]));

//        Page<Object[]> result = boardRepository.getBoardWithReplyCount(
//                pageRequestDTO.getPageable(Sort.by("bno").descending())
//        );

        Page<Object[]> result = boardRepository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending())
        );
        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO get(Long bno) {
        Object result = boardRepository.getBoardByBno(bno);
        Object[] arr = (Object[]) result;
        return entityToDto((Board) arr[0], (Member) arr[1], (Long) arr[2]);
    }

    @Transactional
    @Override
    public void removeWithReplies(Long bno) {
        replyRepository.deleteReplyByBno(bno);

        boardRepository.deleteById(bno);
    }

    @Transactional
    @Override
    public void modify(BoardDTO dto) {
        Board board = boardRepository.getById(dto.getBno());
        board.changeTitle(dto.getTitle());
        board.changeContent(dto.getContent());

        boardRepository.save(board);
    }
}
