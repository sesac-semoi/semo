import React from "react";
import { useNavigate } from "react-router-dom";
import * as S from "./style";

const Login = () => {
  const navigate = useNavigate(); // 페이지 이동을 위한 useNavigate 훅

  const handleKakaoClick = () => {
    navigate("/signup"); 
  };

  return (
    <S.LoginWrapper>
      <S.LoginBox>
        <S.Logo src="/img/logo.png" alt="logo" />
        <S.Text1>새모 시작하기</S.Text1>
        <S.Text2>
          다양한 교육생들과 함께하는 협업의 기회, <br />
          새모에서 오늘부터 시작하세요.
        </S.Text2>

        <S.ButtonWrapper>
          <S.KakaoButton onClick={handleKakaoClick}>
            카카오 계정으로 시작하기
          </S.KakaoButton>
        </S.ButtonWrapper>

        <S.TermsText>
          회원가입 시 새모의 <br />
          이용약관과 개인정보처리방침에 동의하게 됩니다.
        </S.TermsText>
      </S.LoginBox>
    </S.LoginWrapper>
  );
};

export default Login;
