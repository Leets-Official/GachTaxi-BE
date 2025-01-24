package com.gachtaxi.domain.members.service;

import com.gachtaxi.domain.members.dto.request.InactiveMemberDto;
import com.gachtaxi.domain.members.dto.request.MemberAgreementRequestDto;
import com.gachtaxi.domain.members.dto.request.MemberSupplmentRequestDto;
import com.gachtaxi.domain.members.dto.request.MemberTokenDto;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.exception.DuplicatedNickNameException;
import com.gachtaxi.domain.members.exception.DuplicatedStudentNumberException;
import com.gachtaxi.domain.members.exception.MemberNotFoundException;
import com.gachtaxi.domain.members.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.gachtaxi.domain.members.entity.enums.UserStatus.ACTIVE;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public InactiveMemberDto saveTmpKakaoMember(Long kakaoId){
        Members tmpMember = Members.ofKakaoId(kakaoId);
        memberRepository.save(tmpMember);
        return InactiveMemberDto.of(tmpMember);
    }

    @Transactional
    public InactiveMemberDto saveTmpGoogleMember(String googleId){
        Members tmpMember = Members.ofGoogleId(googleId);
        memberRepository.save(tmpMember);
        return InactiveMemberDto.of(tmpMember);
    }

    @Transactional
    public void updateMemberEmail(String email, Long userId) {
        Members members = findById(userId);
        members.updateEmail(email);
    }

    @Transactional
    public void updateMemberAgreement(MemberAgreementRequestDto dto, Long userId) {
        Members members = findById(userId);
        members.updateAgreement(dto);
    }

    @Transactional
    public MemberTokenDto updateMemberSupplement(MemberSupplmentRequestDto dto, Long userId) {
        checkDuplicatedNickName(dto.nickname());
        checkDuplicatedStudentNumber(dto.studentNumber());

        Members members = findById(userId);
        members.updateSupplment(dto);

        return MemberTokenDto.from(members);
    }

    public Optional<Members> findByKakaoId(Long kakaoId) {return memberRepository.findByKakaoId(kakaoId);}

    public Optional<Members> findByGoogleId(String googleId) {return memberRepository.findByGoogleId(googleId);}

    /*
    * refactor
    * */

    public Members findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    public Members findActiveByEmail(String email) {
        return memberRepository.findByEmailAndStatus(email, ACTIVE)
                .orElseThrow(MemberNotFoundException::new);
    }

    private void checkDuplicatedStudentNumber(Long studentNumber) {
        memberRepository.findByStudentNumber(studentNumber).ifPresent(m -> {
            throw new DuplicatedStudentNumberException();
        });
    }

    private void checkDuplicatedNickName(String nickName) {
        memberRepository.findByNickname(nickName).ifPresent(m -> {
            throw new DuplicatedNickNameException();
        });
    }

}
