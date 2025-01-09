package com.gachtaxi.domain.members.controller;

import com.gachtaxi.domain.members.dto.request.InactiveMemberAuthCodeRequestDto;
import com.gachtaxi.domain.members.dto.response.InactiveMemberResponseDto;
import com.gachtaxi.domain.members.service.AuthService;
import com.gachtaxi.domain.members.service.MemberService;
import com.gachtaxi.global.auth.enums.OauthLoginStatus;
import com.gachtaxi.global.auth.jwt.annotation.CurrentMemberId;
import com.gachtaxi.global.auth.jwt.dto.JwtTokenDto;
import com.gachtaxi.global.auth.jwt.service.JwtService;
import com.gachtaxi.global.common.mail.dto.request.EmailAddressDto;
import com.gachtaxi.global.common.mail.service.EmailService;
import com.gachtaxi.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.gachtaxi.domain.members.controller.ResponseMessage.*;
import static com.gachtaxi.global.auth.kakao.dto.KaKaoDTO.KakaoAuthCode;
import static com.gachtaxi.global.auth.kakao.dto.KaKaoDTO.OauthKakaoResponse;
import static com.gachtaxi.global.common.mail.message.ResponseMessage.EMAIL_AUTHENTICATION_SUCESS;
import static com.gachtaxi.global.common.mail.message.ResponseMessage.EMAIL_SEND_SUCCESS;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final EmailService emailService;
    private final AuthService authService;
    private final JwtService jwtService;
    private final MemberService memberService;

    @PostMapping("/login/kakao")
    @Operation(summary = "인가 코드를 전달받아, 소셜 로그인을 진행합니다.")
    public ApiResponse<OauthKakaoResponse> kakaoLogin(@RequestBody @Valid KakaoAuthCode kakaoAuthCode, HttpServletResponse response) {
        OauthKakaoResponse res = authService.kakaoLogin(kakaoAuthCode.authCode(), response);
        ResponseMessage OAUTH_STATUS = (res.status() == OauthLoginStatus.LOGIN)
                ? LOGIN_SUCCESS
                : UN_REGISTER;
        return ApiResponse.response(HttpStatus.OK, OAUTH_STATUS.getMessage(), res);
    }

    @PostMapping("/refresh")
    @Operation(summary = "RefreshToken으로 AccessToken과 RefreshToken을 재발급 하는 API 입니다.")
    public ApiResponse<Void> reissueRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        JwtTokenDto jwtTokenDto = jwtService.reissueJwtToken(request);

        jwtService.setCookie(jwtTokenDto.refreshToken(), response);
        jwtService.setHeader(jwtTokenDto.accessToken(), response);
        return ApiResponse.response(HttpStatus.OK, REFRESH_TOKEN_REISSUE.getMessage());
    }

    @PostMapping("/code/mail")
    @Operation(summary = "이메일 인증 코드를 보내는 API입니다.")
    public ApiResponse sendEmail(
            @RequestBody @Valid EmailAddressDto emailDto,
            @CurrentMemberId Long userId
    ) {
        emailService.sendEmail(emailDto.email());
        return ApiResponse.response(OK, EMAIL_SEND_SUCCESS.getMessage(), InactiveMemberResponseDto.from(userId));
    }

    @PatchMapping("/code/mail")
    @Operation(summary = "사용자가 입력한 인증 코드를 검증 후 이메일 정보를 업데이트하는 API 입니다.")
    public ApiResponse checkAuthCodeAndUpdateEmail(
            @RequestBody @Valid InactiveMemberAuthCodeRequestDto dto,
            @CurrentMemberId Long userId
    ) {
        emailService.checkEmailAuthCode(dto.email(), dto.authCode());
        memberService.updateInactiveMemberOfEmail(dto.email(), userId);
        return ApiResponse.response(OK, EMAIL_AUTHENTICATION_SUCESS.getMessage(), InactiveMemberResponseDto.from(userId));
    }
}
