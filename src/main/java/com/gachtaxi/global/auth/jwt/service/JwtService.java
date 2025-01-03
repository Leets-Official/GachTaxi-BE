package com.gachtaxi.global.auth.jwt.service;

import com.gachtaxi.domain.members.entity.enums.Role;
import com.gachtaxi.global.auth.jwt.dto.JwtTokenDto;
import com.gachtaxi.global.auth.jwt.util.CookieUtil;
import com.gachtaxi.global.auth.jwt.util.JwtExtractor;
import com.gachtaxi.global.auth.jwt.util.JwtProvider;
import com.gachtaxi.global.auth.jwt.util.JwtRedisUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";

    private final CookieUtil cookieUtil;
    private final JwtRedisUtil redisUtil;
    private final JwtProvider jwtProvider;
    private final JwtExtractor jwtExtractor;

    public void responseJwtToken(Long id, String email, Role role, HttpServletResponse response) {
        JwtTokenDto jwtToken = generateJwtToken(id, email, role);
        setHeader(jwtToken.accessToken(), response);
        setCookie(jwtToken.refreshToken(), response);

        log.info(jwtToken.toString());
    }

    // JwtToken 생성 + Redis 저장
    private JwtTokenDto generateJwtToken(Long id, String email, Role role) {
        String accessToken = jwtProvider.generateAccessToken(id, email, role);
        String refreshToken = jwtProvider.generateRefreshToken(id);

        redisUtil.set(id, refreshToken);
        return JwtTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void setHeader(String accessToken, HttpServletResponse response) {
        response.setHeader(ACCESS_TOKEN_SUBJECT, accessToken);
    }

    private void setCookie(String refreshToken, HttpServletResponse response) {
        cookieUtil.setCookie(REFRESH_TOKEN_SUBJECT, refreshToken, response);
    }
}