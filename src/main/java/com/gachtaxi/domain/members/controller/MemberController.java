package com.gachtaxi.domain.members.controller;

import com.gachtaxi.domain.members.dto.request.FcmTokenRequest;
import com.gachtaxi.domain.members.service.MemberService;
import com.gachtaxi.global.auth.jwt.annotation.CurrentMemberId;
import com.gachtaxi.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gachtaxi.domain.members.controller.ResponseMessage.FCM_TOKEN_UPDATE_SUCCESS;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "MEMBER")
@RequestMapping("/api/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/firebase")
    @Operation(summary = "fcm 토큰을 저장하기 위한 API입니다. 매 로그인 혹은 토큰 리프레시가 발생할 때 저장해주세요")
    public ApiResponse<Void> save(@CurrentMemberId Long memberId,
                                  @RequestBody @Valid FcmTokenRequest request) {
        memberService.updateFcmToken(memberId, request);

        return ApiResponse.response(OK, FCM_TOKEN_UPDATE_SUCCESS.getMessage());
    }
}
