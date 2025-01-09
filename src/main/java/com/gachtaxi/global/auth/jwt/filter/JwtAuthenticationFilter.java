package com.gachtaxi.global.auth.jwt.filter;

import com.gachtaxi.global.auth.jwt.user.JwtUserDetails;
import com.gachtaxi.global.auth.jwt.util.JwtExtractor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static com.gachtaxi.global.auth.jwt.exception.JwtErrorMessage.*;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtExtractor jwtExtractor;

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

        if (!jwtExtractor.validateJwtToken(accessToken)) {
            request.setAttribute(JWT_ERROR, JWT_TOKEN_INVALID);
            filterChain.doFilter(request, response);
            return;
        }

        if (jwtExtractor.isExpired(accessToken)) {
            request.setAttribute(JWT_ERROR, JWT_TOKEN_EXPIRED);
            filterChain.doFilter(request, response);
            return;
        }

        saveAuthentcation(accessToken);
        filterChain.doFilter(request, response);
    }

    private void saveAuthentcation(String token) {
        Long id = jwtExtractor.getId(token);
        String email = jwtExtractor.getEmail(token);
        String role = jwtExtractor.getRole(token);

        UserDetails userDetails = JwtUserDetails.of(id, email, role);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

//    private boolean checkValidToken(String token) {
//        try {
//        JwtParser parser = jwtExtractor.getParser();
//        parser.parseClaimsJws(token).getBody();
//        }catch (JwtException e){
//            log.error(e.getMessage());
//            return false;
//        }
//        return true;
//    }
}
