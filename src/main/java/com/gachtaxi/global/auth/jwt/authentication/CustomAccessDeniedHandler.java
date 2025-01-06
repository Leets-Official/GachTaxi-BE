package com.gachtaxi.global.auth.jwt.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gachtaxi.global.common.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.gachtaxi.global.auth.jwt.exception.JwtErrorMessage.JWT_TOKEN_FORBIDDEN;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final static String LOG_FORMAT = "ExceptionClass: {}, Message: {}";
    private final static String CONTENT_TYPE = "application/json";
    private final static String CHAR_ENCODING = "UTF-8";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, org.springframework.security.access.AccessDeniedException accessDeniedException) throws IOException, ServletException {
        setResponse(response);
        log.error(LOG_FORMAT, accessDeniedException.getClass().getSimpleName(), accessDeniedException.getMessage());
    }

    private void setResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHAR_ENCODING);

        String body = new ObjectMapper().writeValueAsString(ApiResponse.response(HttpStatus.FORBIDDEN, JWT_TOKEN_FORBIDDEN.getMessage()));
        response.getWriter().write(body);
    }
}
