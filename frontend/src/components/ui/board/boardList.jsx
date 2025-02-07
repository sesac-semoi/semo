import React, { useState } from "react";
import * as S from "./boardListStyle";
import { LiaHandPaper } from "react-icons/lia";
import Modal from "../modal/modalWrite";
import { truncate } from "../../../utils/truncateText";
import { replaceNewlinesWithSpace } from "../../../utils/replaceUtil";
import formatRelativeTime from "../../../utils/formatTime";

// 이유진
const BoardListWrite = ({ boardData }) => {
  const [isParticipated, setIsParticipated] = useState(boardData?.isParticipated || false);
  const boardId = boardData?.boardId || "2";
  const title = boardData?.title || "제목을 불러오는 중...";
  const content = boardData?.content || "내용을 불러오는 중...";
  const author = boardData?.author?.username || "작성자 없음";
  const createdAt = boardData?.createdAt || "작성일 불러오는 중...";
  const hit = boardData?.hit || "0";
  const commentCount = boardData?.commentCount || "0";
  const applyForms = boardData?.applyForms || {
    backend: 0,
    frontend: 0,
    marketer: 0,
    designer: 0,
  };
  const isClosed = boardData?.isClosed || false;

  const [open, setOpen] = useState(false);

  // 모달 열기
  const modalOpen = (e) => {
    e.stopPropagation(); // 이벤트 전파 중단
    e.preventDefault(); // 기본 동작 방지

    if (isParticipated) {
      // 이미 참여한 경우
      alert("이미 지원했습니다. 수정을 원하시면 마이페이지에서 해주세요.");
    } else {
      setOpen(true); // 모달 열기
    }
  };

  // 모달 닫기
  const closeModal = () => {
    setOpen(false); // 모달 닫기
  };

  return (
    <>
      <S.LinkContainer to={`/boards/${boardId}`} state={{ boardData }}>
        <S.BoardListContainer>
          {/* 조건부 스타일링 */}
          <S.RightTop $isParticipated={isParticipated}>
            <LiaHandPaper onClick={modalOpen} />
          </S.RightTop>
          <S.Row>
            <S.TitleContainer>
              <S.Badge $isClosed={isClosed}>{isClosed ? "모집마감" : "모집중"}</S.Badge>
              <S.Title>{title}</S.Title>
            </S.TitleContainer>
          </S.Row>

          <S.Content>{truncate(replaceNewlinesWithSpace(content), 80)}</S.Content>

          <S.InfoContainer>
            <S.InfoItem>
              <div>{author}</div>
            </S.InfoItem>
            <S.InfoItem>
              <div>・ {formatRelativeTime(createdAt)}</div>
            </S.InfoItem>
            <S.InfoItem>
              <S.Icon>
                <S.EyeIcon />
              </S.Icon>
              <div>{hit}</div>
            </S.InfoItem>
            <S.InfoItem>
              <S.Icon>
                <S.CommentIcon />
              </S.Icon>
              <div>{commentCount}</div>
            </S.InfoItem>
            <S.ApplicantInfo $isClosed={isClosed}>
              <div>지원자 수 |</div>
              <div>프론트엔드 {applyForms.frontend}명</div>
              <div>백엔드 {applyForms.backend}명</div>
              <div>UI/UX {applyForms.designer}명</div>
              <div>마케터 {applyForms.marketer}명</div>
            </S.ApplicantInfo>
          </S.InfoContainer>
        </S.BoardListContainer>
      </S.LinkContainer>
      <Modal
        isOpen={open}
        onClose={closeModal}
        boardId={boardId}
        setIsParticipated={setIsParticipated} // 상태 변경 함수 전달
      />
    </>
  );
};

export default BoardListWrite;
