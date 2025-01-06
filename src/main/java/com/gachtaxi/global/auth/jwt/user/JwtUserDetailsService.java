package com.gachtaxi.global.auth.jwt.user;

import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.repository.MemberRepository;
import com.gachtaxi.global.auth.jwt.exception.UserEmailNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Members member = memberRepository.findByEmail(email)
                .orElseThrow(UserEmailNotFoundException::new);

        return JwtUserDetails.of(member);
    }
}
