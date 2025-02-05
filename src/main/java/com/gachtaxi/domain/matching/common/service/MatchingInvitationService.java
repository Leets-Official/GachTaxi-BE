package com.gachtaxi.domain.matching.common.service;

import static com.gachtaxi.domain.notification.entity.enums.NotificationType.MATCH_INVITE;
import com.gachtaxi.domain.matching.common.dto.request.ManualMatchingInviteReplyRequest;
import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import com.gachtaxi.domain.matching.common.entity.MemberMatchingRoomChargingInfo;
import com.gachtaxi.domain.matching.common.entity.enums.MatchingInviteStatus;
import com.gachtaxi.domain.matching.common.exception.AlreadyInMatchingRoomException;
import com.gachtaxi.domain.matching.common.exception.MatchingRoomAlreadyFullException;
import com.gachtaxi.domain.matching.common.exception.NoSuchInvitationException;
import com.gachtaxi.domain.matching.common.exception.NoSuchMatchingRoomException;
import com.gachtaxi.domain.matching.common.repository.MatchingRoomRepository;
import com.gachtaxi.domain.matching.common.repository.MemberMatchingRoomChargingInfoRepository;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.repository.MemberRepository;
import com.gachtaxi.domain.members.service.MemberService;
import com.gachtaxi.domain.notification.entity.Notification;
import com.gachtaxi.domain.notification.entity.enums.NotificationType;
import com.gachtaxi.domain.notification.entity.payload.MatchingInvitePayload;
import com.gachtaxi.domain.notification.repository.NotificationRepository;
import com.gachtaxi.domain.notification.service.NotificationService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingInvitationService {
    private final NotificationService notificationService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final MatchingRoomRepository matchingRoomRepository;
    private final MemberMatchingRoomChargingInfoRepository memberMatchingRoomChargingInfoRepository;
    private final NotificationRepository notificationRepository;

    /*
      수동 매칭시 친구 초대
    */
    public static final String MATCHING_INVITE_TITLE = "수동 매칭 초대";
    public static final String MATCHING_INVITE_CONTENT = "%s 님이 수동 매칭 초대를 보냈습니다.";

    public void sendMatchingInvitation(Members sender, List<Long> friendIds, Long matchingRoomId) {
        if (friendIds == null || friendIds.isEmpty()) {
            return;
        }

        List<Members> friends = memberRepository.findByIdIn(friendIds);

        for (Members friend : friends) {
            notificationService.sendWithPush(
                    friend,
                    MATCH_INVITE,
                    MATCHING_INVITE_TITLE,
                    String.format(MATCHING_INVITE_CONTENT, sender.getNickname()),
                    MatchingInvitePayload.from(sender.getNickname(), matchingRoomId)
            );
        }
    }

    /*
      수동 매칭시 친구 초대 수락
    */
    @Transactional
    public void acceptInvitation(Long userId, ManualMatchingInviteReplyRequest request) {
        Members member = memberService.findById(userId);
        MatchingRoom matchingRoom = matchingRoomRepository.findById(request.matchingRoomId())
                .orElseThrow(NoSuchMatchingRoomException::new);

        Notification notification = notificationService.find(request.notificationId());

        MatchingInvitePayload payload = (MatchingInvitePayload) notification.getPayload();
        if (!payload.getMatchingRoomId().equals(request.matchingRoomId())) {
            throw new NoSuchInvitationException();
        }

        if (request.status() == MatchingInviteStatus.REJECT) {
            notificationRepository.delete(notification);
            return;
        }

        notificationRepository.save(notification);

        if (notificationRepository.countByReceiverIdAndType(userId, NotificationType.MATCH_INVITE) == 0) {
            throw new NoSuchInvitationException();
        }

        if (matchingRoom.getCurrentMemberCount() >= matchingRoom.getCapacity()) {
            throw new MatchingRoomAlreadyFullException();
        }

        if (matchingRoomRepository.existsByMemberInMatchingRoom(member)) {
            throw new AlreadyInMatchingRoomException();
        }

        MemberMatchingRoomChargingInfo memberInfo = MemberMatchingRoomChargingInfo.notPayedOf(matchingRoom, member);
        memberMatchingRoomChargingInfoRepository.save(memberInfo);

        notificationRepository.delete(notification);
    }
}
