package com.gachtaxi.global.common.redis;

import com.gachtaxi.global.auth.jwt.exception.RefreshTokenNotFoundException;
import com.gachtaxi.global.common.mail.exception.AuthCodeExpirationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final static String TOKEN_FORMAT = "refreshToken:%s";
    private final static String EMAIL_CODE_FORMAT = "emailAuthCode:%s";

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${gachtaxi.auth.jwt.refreshTokenExpiration}")
    private Long refreshTokenExpiration;

    @Value("${gachtaxi.auth.redis.emailAuthCodeExpiration}")
    private Long emailAuthCodeExpiration;

    public void setRefreshToken(Long id, String value) {
        String key = String.format(TOKEN_FORMAT, id);
        redisTemplate.opsForValue().set(key, value, refreshTokenExpiration, TimeUnit.MILLISECONDS);
    }

    public void setEmailAuthCode(String email, String value) {
        String key = String.format(EMAIL_CODE_FORMAT, email);
        redisTemplate.opsForValue().set(key, value, emailAuthCodeExpiration, TimeUnit.MILLISECONDS);
    }

    public Object getRefreshToken(Long id){
        String key = String.format(TOKEN_FORMAT, id);
        Object getObjecet = redisTemplate.opsForValue().get(key);

        if(getObjecet == null){
            throw new RefreshTokenNotFoundException();
        }

        return getObjecet;
    }

    public Object getEmailAuthCode(String email){
        String key = String.format(EMAIL_CODE_FORMAT, email);
        Object getObjecet = redisTemplate.opsForValue().get(key);

        if(getObjecet == null){
            throw new AuthCodeExpirationException();
        }

        return getObjecet;
    }

    public boolean hasRefreshTokenKey(Long id){
        String key = String.format(TOKEN_FORMAT, id);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public boolean hasAuthCodeKey(Long email){
        String key = String.format(EMAIL_CODE_FORMAT, email);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public boolean deleteRefreshToken(Long id){
        String key = String.format(TOKEN_FORMAT, id);
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    public boolean deleteAuthCode(Long email){
        String key = String.format(EMAIL_CODE_FORMAT, email);
        return Boolean.TRUE.equals(redisTemplate.delete( key));
    }
}
