package com.gachtaxi.global.config;

import org.springframework.stereotype.Component;

@Component
public class PermitUrlConfig {

    public String[] getPublicUrl(){
        return new String[]{
                "/auth/login/kakao",
                "/auth/refersh",
                "/api/members/signup",

                "/swagger-ui/**",
                "/v3/api-docs/**",
        };
    }

    public String[] getMemberUrl(){
        return new String[]{
                "/api/tmp-members/**",
                "/api/admin/email/template",
        };
    }

    public String[] getAdminUrl(){
        return new String[]{

        };
    }


}
