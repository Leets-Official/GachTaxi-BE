package com.gachtaxi.domain.members.controller;

import com.gachtaxi.domain.members.service.MemberService;
import com.gachtaxi.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gachtaxi.domain.members.dto.request.UserRequestDto.*;
import static com.gachtaxi.domain.members.controller.SuccessMessage.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RequestMapping("/api/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping()
        public ApiResponse<Void> signUp(@RequestBody @Valid SignUpDto signUpDto, HttpServletResponse response) {
        memberService.saveMember(signUpDto, response);
        return ApiResponse.response(OK, REGISTER_SUCCESS.getMessage());
    }
}
