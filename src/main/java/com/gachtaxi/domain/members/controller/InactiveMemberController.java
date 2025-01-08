package com.gachtaxi.domain.members.controller;

import com.gachtaxi.domain.members.dto.response.InactiveMemberResponseDto;
import com.gachtaxi.global.auth.jwt.annotation.CurrentMemberId;
import com.gachtaxi.global.common.mail.dto.request.EmailAddressDto;
import com.gachtaxi.global.common.mail.service.EmailService;
import com.gachtaxi.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gachtaxi.domain.members.controller.ResponseMessage.EMAIL_SEND_SUCCESS;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class InactiveMemberController {

    private final EmailService emailService;

    @PostMapping("/tmp-members/mail-authcode")
    public ApiResponse sendEmail(
            @RequestBody @Valid EmailAddressDto emailDto,
            @CurrentMemberId Long userId
    ) {
        emailService.sendEmail(emailDto.email());
        return ApiResponse.response(OK, EMAIL_SEND_SUCCESS.getMessage(), InactiveMemberResponseDto.from(userId));
    }
}
