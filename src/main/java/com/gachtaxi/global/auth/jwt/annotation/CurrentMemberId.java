package com.gachtaxi.global.auth.jwt.annotation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? T(com.gachtaxi.domain.members.exception.MemberIdNotFoundException).throwException() : id")
public @interface CurrentMemberId {
    /*
     * AuthenticationPrincipal의 id 필드를 반환
     * 즉, JwtUserDetails의 id 필드를 반환
     * JwtUserDetails의 id는 Userid
     * */
}
