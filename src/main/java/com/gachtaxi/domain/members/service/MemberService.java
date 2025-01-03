package com.gachtaxi.domain.members.service;

import com.gachtaxi.domain.members.dto.request.UserRequestDto;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.exception.DuplicatedStudentNumberException;
import com.gachtaxi.domain.members.repository.MemberRepository;
import com.gachtaxi.global.auth.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    public void saveMember(UserRequestDto.registerDto dto, HttpServletResponse response) {
        validStudentNumber(dto);
        Members newMember = Members.of(dto);
        jwtService.responseJwtToken(newMember.getId(), newMember.getEmail(), newMember.getRole(), response);
        memberRepository.save(newMember);
    }

    public Optional<Members> findByKakaoId(Long kakaoId) {
        return memberRepository.findByKakaoId(kakaoId);
    }

    /*
    * refactor
    * */

    private void validStudentNumber(UserRequestDto.registerDto dto) {
        String studentNumber = dto.studentNumber();
        memberRepository.findByStudentNumber(studentNumber).ifPresent(m -> {
            throw new DuplicatedStudentNumberException();
        });
    }
}
