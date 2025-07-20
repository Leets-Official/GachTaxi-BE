package com.gachtaxi.domain.members.service;

import com.gachtaxi.domain.chat.repository.ChattingMessageMongoRepository;
import com.gachtaxi.domain.friend.entity.enums.FriendStatus;
import com.gachtaxi.domain.members.dto.request.*;
import com.gachtaxi.domain.members.dto.response.*;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.exception.DuplicatedNickNameException;
import com.gachtaxi.domain.members.exception.DuplicatedStudentNumberException;
import com.gachtaxi.domain.members.exception.InvalidNicknameLengthException;
import com.gachtaxi.domain.members.exception.MemberNotFoundException;
import com.gachtaxi.domain.members.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.gachtaxi.domain.members.entity.enums.UserStatus.ACTIVE;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ChattingMessageMongoRepository chattingMessageMongoRepository;
    public static final String SORT_BY_NICKNAME = "nickname";

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

    public MemberSliceResponse getMemberListByNickName(String nickName, int pageNum, int pageSize){
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.ASC, SORT_BY_NICKNAME));

        Slice<Members> memberSlice = memberRepository.findByNicknameContaining(nickName, pageable);
        List<MemberSummaryResponse> memberList = memberSlice
                .stream()
                .map(MemberSummaryResponse::from)
                .toList();

        return MemberSliceResponse.of(memberList, MemberPageableResponse.from(memberSlice));
    }

    public MemberWithFriendStatusSlice getMemberListValidateFriendShip(String keyword, Long requesterId ,int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.ASC, SORT_BY_NICKNAME));

        Slice<MemberWithFriendRequestProjection> projectionList
                = memberRepository.findMembersWithFriendRequestStatus(keyword, requesterId, pageable);

        List<MemberWithFriendStatusDetailResponse> memberList = projectionList.stream()
                .map(MemberWithFriendStatusDetailResponse::of)
                .toList();

        return MemberWithFriendStatusSlice.of(memberList, MemberPageableResponse.fromProjection(projectionList));
    }

    @Transactional
    public MemberResponseDto updateMemberInfo(Long currentId, MemberInfoRequestDto dto){
        Members member = findById(currentId);
        checkDuplicatedNickName(dto.nickName(), member);

        member.updateMemberInfo(dto);

        chattingMessageMongoRepository.updateMemberInfo(member);

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
        Members members = findTempUserById(userId);
        members.updateEmail(email);
    }

    @Transactional
    public void updateMemberAgreement(MemberAgreementRequestDto dto, Long userId) {
        Members members = findTempUserById(userId);
        members.updateAgreement(dto);
    }

    @Transactional
    public MemberResponseDto updateMemberSupplement(MemberSupplmentRequestDto dto, Long userId) {
        checkInvalidLengthNickName(dto.nickname());
        checkDuplicatedNickName(dto.nickname());
        checkDuplicatedStudentNumber(dto.studentNumber());

        Members members = findTempUserById(userId);
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
        return memberRepository.findByIdAndStatus(id, ACTIVE)
                .orElseThrow(MemberNotFoundException::new);
    }

    public Members findTempUserById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    public Members findActiveByEmail(String email) {
        return memberRepository.findByEmailAndStatus(email, ACTIVE)
                .orElseThrow(MemberNotFoundException::new);
    }

    public Members findByNickname(String nickname) {
        return memberRepository.findByNicknameAndStatus(nickname, ACTIVE)
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

    private void checkDuplicatedNickName(String nickName, Members member) {
        memberRepository.findByNickname(nickName).ifPresent(m -> {
            if (!m.equals(member)) {
                throw new DuplicatedNickNameException();
            }
        });
    }

    private void checkInvalidLengthNickName(String nickName) {
        if (nickName.length() > 10) {
            throw new InvalidNicknameLengthException();
        }
    }

}
