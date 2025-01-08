package com.gachtaxi.domain.members.controller;

import com.gachtaxi.domain.members.dto.request.InactiveMemberAuthCodeRequestDto;
import com.gachtaxi.domain.members.dto.response.InactiveMemberResponseDto;
import com.gachtaxi.domain.members.service.MemberService;
import com.gachtaxi.global.auth.jwt.annotation.CurrentMemberId;
import com.gachtaxi.global.common.mail.dto.request.EmailAddressDto;
import com.gachtaxi.global.common.mail.service.EmailService;
import com.gachtaxi.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.gachtaxi.global.common.mail.message.ResponseMessage.EMAIL_AUTHENTICATION_SUCESS;
import static com.gachtaxi.global.common.mail.message.ResponseMessage.EMAIL_SEND_SUCCESS;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class InactiveMemberController {

    private final EmailService emailService;
    private final MemberService memberService;

    @PostMapping("/tmp-members/mail-authcode")
    public ApiResponse sendEmail(
            @RequestBody @Valid EmailAddressDto emailDto,
            @CurrentMemberId Long userId
    ) {
        emailService.sendEmail(emailDto.email());
        return ApiResponse.response(OK, EMAIL_SEND_SUCCESS.getMessage(), InactiveMemberResponseDto.from(userId));
    }

    @PatchMapping("/tmp-members/mail-authcode")
    public ApiResponse checkAuthCodeAndUpdateEmail(
            @RequestBody @Valid InactiveMemberAuthCodeRequestDto dto,
            @CurrentMemberId Long userId
    ) {
        emailService.checkEmailAuthCode(dto.email(), dto.authCode());
        memberService.updateInactiveMemberOfEmail(dto.email(), userId);
        return ApiResponse.response(OK, EMAIL_AUTHENTICATION_SUCESS.getMessage(), InactiveMemberResponseDto.from(userId));
    }
}
