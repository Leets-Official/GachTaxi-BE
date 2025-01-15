package com.gachtaxi.domain.members.service;

import com.gachtaxi.domain.members.dto.request.*;
import com.gachtaxi.domain.members.dto.response.MemberMailResponseDto;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.exception.DuplicatedEmailException;
import com.gachtaxi.domain.members.exception.DuplicatedNickNameException;
import com.gachtaxi.domain.members.exception.DuplicatedStudentNumberException;
import com.gachtaxi.domain.members.exception.MemberNotFoundException;
import com.gachtaxi.domain.members.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.gachtaxi.domain.members.entity.enums.UserStatus.ACTIVE;
import static com.gachtaxi.global.common.mail.dto.enums.EmailStatus.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberMailResponseDto IsAlreadySignEmail(String email, Long tmpId) {
        Optional<Members> findMembers = memberRepository.findByEmailAndStatus(email, ACTIVE);
        if(findMembers.isPresent()) { // 이미 가입되어 있는 회원 가입 한 유저면
            Members members = findMembers.get(); // 이미 가입되어있는 멤버
            Members tmpMembers = findById(tmpId);
            //만약 카카오 Id가 있다면 KAKAO_INTEGRATE
            if (members.getKakaoId() != null) {
                if(tmpMembers.getGoogleId() != null) {
                    return new MemberMailResponseDto(KAKAO_INTEGRATE, email, null, tmpMembers.getGoogleId());
                }else{
                    // 임시 유저 구글 ID도 null이다? -> 회원가입한 카카오 계정이 아닌 다른 카카오 계정을 로그인 해
                    // 다른 카카오 계정으로 로그인하고 같은 학교 이메일을 사용한 경우
                    throw new DuplicatedEmailException();
                }
            }
            if (members.getGoogleId() != null){ // 구글 Id가 있다는 의미므로 GOOGLE_INTEGRATE
                if(tmpMembers.getKakaoId() != null) {
                    return new MemberMailResponseDto(GOOGLE_INTEGRATE, email, tmpMembers.getKakaoId(), null);
                }else{
                    // 임시 유저 카카오ID도 null -> 회원가입한 구글 계정이 아닌 다른 구글 계정으로 로그인한 경우
                    // 다른 구글 계정으로 로그인하고 같은 학교 이메일을 사용한 경우
                    throw new DuplicatedEmailException();
                }
            }
        }
        return new MemberMailResponseDto(MAIL_SUCCESS, email, null, null);
    }

    @Transactional
    public MemberTokenDto IntegrationMemberToKakao(MemberIntegrationRequestDto dto, Long tmpId) {
        Members existsMembers = findActiveByEmail(dto.email());
        memberRepository.deleteById(tmpId);
        memberRepository.flush();

        existsMembers.updateGoogleId(dto.googleId());
        return MemberTokenDto.from(existsMembers);
    }

    @Transactional
    public MemberTokenDto IntegrationMemberToGoogle(MemberIntegrationRequestDto dto, Long tmpId) {
        Members existsMembers = findActiveByEmail(dto.email());
        memberRepository.deleteById(tmpId);
        memberRepository.flush();

        existsMembers.updateKakaoId(dto.kakaoId());
        return MemberTokenDto.from(existsMembers);
    }

    // 임시 유저 저장
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
