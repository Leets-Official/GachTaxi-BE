package com.gachtaxi.global.auth.jwt.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JwtRedisUtil {

    private final static String PREFIX = "refresh_";

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${gachtaxi.auth.jwt.refreshTokenExpiration}")
    private Long refreshTokenExpiration;

    public void set(Long key, String value) {
        redisTemplate.opsForValue().set(PREFIX + key, value, refreshTokenExpiration, TimeUnit.MILLISECONDS);
    }

    public Object get(Long key){
        return redisTemplate.opsForValue().get(PREFIX +key);
    }

    public boolean hasKey(Long key){
        return Boolean.TRUE.equals(redisTemplate.hasKey(PREFIX + key));
    }

    public boolean delete(Long key){
        return Boolean.TRUE.equals(redisTemplate.delete(PREFIX + key));
    }
}
