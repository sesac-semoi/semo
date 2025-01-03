package com.semoi.semo.board.service;

import com.semoi.semo.board.dto.requestdto.BoardRequestDto;
import com.semoi.semo.board.dto.responsedto.BoardListResponseDto;
import com.semoi.semo.board.dto.responsedto.BoardResponseDto;
import com.semoi.semo.board.entity.Board;
import com.semoi.semo.board.mapper.BoardMapper;
import com.semoi.semo.board.repository.BoardRepository;
import com.semoi.semo.global.exception.DataNotFoundException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Page<BoardListResponseDto> getAllBoards(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Order.desc("createdAt")) // "createdAt" 기준으로 역순 정렬
        );
        return boardRepository.findAllActiveBoards(sortedPageable)
                .map(BoardMapper::toBoardListResponseDto);
    }

    public BoardResponseDto getBoardById(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new DataNotFoundException("board not found"));

        return BoardMapper.toBoardResponseDto(board);
    }

    public void createBoard(BoardRequestDto boardRequestDto) {
        Board board = BoardMapper.toEntity(boardRequestDto);
        boardRepository.save(board);
    }

    public void updateBoard(Long boardId, BoardRequestDto boardRequestDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new DataNotFoundException("board not found"));

        BoardMapper.updateEntity(board, boardRequestDto);
        boardRepository.save(board); // 반드시 호출
    }

    public void softDeleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new DataNotFoundException(("board not found")));

        board.setDeletedAt(LocalDateTime.now());
        boardRepository.save(board);
    }
}
