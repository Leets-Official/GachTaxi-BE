package com.gachtaxi.domain.members.controller;

import com.gachtaxi.domain.members.service.AuthService;
import com.gachtaxi.global.auth.enums.OauthLoginStatus;
import com.gachtaxi.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.gachtaxi.domain.members.controller.SuccessMessage.*;
import static com.gachtaxi.global.auth.dto.OauthLoginResponse.*;

@Slf4j
@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login/kakao")
    public ApiResponse<kakaoLoginResponse> kakaoLogin(@RequestParam("code") String authcode, HttpServletResponse response) {
        kakaoLoginResponse res = authService.kakaoLogin(authcode, response);
        SuccessMessage OAUTH_STATUS = (res.status() == OauthLoginStatus.LOGIN)
                ? LOGIN_SUCCESS
                : UN_REGISTER;
        return ApiResponse.response(HttpStatus.OK, OAUTH_STATUS.getMessage(), res);
    }
}
