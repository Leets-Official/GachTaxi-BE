package com.gachtaxi.global.auth.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import static com.gachtaxi.global.auth.jwt.util.JwtProvider.ACCESS_TOKEN_SUBJECT;

// 토큰 추출 및 검증
@Slf4j
@Component
public class JwtExtractor {

    private static final String BEARER = "Bearer ";
    private static final String ID_CLAIM = "id";
    private static final String EMAIL_CLAIM = "email";
    private static final String ROLE_CLAIM = "role";

    private final Key key;

    public JwtExtractor(@Value("${gachtaxi.auth.jwt.key}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes()); // 키 변환
    }

    public Optional<String> extractJwtToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(ACCESS_TOKEN_SUBJECT))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Long getId(String token){
        return getIdFromToken(token, ID_CLAIM);
    }

    public String getEmail(String token){
        return getClaimFromToken(token, EMAIL_CLAIM);
    }

    public String getRole(String token) {
        return getClaimFromToken(token, ROLE_CLAIM);
    }

    public Boolean isExpired(String token) {
        Claims claims = parseClaims(token);
        return claims.getExpiration().before(new Date());
    }

    private String getClaimFromToken(String token, String claimName) {
        Claims claims = parseClaims(token);
        return claims.get(claimName, String.class);
    }

    private Long getIdFromToken(String token, String claimName) {
        Claims claims = parseClaims(token);
        return claims.get(claimName, Long.class);
    }

    private Claims parseClaims(String token) {
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims;
    }

    public boolean validateJwtToken(String token) {
        try {
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build();
            parser.parseClaimsJws(token).getBody();
            return true;
        }catch (JwtException e){
            return false;
        }
    }
}
