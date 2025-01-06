package com.gachtaxi.global.auth.jwt.user;

import com.gachtaxi.domain.members.entity.Members;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.List;

@Getter
public class JwtUserDetails extends User {

    private final Members member;
    private final Long id;
    private final String email;

    public JwtUserDetails(Members member, Long id, String email, List<GrantedAuthority> authorities) {
        super(email, "", authorities);
        this.member = member;
        this.id = id;
        this.email = email;
    }

    public static JwtUserDetails of(Members member) {
        return new JwtUserDetails(member, member.getId(), member.getEmail(), Collections.singletonList(new SimpleGrantedAuthority(member.getRole().toString())));
    }
}