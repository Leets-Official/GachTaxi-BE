package com.gachtaxi.global.config;

import org.springframework.stereotype.Component;

@Component
public class PermitUrlConfig {

    public String[] getPublicUrl(){
        return new String[]{
                "/auth/login/kakao",
                "/api/members",
                "/login",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/test/login/kakao"
        };
    }

    public String[] getMemberUrl(){
        return new String[]{

        };
    }

    public String[] getAdminUrl(){
        return new String[]{

        };
    }


}
