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
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

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

    @Value("${chat.topic}")
    public String chatTopic;

    public void chat(ChatMessageRequest request, SimpMessageHeaderAccessor accessor) {
        long roomId = getSessionAttribute(accessor, CHAT_ROOM_ID, Long.class);
        long userId = getSessionAttribute(accessor, CHAT_USER_ID, Long.class);
        String senderName = getSessionAttribute(accessor, CHAT_USER_NAME, String.class);

        ChatMessage chatMessage = ChatMessage.of(request, roomId, userId, senderName);
        ChannelTopic topic = new ChannelTopic(chatTopic + chatMessage.roomId());
        ChattingMessage chattingMessage = ChattingMessage.from(chatMessage);

        chattingMessageRepository.save(chattingMessage);
        redisChatPublisher.publish(topic, chatMessage);
    }

    public ChatResponse getMessage(long roomId, long senderId, int pageNumber, int pageSize, LocalDateTime lastMessageTimeStamp) {
        ChattingRoom chattingRoom = chattingRoomService.find(roomId);
        Members members = memberService.find(senderId);
        ChattingParticipant chattingParticipant = chattingParticipantService.find(chattingRoom, members);

        Slice<ChattingMessage> chattingMessages = checkPageNumber(roomId, chattingParticipant, pageNumber, pageSize, lastMessageTimeStamp);

        List<ChattingMessageResponse> chattingMessageResponses = chattingMessages.stream()
                .map(ChattingMessageResponse::from)
                .toList();

        ChatPageableResponse chatPageableResponse = ChatPageableResponse.of(pageNumber, chattingMessages);

        return ChatResponse.of(chattingParticipant, chattingMessageResponses, chatPageableResponse);
    }

    private Slice<ChattingMessage> checkPageNumber(long roomId, ChattingParticipant chattingParticipant, int pageNumber, int pageSize, LocalDateTime lastMessageTimeStamp) {
        if (pageNumber == 0) {
            return loadInitialMessage(roomId, chattingParticipant);
        }

        return loadMessage(roomId, chattingParticipant.getJoinedAt(), lastMessageTimeStamp, pageNumber, pageSize);
    }

    private Slice<ChattingMessage> loadInitialMessage(long roomId, ChattingParticipant chattingParticipant) {
        int chattingCount = chattingMessageRepository.countAllByRoomIdAndTimeStampAfterOrderByTimeStampDesc(roomId, chattingParticipant.getDisconnectedAt());

        int effectivePageSize = Math.max(chattingCount, 20);
        Pageable pageable = PageRequest.of(0, effectivePageSize);

        return chattingMessageRepository.findAllByRoomIdAndTimeStampAfterOrderByTimeStampDesc(roomId, chattingParticipant.getJoinedAt(), pageable);
    }

    private Slice<ChattingMessage> loadMessage(long roomId, LocalDateTime joinedAt, LocalDateTime lastMessageTime, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        return chattingMessageRepository.findAllByRoomIdAndTimeStampAfterAndTimeStampBeforeOrderByTimeStampDesc(roomId, joinedAt, lastMessageTime, pageable);
    }

    private <T> T getSessionAttribute(SimpMessageHeaderAccessor accessor, String attributeName, Class<T> type) {
        return Optional.ofNullable(accessor.getSessionAttributes())
                .map(attrs -> type.cast(attrs.get(attributeName)))
                .orElseThrow(() -> new WebSocketSessionException(attributeName));
    }
}
