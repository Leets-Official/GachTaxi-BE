package com.gachtaxi.domain.members.service;

import com.gachtaxi.domain.members.dto.request.UserSignUpRequestDto;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.exception.DuplicatedStudentNumberException;
import com.gachtaxi.domain.members.exception.NoSuchMemberException;
import com.gachtaxi.domain.members.repository.MemberRepository;
import com.gachtaxi.global.auth.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveMember(UserSignUpRequestDto dto, HttpServletResponse response) {
        checkDuplicatedStudentNumber(dto);
        Members newMember = Members.of(dto);
        memberRepository.save(newMember);
        jwtService.responseJwtToken(newMember.getId(), newMember.getEmail(), newMember.getRole(), response);
    }

    public Optional<Members> findByKakaoId(Long kakaoId) {
        return memberRepository.findByKakaoId(kakaoId);
    }

    /*
    * refactor
    * */

    private void checkDuplicatedStudentNumber(UserSignUpRequestDto dto) {
        Long studentNumber = dto.studentNumber();
        memberRepository.findByStudentNumber(studentNumber).ifPresent(m -> {
            throw new DuplicatedStudentNumberException();
        });
    }

    public Members findById(Long id) {
        return memberRepository.findById(id).orElseThrow(NoSuchMemberException::new);
    }
}
