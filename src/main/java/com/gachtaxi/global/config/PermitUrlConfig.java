package com.gachtaxi.global.config;

import org.springframework.stereotype.Component;

@Component
public class PermitUrlConfig {

    public String[] getPublicUrl(){
        return new String[]{
                "/auth/login/kakao",
                "/auth/login/google",
                "/auth/refresh",
                "/api/members",

                "/swagger-ui/**",
                "/v3/api-docs/**",

                "/ws/**"
        };
    }

    public String[] getMemberUrl(){
        return new String[]{
                "/auth/code/**",
                "/api/friends/**"
        };
    }

    public String[] getAdminUrl(){
        return new String[]{
                "/api/admin/email/template",
        };
    }


}
