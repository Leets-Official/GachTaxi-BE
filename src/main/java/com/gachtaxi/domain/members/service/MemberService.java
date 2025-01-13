package com.gachtaxi.domain.members.service;

import com.gachtaxi.domain.members.dto.request.InactiveMemberDto;
import com.gachtaxi.domain.members.dto.request.UserSignUpRequestDto;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.exception.DuplicatedStudentNumberException;
import com.gachtaxi.domain.members.exception.MemberNotFoundException;
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

    //TODO 최종 회원가입 절차에서 사용
    @Transactional
    public void saveMember(UserSignUpRequestDto dto, HttpServletResponse response) {
        checkDuplicatedStudentNumber(dto);
        Members newMember = Members.of(dto);
        memberRepository.save(newMember);
        jwtService.responseJwtToken(newMember.getId(), newMember.getEmail(), newMember.getRole(), response);
    }

    // 임시 유저 저장
    @Transactional
    public InactiveMemberDto saveTmpMember(Long kakaoId){
        Members tmpMember = Members.ofKakaoId(kakaoId);
        memberRepository.save(tmpMember);
        return InactiveMemberDto.of(tmpMember);
    }

    @Transactional
    public void updateInactiveMemberOfEmail(String email, Long userId) {
        Members members = memberRepository.findById(userId)
                .orElseThrow(MemberNotFoundException::new);

        members.updateEmail(email);
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
        return memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
    }
}
