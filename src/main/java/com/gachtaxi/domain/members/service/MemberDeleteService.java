package com.gachtaxi.domain.members.service;

import com.gachtaxi.domain.chat.repository.ChattingParticipantRepository;
import com.gachtaxi.domain.friend.repository.FriendRepository;
import com.gachtaxi.domain.matching.common.repository.MemberMatchingRoomChargingInfoRepository;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.exception.MemberNotFoundException;
import com.gachtaxi.domain.members.repository.MemberRepository;
import com.gachtaxi.domain.notification.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDeleteService {

    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final ChattingParticipantRepository chattingParticipantRepository;
    private final MemberMatchingRoomChargingInfoRepository memberMatchingRoomChargingInfoRepository;
    private final NotificationRepository notificationRepository;

    /*
    todo 채팅 메시지 수정 PR 머지 되면 (알수없음)으로 바꾸고 프사 삭제하기
     */

    // 하드 딜리트
//    @Transactional
//    public void delete(Long memberId) {
//        Members member = memberRepository.findById(memberId)
//                .orElseThrow(MemberNotFoundException::new);
//
//        chattingParticipantRepository.deleteAllByMembers(member);
//        friendRepository.deleteBySenderOrReceiver(member, member);
//        memberMatchingRoomChargingInfoRepository.deleteAllByMembers(member);
//
//        notificationRepository.deleteByReceiverId(memberId);
//        memberRepository.deleteById(memberId);
//    }

    // 소프트 딜리트
    @Transactional
    public void softDelete(Long memberId) {
        Members member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        member.delete();
    }
}
