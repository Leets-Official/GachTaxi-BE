package com.gachtaxi.domain.members.service;

import com.gachtaxi.domain.members.dto.request.*;
import com.gachtaxi.domain.members.dto.response.MemberResponseDto;
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

    public MemberResponseDto getMember(Long currentId){
        Members members = findById(currentId);
        return MemberResponseDto.from(members);
    }

    @Transactional
    public MemberResponseDto updateMemberInfo(Long currentId, MemberInfoRequestDto dto){
        Members member = findById(currentId);
        member.updateMemberInfo(dto);

        return MemberResponseDto.from(member);
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
    public MemberResponseDto updateMemberSupplement(MemberSupplmentRequestDto dto, Long userId) {
        checkDuplicatedNickName(dto.nickname());
        checkDuplicatedStudentNumber(dto.studentNumber());

        Members members = findById(userId);
        members.updateSupplment(dto);

        return MemberResponseDto.from(members);
    }

    public Optional<Members> findByKakaoId(Long kakaoId) {return memberRepository.findByKakaoId(kakaoId);}

    public Optional<Members> findByGoogleId(String googleId) {return memberRepository.findByGoogleId(googleId);}

    @Transactional
    public void updateFcmToken(Long userId, FcmTokenRequest request) {
        Members member = findById(userId);

        member.updateToken(request.fcmToken());
    }

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
