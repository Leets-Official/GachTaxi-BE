package com.gachtaxi.domain.chat.service;

import com.gachtaxi.domain.chat.dto.request.ChatMessage;
import com.gachtaxi.domain.chat.dto.request.ChatMessageRequest;
import com.gachtaxi.domain.chat.dto.response.ChatPageableResponse;
import com.gachtaxi.domain.chat.dto.response.ChatResponse;
import com.gachtaxi.domain.chat.dto.response.ChattingMessageResponse;
import com.gachtaxi.domain.chat.entity.ChattingMessage;
import com.gachtaxi.domain.chat.entity.ChattingParticipant;
import com.gachtaxi.domain.chat.entity.ChattingRoom;
import com.gachtaxi.domain.chat.exception.WebSocketSessionException;
import com.gachtaxi.domain.chat.redis.RedisChatPublisher;
import com.gachtaxi.domain.chat.repository.ChattingMessageRepository;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.gachtaxi.domain.chat.stomp.strategy.StompConnectStrategy.CHAT_USER_ID;
import static com.gachtaxi.domain.chat.stomp.strategy.StompSubscribeStrategy.CHAT_ROOM_ID;
import static com.gachtaxi.domain.chat.stomp.strategy.StompSubscribeStrategy.CHAT_USER_NAME;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final ChattingMessageRepository chattingMessageRepository;
    private final RedisChatPublisher redisChatPublisher;
    private final ChattingRoomService chattingRoomService;
    private final ChattingParticipantService chattingParticipantService;
    private final MemberService memberService;
    private final ChattingRedisService chattingRedisService;

    @Value("${chat.topic}")
    public String chatTopic;

    @Transactional
    public void chat(ChatMessageRequest request, SimpMessageHeaderAccessor accessor) {
        long roomId = getSessionAttribute(accessor, CHAT_ROOM_ID, Long.class);
        long userId = getSessionAttribute(accessor, CHAT_USER_ID, Long.class);
        String senderName = getSessionAttribute(accessor, CHAT_USER_NAME, String.class);

        long unreadCount = getUnreadCount(roomId);
        String profilePicture = chattingRedisService.getProfilePicture(roomId, userId);

        ChattingMessage chattingMessage = ChattingMessage.of(request, roomId, userId, senderName, unreadCount, profilePicture);

        chattingMessageRepository.save(chattingMessage);

        ChannelTopic topic = new ChannelTopic(chatTopic + roomId);
        ChatMessage chatMessage = ChatMessage.from(chattingMessage);

        redisChatPublisher.publish(topic, chatMessage);
        /*
        todo 채팅에 알림이 도입되면 redis에 참여하지 않은 사람 리스트를 가져와서 푸시알림 보내기. 참여하고 있다면 X
         */
    }

    public ChatResponse getMessage(long roomId, long senderId, int pageNumber, int pageSize, LocalDateTime lastMessageTimeStamp) {
        ChattingRoom chattingRoom = chattingRoomService.find(roomId);
        Members members = memberService.findById(senderId);
        ChattingParticipant chattingParticipant = chattingParticipantService.find(chattingRoom, members);

        chattingRedisService.checkSubscriptionStatus(roomId, senderId);

        Slice<ChattingMessage> chattingMessages = loadMessage(roomId, chattingParticipant, pageNumber, pageSize, lastMessageTimeStamp);

        return getChatResponse(pageNumber, chattingMessages, chattingParticipant);
    }

    private ChatResponse getChatResponse(int pageNumber, Slice<ChattingMessage> chattingMessages, ChattingParticipant chattingParticipant) {
        List<ChattingMessageResponse> chattingMessageResponses = chattingMessages.stream()
                .map(ChattingMessageResponse::from)
                .toList();

        ChatPageableResponse chatPageableResponse = ChatPageableResponse.of(pageNumber, chattingMessages);

        return ChatResponse.of(chattingParticipant, chattingMessageResponses, chatPageableResponse);
    }

    private Slice<ChattingMessage> loadMessage(long roomId, ChattingParticipant chattingParticipant, int pageNumber, int pageSize, LocalDateTime lastMessageTimeStamp) {
        if (pageNumber == 0) {
            return loadInitialMessage(roomId, chattingParticipant, pageSize);
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, "timeStamp"));
        return chattingMessageRepository.findAllByRoomIdAndTimeStampAfterAndTimeStampBeforeOrderByTimeStampDesc(roomId, chattingParticipant.getJoinedAt(), lastMessageTimeStamp, pageable);
    }

    private Slice<ChattingMessage> loadInitialMessage(long roomId, ChattingParticipant chattingParticipant, int pageSize) {
        int chattingCount = chattingMessageRepository.countAllByRoomIdAndTimeStampAfterOrderByTimeStampDesc(roomId, chattingParticipant.getLastReadAt());

        int effectivePageSize = Math.max(chattingCount, pageSize);
        Pageable pageable = PageRequest.of(0, effectivePageSize, Sort.by(Sort.Direction.DESC, "timeStamp"));

        return chattingMessageRepository.findAllByRoomIdAndTimeStampAfterOrderByTimeStampDesc(roomId, chattingParticipant.getJoinedAt(), pageable);
    }

    private <T> T getSessionAttribute(SimpMessageHeaderAccessor accessor, String attributeName, Class<T> type) {
        return Optional.ofNullable(accessor.getSessionAttributes())
                .map(attrs -> type.cast(attrs.get(attributeName)))
                .orElseThrow(() -> new WebSocketSessionException(attributeName));
    }

    private long getUnreadCount(long roomId) {
        long totalCount = chattingParticipantService.getParticipantCount(roomId);
        long nowCount = chattingRedisService.getSubscriberCount(roomId);

        return totalCount - nowCount;
    }
}
