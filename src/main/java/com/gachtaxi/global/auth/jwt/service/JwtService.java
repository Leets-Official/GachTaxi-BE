package com.gachtaxi.global.auth.jwt.service;

import com.gachtaxi.domain.members.dto.request.TmpMemberDto;
import com.gachtaxi.domain.members.entity.enums.Role;
import com.gachtaxi.global.auth.jwt.dto.JwtTokenDto;
import com.gachtaxi.global.auth.jwt.exception.CookieNotFoundException;
import com.gachtaxi.global.auth.jwt.exception.TokenExpiredException;
import com.gachtaxi.global.auth.jwt.exception.TokenInvalidException;
import com.gachtaxi.global.auth.jwt.util.CookieUtil;
import com.gachtaxi.global.auth.jwt.util.JwtExtractor;
import com.gachtaxi.global.auth.jwt.util.JwtProvider;
import com.gachtaxi.global.auth.jwt.util.JwtRedisUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;


@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String ACCESS_TOKEN_SUBJECT = "Authorization";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";

    private final CookieUtil cookieUtil;
    private final JwtRedisUtil redisUtil;
    private final JwtProvider jwtProvider;
    private final JwtExtractor jwtExtractor;

    public void responseJwtToken(Long userId, String email, Role role, HttpServletResponse response) {
        JwtTokenDto jwtToken = generateJwtToken(userId, email, role.name());
        setHeader(jwtToken.accessToken(), response);
        setCookie(jwtToken.refreshToken(), response);
    }

    public void responseTmpAccessToken(TmpMemberDto tmpMemberDto, HttpServletResponse response) {
        String tmpAccessToken = jwtProvider.generateTmpAccessToken(tmpMemberDto.userId(), tmpMemberDto.email(), tmpMemberDto.role().name());
        setHeader(tmpAccessToken, response);
    }

    public JwtTokenDto reissueJwtToken(HttpServletRequest request) {
        String refreshToken = extractRefreshToken(request);
        if(jwtExtractor.isExpired(refreshToken)){
            throw new TokenExpiredException();
        }
        Long userId = jwtExtractor.getId(refreshToken);

        String redisToken = (String) redisUtil.get(userId);
        if(!redisToken.equals(refreshToken)) {
            throw new TokenInvalidException();
        }

        String email = jwtExtractor.getEmail(refreshToken);
        String role = jwtExtractor.getRole(refreshToken);
        return generateJwtToken(userId, email, role);
    }

    /*
    * refactoring
    * */

    private String extractRefreshToken(HttpServletRequest request){
        Cookie[] cookies = getCookie(request);

        return Arrays.stream(cookies)
                .filter(cookie -> REFRESH_TOKEN_SUBJECT.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(TokenInvalidException::new);
    }

    private static Cookie[] getCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new CookieNotFoundException();
        }
        return cookies;
    }

    // JwtToken 생성 + Redis 저장
    private JwtTokenDto generateJwtToken(Long userId, String email, String role) {
        String accessToken = jwtProvider.generateAccessToken(userId, email, role);
        String refreshToken = jwtProvider.generateRefreshToken(userId, email, role);

        redisUtil.set(userId, refreshToken);
        return JwtTokenDto.of(accessToken, refreshToken);
    }

    public void setHeader(String accessToken, HttpServletResponse response) {
        response.setHeader(ACCESS_TOKEN_SUBJECT, accessToken);
    }

    public void setCookie(String refreshToken, HttpServletResponse response) {
        cookieUtil.setCookie(REFRESH_TOKEN_SUBJECT, refreshToken, response);
    }
}
