package com.gachtaxi.domain.members.service;

import com.gachtaxi.domain.members.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Boolean checkByEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

}
