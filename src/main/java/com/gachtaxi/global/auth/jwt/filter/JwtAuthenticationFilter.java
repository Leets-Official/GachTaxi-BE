package com.gachtaxi.global.auth.jwt.filter;

import com.gachtaxi.global.auth.jwt.user.JwtUserDetailsService;
import com.gachtaxi.global.auth.jwt.util.JwtExtractor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static com.gachtaxi.global.auth.jwt.exception.JwtErrorMessage.JWT_TOKEN_EXPIRED;
import static com.gachtaxi.global.auth.jwt.exception.JwtErrorMessage.JWT_TOKEN_NOT_FOUND;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtExtractor jwtExtractor;
    private final JwtUserDetailsService jwtUserDetailsService;

    private final static String JWT_ERROR = "jwtError";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> token = jwtExtractor.extractJwtToken(request);

        if (token.isEmpty()) {
            request.setAttribute(JWT_ERROR, JWT_TOKEN_NOT_FOUND);
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = token.get();

        if(jwtExtractor.isExpired(accessToken)){
            request.setAttribute(JWT_ERROR, JWT_TOKEN_EXPIRED);
            filterChain.doFilter(request, response);
            return;
        }

        saveAuthentcation(accessToken);
        filterChain.doFilter(request, response);
    }

    private void saveAuthentcation(String token) {
        String email = jwtExtractor.getEmail(token);

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
