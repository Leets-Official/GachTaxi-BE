package com.gachtaxi.global.auth.jwt.service;

import com.gachtaxi.domain.members.dto.request.InactiveMemberDto;
import com.gachtaxi.global.auth.jwt.dto.JwtTokenDto;
import com.gachtaxi.global.auth.jwt.exception.CookieNotFoundException;
import com.gachtaxi.global.auth.jwt.exception.TokenExpiredException;
import com.gachtaxi.global.auth.jwt.exception.TokenInvalidException;
import com.gachtaxi.global.auth.jwt.util.CookieUtil;
import com.gachtaxi.global.auth.jwt.util.JwtExtractor;
import com.gachtaxi.global.auth.jwt.util.JwtProvider;
import com.gachtaxi.global.common.redis.RedisUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static com.gachtaxi.global.auth.jwt.util.JwtProvider.REFRESH_TOKEN_SUBJECT;


@Service
@RequiredArgsConstructor
public class JwtService {

    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;
    private final JwtProvider jwtProvider;
    private final JwtExtractor jwtExtractor;

    // JwtToken 생성 + Redis 저장
    public JwtTokenDto generateJwtToken(Long userId, String email, String role) {
        String accessToken = jwtProvider.generateAccessToken(userId, email, role);
        String refreshToken = jwtProvider.generateRefreshToken(userId, email, role);

        redisUtil.setRefreshToken(userId, refreshToken);
        return JwtTokenDto.of(accessToken, refreshToken);
    }

    public JwtTokenDto generateTmpAccessToken(InactiveMemberDto inactiveMemberDto) {
        String tmpAccessToken = jwtProvider.generateTmpAccessToken(inactiveMemberDto.userId(), inactiveMemberDto.role().name());
        return JwtTokenDto.of(tmpAccessToken);
    }

    public JwtTokenDto reissueJwtToken(HttpServletRequest request) {
        String refreshToken = extractRefreshToken(request);
        if(jwtExtractor.isExpired(refreshToken)){
            throw new TokenExpiredException();
        }

        Long userId = jwtExtractor.getId(refreshToken);
        String redisToken = (String) redisUtil.getRefreshToken(userId);
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
}
