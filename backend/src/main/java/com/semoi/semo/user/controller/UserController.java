package com.semoi.semo.user.controller;

import com.semoi.semo.global.response.Response;
import com.semoi.semo.jwt.service.TokenProvider;
import com.semoi.semo.jwt.service.TokenService;
import com.semoi.semo.oauth2.util.CookieUtil;
import com.semoi.semo.user.dto.request.UserInfoRequestDto;
import com.semoi.semo.user.dto.response.UserResponseDto;
import com.semoi.semo.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

// 차현철
@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final TokenProvider tokenProvider;
    private static final String REFRESH_TOKEN_KEY_NAME = "refresh_token";

    @Operation(
            summary = "사용자 정보 저장",
            description = "로그인된 사용자의 정보를 저장합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 정보 저장 성공",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
    })
    @PutMapping
    public Response<UserResponseDto> saveInfo(HttpServletRequest request, @RequestBody UserInfoRequestDto userInfoRequestDto) {
        return Response.success(userService.updateUser(tokenProvider.getUserLoginEmail(request), userInfoRequestDto));
    }

    @Operation(
            summary = "로그인된 사용자 정보 조회",
            description = "현재 로그인된 사용자의 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
    })
    @GetMapping
    public Response<UserResponseDto> getLoginUser(HttpServletRequest request) {
        return Response.success(UserResponseDto.toDto(userService.getUserByLoginEmailOrElseThrow(tokenProvider.getUserLoginEmail(request))));
    }

    @Operation(
            summary = "로그아웃",
            description = "로그아웃 처리 후 Refresh Token 삭제 및 쿠키 제거를 수행합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "로그아웃 성공",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
    })
    @PostMapping("/logout")
    public Response<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        tokenService.deleteRefreshToken(tokenProvider.getUserLoginEmail(request));

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_KEY_NAME);
        return Response.success();
    }

    @Operation(
            summary = "신규 유저 확인 API",
            description = "신규 유저의 경우 true, 기존 유저의 경우 false 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = Response.class))
            ),
    })
    @GetMapping("/check")
    public Response<Boolean> checkNewUser(HttpServletRequest request) {
        return Response.success(userService.getCheckNewUser(tokenProvider.getUserLoginEmail(request)));
    }
}
