package com.gachtaxi.domain.friend.service;

import com.gachtaxi.domain.friend.dto.request.FriendRequestDto;
import com.gachtaxi.domain.friend.dto.response.FriendsResponseDto;
import com.gachtaxi.domain.friend.entity.Friends;
import com.gachtaxi.domain.friend.entity.enums.FriendStatus;
import com.gachtaxi.domain.friend.exception.FriendNotExistsException;
import com.gachtaxi.domain.friend.exception.FriendShipDoesNotSendMySelfException;
import com.gachtaxi.domain.friend.exception.FriendShipExistsException;
import com.gachtaxi.domain.friend.exception.FriendShipPendingException;
import com.gachtaxi.domain.friend.mapper.FriendsMapper;
import com.gachtaxi.domain.friend.repository.FriendRepository;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.service.MemberService;
import com.gachtaxi.domain.notification.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gachtaxi.domain.notification.entity.enums.NotificationType.FRIEND_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final NotificationService notificationService;
    private final MemberService memberService;

    public static final String FRIEND_REQUEST_MESSAGE = "%s 님이 친구 요청을 보냈어요.";

    @Transactional
    public void sendFriendRequest(Long senderId, FriendRequestDto dto) {
        Members sender = memberService.findById(senderId);
        Members receiver = memberService.findById(dto.memberId());

        checkDuplicatedFriendShip(senderId, receiver.getId());
        friendRepository.save(Friends.of(sender, receiver));

        notificationService.sendWithPush(
                sender.getId(),
                receiver,
                FRIEND_REQUEST,
                String.format(FRIEND_REQUEST_MESSAGE, sender.getNickname()));
    }

    public List<FriendsResponseDto> getFriendsList(Long memberId){
        List<Friends> friendsList = getAcceptedFriendsList(memberId);

        return friendsList.stream()
                .map(friends -> FriendsMapper.toResponseDto(friends, memberId))
                .toList();
    }

    @Transactional
    public void updateFriendStatus(Long senderId, Long receiverId) {
        Friends friendShip = findBySenderIdAndReceiverId(senderId, receiverId);
        friendShip.updateStatus();
    }

    public void deleteFriend(Long currentId, Long memberId) {
        Friends friendShip = getFriendShip(currentId, memberId);
        friendRepository.delete(friendShip);
    }

    /*
    * refactoring
    * */

    public void checkDuplicatedFriendShip(Long senderId, Long receiverId) {
        if(senderId.equals(receiverId)) { // 자기 자신한테 친구 요청을 보낼 경우
            throw new FriendShipDoesNotSendMySelfException();
        }

        friendRepository.findFriendShip(senderId, receiverId)
                .ifPresent(f -> {
                    if(f.getStatus() == FriendStatus.ACCEPTED) { // 이미 친구 관계인 경우
                        throw new FriendShipExistsException();
                    }
                    if(f.getStatus() == FriendStatus.PENDING) { // 친구 요청 대기중인 경우
                        throw new FriendShipPendingException();
                    }
                });
    }

    public List<Friends> getAcceptedFriendsList(Long memberId) {
        return friendRepository.findAcceptedFriendsByMemberId(memberId);
    }

    // A와 B 중 누가 sender이고 receiver인지 정확히 아는 경우 (ex Notification에 저장된 친구 요청)
    public Friends findBySenderIdAndReceiverId(Long senderId, Long receiverId) {
        return friendRepository.findBySenderIdAndReceiverId(senderId, receiverId)
                .orElseThrow(FriendNotExistsException::new);
    }

    // A와 B 중 누가 sender이고 receiver인지 모르는 경우 (ex A와B가 친구인 지 확인할 때)
    public Friends getFriendShip(Long senderId, Long receiverId) {
        return friendRepository.findFriendShip(senderId, receiverId)
                .orElseThrow(FriendNotExistsException::new);
    }
}
