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

    private final static String PREFIX_TOKEN = "refresh_";
    private final static String PREFIX_EMAIL_CODE = "email_";

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${gachtaxi.auth.jwt.refreshTokenExpiration}")
    private Long refreshTokenExpiration;

    @Value("${gachtaxi.auth.redis.emailAuthCodeExpiration}")
    private Long emailAuthCodeExpiration;

    public void setRefreshToken(Long key, String value) {
        redisTemplate.opsForValue().set(PREFIX_TOKEN + key, value, refreshTokenExpiration, TimeUnit.MILLISECONDS);
    }

    public void setEmailAuthCode(String key, String value) {
        redisTemplate.opsForValue().set(PREFIX_EMAIL_CODE+key, value, emailAuthCodeExpiration, TimeUnit.MILLISECONDS);
    }

    public Object getRefreshToken(Long key){
        Object getObjecet = redisTemplate.opsForValue().get(PREFIX_TOKEN +key);
        if(getObjecet == null){
            throw new RefreshTokenNotFoundException();
        }
        return getObjecet;
    }

    public Object getEmailAuthCode(String key){
        Object getObjecet = redisTemplate.opsForValue().get(PREFIX_EMAIL_CODE+key);
        if(getObjecet == null){
            // EmailAuthCode 만료됨
            throw new AuthCodeExpirationException();
        }
        return getObjecet;
    }

    public boolean hasKey(Long key){
        return Boolean.TRUE.equals(redisTemplate.hasKey(PREFIX_TOKEN + key));
    }

    public boolean delete(Long key){
        return Boolean.TRUE.equals(redisTemplate.delete(PREFIX_TOKEN + key));
    }
}
