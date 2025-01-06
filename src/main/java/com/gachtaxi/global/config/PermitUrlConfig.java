package com.gachtaxi.global.config;

import org.springframework.stereotype.Component;

@Component
public class PermitUrlConfig {

    public String[] getPublicUrl(){
        String[] publicUrls = {
                "/auth/login/kakao",
                "/login",
                "/uri/test",
                "/swagger-ui/**",
                "/v3/api-docs/**"
        };
        return publicUrls;
    }

    public String[] getMemberUrl(){
        String[] memberUrls = {

        };
        return memberUrls;
    }

    public String[] getAdminUrl(){
        String[] adminUrls = {

        };
        return adminUrls;
    }


}
