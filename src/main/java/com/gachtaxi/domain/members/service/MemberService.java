package com.gachtaxi.domain.members.service;

import com.gachtaxi.domain.members.dto.request.InactiveMemberDto;
import com.gachtaxi.domain.members.dto.request.MemberAgreementRequestDto;
import com.gachtaxi.domain.members.dto.request.MemberSupplmentRequestDto;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.exception.DuplicatedStudentNumberException;
import com.gachtaxi.domain.members.exception.MemberNotFoundException;
import com.gachtaxi.domain.members.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 임시 유저 저장
    @Transactional
    public InactiveMemberDto saveTmpMember(Long kakaoId){
        Members tmpMember = Members.ofKakaoId(kakaoId);
        memberRepository.save(tmpMember);
        return InactiveMemberDto.of(tmpMember);
    }

    @Transactional
    public void updateMemberEmail(String email, Long userId) {
        Members members = memberRepository.findById(userId)
                .orElseThrow(MemberNotFoundException::new);

        members.updateEmail(email);
    }

    @Transactional
    public void updateMemberAgreement(MemberAgreementRequestDto dto, Long userId) {
        Members members = memberRepository.findById(userId)
                .orElseThrow(MemberNotFoundException::new);

        members.updateAgreement(dto);
    }

    @Transactional
    public void updateMemberSupplement(MemberSupplmentRequestDto dto, Long userId) {
        Members members = memberRepository.findById(userId)
                .orElseThrow(MemberNotFoundException::new);

        members.updateSupplment(dto);
    }

    public Optional<Members> findByKakaoId(Long kakaoId) {
        return memberRepository.findByKakaoId(kakaoId);
    }

    /*
    * refactor
    * */

    private void checkDuplicatedStudentNumber(Long studentNumber) {
        memberRepository.findByStudentNumber(studentNumber).ifPresent(m -> {
            throw new DuplicatedStudentNumberException();
        });
    }
}
