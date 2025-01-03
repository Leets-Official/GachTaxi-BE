package com.gachtaxi.global.auth.jwt.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    @Value("${gachtaxi.auth.jwt.cookieMaxAge}")
    private Long cookieMaxAge;

    @Value("${gachtaxi.auth.jwt.secureOption}")
    private boolean secureOption;

    public void setCookie( String name, String value, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .maxAge(cookieMaxAge)
                .path("/")
                .secure(secureOption) //https 적용 시 true
                .httpOnly(true)
                .sameSite("None")
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
    }

    public void deleteCookie(HttpServletResponse response, String name) {
        ResponseCookie cookie = ResponseCookie.from(name, "value")
                .maxAge(0)
                .path("/")
                .secure(false)
                .httpOnly(true)
                .sameSite("None")
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
    }
}
