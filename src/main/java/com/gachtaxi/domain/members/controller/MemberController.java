package com.gachtaxi.domain.members.controller;

import com.gachtaxi.domain.members.dto.request.InactiveMemberAuthCodeRequestDto;
import com.gachtaxi.domain.members.dto.request.UserSignUpRequestDto;
import com.gachtaxi.domain.members.dto.response.InactiveMemberResponseDto;
import com.gachtaxi.domain.members.service.MemberService;
import com.gachtaxi.global.auth.jwt.annotation.CurrentMemberId;
import com.gachtaxi.global.common.mail.dto.request.EmailAddressDto;
import com.gachtaxi.global.common.mail.service.EmailService;
import com.gachtaxi.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.gachtaxi.domain.members.controller.ResponseMessage.*;
import static com.gachtaxi.global.common.mail.message.ResponseMessage.EMAIL_AUTHENTICATION_SUCESS;
import static com.gachtaxi.global.common.mail.message.ResponseMessage.EMAIL_SEND_SUCCESS;
import static org.springframework.http.HttpStatus.*;

@RequestMapping("/api/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;

    @PostMapping()
        public ApiResponse<Void> signUp(@RequestBody @Valid UserSignUpRequestDto signUpDto, HttpServletResponse response) {
        memberService.saveMember(signUpDto, response);
        return ApiResponse.response(OK, REGISTER_SUCCESS.getMessage());
    }

    @PostMapping("/mail-authcode")
    public ApiResponse sendEmail(
            @RequestBody @Valid EmailAddressDto emailDto,
            @CurrentMemberId Long userId
    ) {
        emailService.sendEmail(emailDto.email());
        return ApiResponse.response(OK, EMAIL_SEND_SUCCESS.getMessage(), InactiveMemberResponseDto.from(userId));
    }

    @PatchMapping("/mail-authcode")
    public ApiResponse checkAuthCodeAndUpdateEmail(
            @RequestBody @Valid InactiveMemberAuthCodeRequestDto dto,
            @CurrentMemberId Long userId
    ) {
        emailService.checkEmailAuthCode(dto.email(), dto.authCode());
        memberService.updateInactiveMemberOfEmail(dto.email(), userId);
        return ApiResponse.response(OK, EMAIL_AUTHENTICATION_SUCESS.getMessage(), InactiveMemberResponseDto.from(userId));
    }
}
