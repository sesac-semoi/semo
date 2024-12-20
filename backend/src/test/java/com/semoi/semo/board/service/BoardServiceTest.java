package com.semoi.semo.board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.semoi.semo.board.dto.responsedto.BoardListResponseDto;
import com.semoi.semo.board.entity.Board;
import com.semoi.semo.board.repository.BoardRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardServiceTest {

    private final BoardRepository boardRepository = Mockito.mock(BoardRepository.class);
    private final BoardService boardService = new BoardService(boardRepository);

    @Test
    void testGetAllBoards() {
        // Mock 데이터 설정
        Board mockBoard1 = new Board();
        mockBoard1.setBoardId(1L);
        mockBoard1.setTitle("First Board");
        mockBoard1.setHit(100);
        mockBoard1.setRecruitmentType("Full-time");
        mockBoard1.setRecruitmentCount(5);
        mockBoard1.setRecruitmentField("Engineering");
        mockBoard1.setRecruitmentMethod("Online");
        mockBoard1.setRecruitmentDeadline(LocalDateTime.now().plusDays(7));
        mockBoard1.setProgressPeriod("3 months");
        mockBoard1.setCreatedAt(LocalDateTime.now());
        mockBoard1.setUpdatedAt(LocalDateTime.now());

        Board mockBoard2 = new Board();
        mockBoard2.setBoardId(2L);
        mockBoard2.setTitle("Second Board");
        mockBoard2.setHit(200);
        mockBoard2.setRecruitmentType("Part-time");
        mockBoard2.setRecruitmentCount(2);
        mockBoard2.setRecruitmentField("Marketing");
        mockBoard2.setRecruitmentMethod("Offline");
        mockBoard2.setRecruitmentDeadline(LocalDateTime.now().plusDays(14));
        mockBoard2.setProgressPeriod("1 month");
        mockBoard2.setCreatedAt(LocalDateTime.now());
        mockBoard2.setUpdatedAt(null);

        List<Board> mockBoardList = Arrays.asList(mockBoard1, mockBoard2);

        // Mock Repository 반환값 설정
        when(boardRepository.findAll()).thenReturn(mockBoardList);

        // Service 호출
        List<BoardListResponseDto> boardDtos = boardService.getAllBoards();

        // 검증
        assertThat(boardDtos).isNotNull();
        assertThat(boardDtos.size()).isEqualTo(2);

        // 첫 번째 게시글 검증
        BoardListResponseDto firstDto = boardDtos.get(0);
        assertThat(firstDto.getBoardId()).isEqualTo(1L);
        assertThat(firstDto.getTitle()).isEqualTo("First Board");
        assertThat(firstDto.getRecruitmentType()).isEqualTo("Full-time");

        // 두 번째 게시글 검증
        BoardListResponseDto secondDto = boardDtos.get(1);
        assertThat(secondDto.getBoardId()).isEqualTo(2L);
        assertThat(secondDto.getTitle()).isEqualTo("Second Board");
        assertThat(secondDto.getRecruitmentType()).isEqualTo("Part-time");
        assertThat(secondDto.getUpdatedAt()).isNull();
    }
}