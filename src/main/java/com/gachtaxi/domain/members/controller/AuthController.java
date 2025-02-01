package com.gachtaxi.domain.members.controller;

import com.gachtaxi.domain.members.dto.request.InactiveMemberAuthCodeRequestDto;
import com.gachtaxi.domain.members.dto.request.MemberAgreementRequestDto;
import com.gachtaxi.domain.members.dto.request.MemberSupplmentRequestDto;
import com.gachtaxi.domain.members.dto.request.MemberTokenDto;
import com.gachtaxi.domain.members.dto.response.LoginDto;
import com.gachtaxi.domain.members.dto.response.MemberLoginResponseDto;
import com.gachtaxi.domain.members.dto.response.MemberResponseDto;
import com.gachtaxi.domain.members.service.AuthService;
import com.gachtaxi.domain.members.service.MemberService;
import com.gachtaxi.global.auth.google.dto.GoogleAuthCode;
import com.gachtaxi.global.auth.jwt.annotation.CurrentMemberId;
import com.gachtaxi.global.auth.jwt.dto.JwtTokenDto;
import com.gachtaxi.global.auth.jwt.service.JwtService;
import com.gachtaxi.global.auth.jwt.util.CookieUtil;
import com.gachtaxi.global.common.mail.dto.request.EmailAddressDto;
import com.gachtaxi.global.common.mail.service.EmailService;
import com.gachtaxi.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.gachtaxi.domain.members.controller.ResponseMessage.*;
import static com.gachtaxi.global.auth.jwt.util.JwtProvider.ACCESS_TOKEN_SUBJECT;
import static com.gachtaxi.global.auth.jwt.util.JwtProvider.REFRESH_TOKEN_SUBJECT;
import static com.gachtaxi.global.auth.kakao.dto.KaKaoDTO.KakaoAuthCode;
import static com.gachtaxi.global.common.mail.message.ResponseMessage.*;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final EmailService emailService;
    private final CookieUtil cookieUtil;
    private final AuthService authService;
    private final JwtService jwtService;
    private final MemberService memberService;

    @PostMapping("/login/kakao")
    @Operation(summary = "인가 코드를 전달받아, 카카오 소셜 로그인을 진행합니다.")
    public ApiResponse<MemberLoginResponseDto> kakaoLogin(
            @RequestBody @Valid KakaoAuthCode kakaoAuthCode
            , HttpServletResponse response)
    {
        LoginDto loginDto = authService.kakaoLogin(kakaoAuthCode.authCode());
        response.setHeader(ACCESS_TOKEN_SUBJECT, loginDto.jwtTokenDto().accessToken());

        if (loginDto.isTemporaryUser()) { // 임시 유저
            return ApiResponse.response(OK, UN_REGISTER.getMessage(), MemberLoginResponseDto.from());
        }

        cookieUtil.setCookie(REFRESH_TOKEN_SUBJECT, loginDto.jwtTokenDto().refreshToken(), response);
        return ApiResponse.response(
                OK,
                LOGIN_SUCCESS.getMessage(),
                MemberLoginResponseDto.from(loginDto.memberResponseDto())
        );
    }

    @PostMapping("/login/google")
    @Operation(summary = "인가 코드를 전달받아, 구글 소셜 로그인을 진행합니다.")
    public ApiResponse<MemberLoginResponseDto> googleLogin(
            @RequestBody @Valid GoogleAuthCode googleAuthCode
            , HttpServletResponse response)
    {
        LoginDto loginDto = authService.googleLogin(googleAuthCode.authCode());
        response.setHeader(ACCESS_TOKEN_SUBJECT, loginDto.jwtTokenDto().accessToken());

        if (loginDto.isTemporaryUser()) { // 임시 유저
            return ApiResponse.response(HttpStatus.OK, UN_REGISTER.getMessage(), MemberLoginResponseDto.from());
        }

        cookieUtil.setCookie(REFRESH_TOKEN_SUBJECT, loginDto.jwtTokenDto().refreshToken(), response);
        return ApiResponse.response(
                OK,
                LOGIN_SUCCESS.getMessage(),
                MemberLoginResponseDto.from(loginDto.memberResponseDto())
        );    }

    @PostMapping("/refresh")
    @Operation(summary = "RefreshToken으로 AccessToken과 RefreshToken을 재발급 하는 API 입니다.")
    public ApiResponse<Void> reissueRefreshToken(
            @CookieValue(value = REFRESH_TOKEN_SUBJECT) String refreshToken,
            HttpServletResponse response
    ) {

        JwtTokenDto jwtTokenDto = jwtService.reissueJwtToken(refreshToken);
        responseToken(jwtTokenDto, response);

        return ApiResponse.response(OK, REFRESH_TOKEN_REISSUE.getMessage());
    }

    @PostMapping("/code/mail")
    @Operation(summary = "이메일 인증 코드를 보내는 API입니다.")
    public ApiResponse sendEmail(
            @RequestBody @Valid EmailAddressDto emailDto,
            @CurrentMemberId Long userId
    ) {

        emailService.sendEmail(emailDto.email());

        return ApiResponse.response(OK, EMAIL_SEND_SUCCESS.getMessage());
    }

    @PatchMapping("/code/mail")
    @Operation(summary = "사용자가 입력한 인증 코드를 검증 후 이메일 정보를 업데이트하는 API 입니다.")
    public ApiResponse checkAuthCodeAndUpdateEmail(
            @RequestBody @Valid InactiveMemberAuthCodeRequestDto dto,
            @CurrentMemberId Long userId
    ) {

        emailService.checkEmailAuthCode(dto.email(), dto.authCode());
        memberService.updateMemberEmail(dto.email(), userId);

        return ApiResponse.response(OK, EMAIL_AUTHENTICATION_SUCESS.getMessage());
    }

    @PatchMapping("/agreement")
    @Operation(summary = "약관 동의 정보를 업데이트하는 API 입니다.")
    public ApiResponse<Void> updateUserAgreement(
            @RequestBody MemberAgreementRequestDto dto,
            @CurrentMemberId Long userId
    ){
        memberService.updateMemberAgreement(dto, userId);
        return ApiResponse.response(OK, AGREEEMENT_UPDATE_SUCCESS.getMessage());
    }

    @PatchMapping("/supplement")
    @Operation(summary = "사용자 추가 정보 업데이트하는 API 입니다. (프로필, 닉네임, 실명, 학번, 성별,)")
    public ApiResponse<MemberLoginResponseDto> updateMemberSupplement(
            @RequestBody MemberSupplmentRequestDto dto,
            @CurrentMemberId Long userId,
            HttpServletResponse response
    ){
        MemberResponseDto memberDto = memberService.updateMemberSupplement(dto, userId);
        JwtTokenDto jwtTokenDto = jwtService
                .generateJwtToken(MemberTokenDto.from(memberDto));
        responseToken(jwtTokenDto, response);

        return ApiResponse.response(
                OK,
                SUPPLEMENT_UPDATE_SUCCESS.getMessage(),
                MemberLoginResponseDto.from(memberDto)
        );
    }

    /*
    * refactoring
    * */

    private void responseToken(JwtTokenDto jwtTokenDto, HttpServletResponse response) {
        response.setHeader(ACCESS_TOKEN_SUBJECT, jwtTokenDto.accessToken());
        cookieUtil.setCookie(REFRESH_TOKEN_SUBJECT, jwtTokenDto.refreshToken(), response);
    }
}
