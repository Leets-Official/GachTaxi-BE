package com.gachtaxi.domain.matching.common.service;

import static com.gachtaxi.domain.notification.entity.enums.NotificationType.MATCH_INVITE;

import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import com.gachtaxi.domain.matching.common.exception.NoSuchInvitationException;
import com.gachtaxi.domain.matching.common.exception.NoSuchMatchingRoomException;
import com.gachtaxi.domain.matching.common.repository.MatchingRoomRepository;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.repository.MemberRepository;
import com.gachtaxi.domain.members.service.MemberService;
import com.gachtaxi.domain.notification.entity.payload.MatchingInvitePayload;
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

    /*
      수동 매칭시 친구 초대
    */
    public static final String MATCHING_INVITE_TITLE = "수동 매칭 초대";
    public static final String MATCHING_INVITE_CONTENT = "%s 님이 수동 매칭 초대를 보냈습니다.";

    public void sendMatchingInvitation(Members sender, List<String> friendNicknames) {
        if (friendNicknames == null || friendNicknames.isEmpty()) {
            return;
        }

        List<Members> friends = memberRepository.findByNicknameIn(friendNicknames);

        for (Members friend : friends) {
            notificationService.sendWithPush(
                    sender.getId(),
                    friend,
                    MATCH_INVITE,
                    MATCHING_INVITE_TITLE,
                    String.format(MATCHING_INVITE_CONTENT, sender.getNickname()),
                    MatchingInvitePayload.from(sender.getNickname())
            );
        }
    }

    /*
      수동 매칭시 친구 초대 수락
    */
    @Transactional
    public void acceptInvitation(Long userId, Long matchingRoomId) {
        Members member = memberService.findById(userId);
        MatchingRoom matchingRoom = matchingRoomRepository.findById(matchingRoomId)
                .orElseThrow(NoSuchMatchingRoomException::new);

        if (!notificationService.hasReceivedMatchingInvite(userId)) {
            throw new NoSuchInvitationException();
        }

        matchingRoom.addMember(member);
        matchingRoomRepository.save(matchingRoom);
    }
}
