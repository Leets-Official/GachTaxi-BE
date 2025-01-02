package com.gachtaxi.global.auth.jwt.util;

import com.gachtaxi.domain.members.entity.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

// 토큰 생성
@Component
public class JwtProvider {

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String ID_CLAIM = "id";
    private static final String EMAIL_CLAIM = "email";
    private static final String ROLE_CLAIM = "role";
    private final Key key;

    public JwtProvider(@Value("${gachtaxi.auth.jwt.key}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes()); // 키 변환
    }

    @Value("${gachtaxi.auth.jwt.accessTokenExpiration}")
    private Long accessTokenExpiration;

    @Value("${gachtaxi.auth.jwt.refreshTokenExpiration}")
    private Long refreshTokenExpiration;

    public String generateAccessToken(Long id, String email, Role role) {
        return Jwts.builder()
                .claim(ID_CLAIM, id)
                .claim(EMAIL_CLAIM, email)
                .claim(ROLE_CLAIM, role)
                .setSubject(ACCESS_TOKEN_SUBJECT) // 사용자 정보(고유 식별자)
                .setIssuedAt(new Date()) // 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration)) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명 알고리즘
                .compact(); // 최종 문자열 생성
    }

    public String generateRefreshToken(Long id) {
        return Jwts.builder()
                .claim(ID_CLAIM, id)
                .setSubject(REFRESH_TOKEN_SUBJECT) // 사용자 정보(고유 식별자)
                .setIssuedAt(new Date()) // 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration)) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명 알고리즘
                .compact(); // 최종 문자열 생성
    }
}
