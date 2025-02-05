package com.gachtaxi.global.auth.google.utils;

import com.gachtaxi.global.auth.google.dto.GoogleTokenResponse;
import com.gachtaxi.global.auth.google.dto.GoogleUserInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component
public class GoogleUtils {

    @Value("${gachtaxi.auth.google.client}")
    private String googleClient;

    @Value("${gachtaxi.auth.google.client-secret}")
    private String clientSecret;

    @Value("${gachtaxi.auth.google.redirect}")
    private String googleRedirect;

    @Value("${gachtaxi.auth.google.token_uri}")
    private String GoogleTokenUri;

    @Value("${gachtaxi.auth.google.user_profile}")
    private String GoogleProfileUri;

    private final RestClient restClient = RestClient.create();

    public GoogleTokenResponse reqeustGoogleToken(String authCode){
        String decodeCode = URLDecoder.decode(authCode, StandardCharsets.UTF_8);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", googleClient);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", googleRedirect);
        params.add("code", decodeCode);

        return restClient.post()
                .uri(GoogleTokenUri)
                .body(params)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .body(GoogleTokenResponse.class);
    }

    public GoogleUserInfoResponse requestGoogleProfile(String Token){
        return restClient.get()
                .uri(GoogleProfileUri)
                .header("Authorization", "Bearer " + Token)
                .retrieve()
                .body(GoogleUserInfoResponse.class);
    }
}
