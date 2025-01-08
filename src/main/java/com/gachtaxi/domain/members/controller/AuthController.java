package com.gachtaxi.domain.members.controller;

import com.gachtaxi.domain.members.service.AuthService;
import com.gachtaxi.global.auth.enums.OauthLoginStatus;
import com.gachtaxi.global.auth.jwt.dto.JwtTokenDto;
import com.gachtaxi.global.auth.jwt.service.JwtService;
import com.gachtaxi.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.gachtaxi.domain.members.controller.ResponseMessage.*;
import static com.gachtaxi.global.auth.kakao.dto.KaKaoDTO.KakaoAuthCode;
import static com.gachtaxi.global.auth.kakao.dto.KaKaoDTO.OauthKakaoResponse;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/login/kakao")
    public ApiResponse<OauthKakaoResponse> kakaoLogin(@RequestBody @Valid KakaoAuthCode kakaoAuthCode, HttpServletResponse response) {
        OauthKakaoResponse res = authService.kakaoLogin(kakaoAuthCode.authCode(), response);
        ResponseMessage OAUTH_STATUS = (res.status() == OauthLoginStatus.LOGIN)
                ? LOGIN_SUCCESS
                : UN_REGISTER;
        return ApiResponse.response(HttpStatus.OK, OAUTH_STATUS.getMessage(), res);
    }

    @PostMapping("/refresh")
    public ApiResponse<Void> reissueRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        JwtTokenDto jwtTokenDto = jwtService.reissueJwtToken(request);

        jwtService.setCookie(jwtTokenDto.refreshToken(), response);
        jwtService.setHeader(jwtTokenDto.accessToken(), response);
        return ApiResponse.response(HttpStatus.OK, REFRESH_TOKEN_REISSUE.getMessage());
    }
}
