package com.semoi.semo.bookmark.service;

import com.semoi.semo.applyForm.repository.ApplyFormRepository;
import com.semoi.semo.board.dto.responsedto.BoardResponseDto;
import com.semoi.semo.board.entity.Board;
import com.semoi.semo.board.repository.BoardRepository;
import com.semoi.semo.bookmark.domain.Bookmark;
import com.semoi.semo.bookmark.repository.BookmarkRepository;
import com.semoi.semo.comment.repository.CommentRepository;
import com.semoi.semo.global.exception.ErrorCode;
import com.semoi.semo.global.exception.SemoException;
import com.semoi.semo.user.domain.User;
import com.semoi.semo.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final ApplyFormRepository applyFormRepository;
    private final CommentRepository commentRepository;

    @Override
    public Boolean getState(String loginEmail, Long boardId) {
        User user = getUserByEmailOrElseThrow(loginEmail);
        Board board = getBoardByIdOrElseThrow(boardId);

        return bookmarkRepository.findByUserAndBoard(user, board).isPresent();
    }

    @Override
    public void addOrCancelBookmark(String loginEmail, Long boardId) {

        User user = getUserByEmailOrElseThrow(loginEmail);
        Board board = getBoardByIdOrElseThrow(boardId);

        Bookmark bookmark = bookmarkRepository.findByUserAndBoard(user, board).orElse(null);

        // 북마크 안한 경우, 추가
        if (bookmark == null) {
            bookmark = Bookmark.create(user, board);
            bookmarkRepository.save(bookmark);
        } else {
        // 북마크 되어 있는 경우, 취소
            bookmarkRepository.delete(bookmark);
        }
    }

    public List<BoardResponseDto> getBookmarksFromUser(String loginEmail) {
        User user = getUserByEmailOrElseThrow(loginEmail);

        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserOrderByCreatedAtDesc(user);

        List<BoardResponseDto> boardListDtos = new ArrayList<>();
        for (Bookmark bookmark : bookmarks) {
            Board board = boardRepository.findById(bookmark.getBookmarkId())
                    .orElse(null);

            if (board == null) {
                continue;
            }

            // ApplyFormRepository에서 boardId와 userId를 기준으로 존재 여부 확인
            boolean isParticipated = applyFormRepository.existsByBoardIdAndUserId(board.getBoardId(), user.getUserId());

            BoardResponseDto boardResponseDto = BoardResponseDto.fromEntity(board, loginEmail, isParticipated, applyFormRepository, commentRepository);

            boardListDtos.add(boardResponseDto);
        }

        return boardListDtos;
    }

    private User getUserByEmailOrElseThrow(String loginEmail) {
        return userRepository.findByLoginEmail(loginEmail)
                .orElseThrow(() -> new SemoException(ErrorCode.USER_NOT_FOUND));
    }

    private Board getBoardByIdOrElseThrow(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new SemoException(ErrorCode.BOARD_NOT_FOUND));
    }

}