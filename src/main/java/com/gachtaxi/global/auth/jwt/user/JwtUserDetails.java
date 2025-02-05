package com.gachtaxi.global.auth.jwt.user;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.List;

@Getter
public class JwtUserDetails extends User {

    private final Long id;

    public JwtUserDetails(Long id, String email, List<GrantedAuthority> authorities) {
        super(email, "", authorities);
        this.id = id;
    }

    public static JwtUserDetails of(Long id, String email, String role) {
        return new JwtUserDetails(id, email, Collections.singletonList(new SimpleGrantedAuthority(role)));
    }
}