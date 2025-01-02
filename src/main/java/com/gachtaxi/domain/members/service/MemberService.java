package com.gachtaxi.domain.members.service;

import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Boolean checkByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public Optional<Members> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

}
