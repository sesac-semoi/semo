package com.semoi.semo.jwt.service;

import com.semoi.semo.jwt.domain.RefreshToken;
import com.semoi.semo.jwt.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

// 차현철
@RequiredArgsConstructor
@Service
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    // 리프레시 토큰 조회
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unable to find refresh token"));
    }
}
