package com.gachtaxi.domain.chat.service;

import com.gachtaxi.domain.chat.dto.request.ChatMessage;
import com.gachtaxi.domain.chat.dto.response.ChattingRoomResponse;
import com.gachtaxi.domain.chat.entity.ChattingMessage;
import com.gachtaxi.domain.chat.entity.ChattingParticipant;
import com.gachtaxi.domain.chat.entity.ChattingRoom;
import com.gachtaxi.domain.chat.entity.enums.ChatStatus;
import com.gachtaxi.domain.chat.entity.enums.MessageType;
import com.gachtaxi.domain.chat.exception.ChattingRoomNotFoundException;
import com.gachtaxi.domain.chat.redis.RedisChatPublisher;
import com.gachtaxi.domain.chat.repository.ChattingMessageRepository;
import com.gachtaxi.domain.chat.repository.ChattingRoomRepository;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.members.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gachtaxi.domain.chat.stomp.strategy.StompConnectStrategy.CHAT_USER_ID;
import static com.gachtaxi.domain.chat.stomp.strategy.StompSubscribeStrategy.CHAT_ROOM_ID;
import static com.gachtaxi.domain.chat.stomp.strategy.StompSubscribeStrategy.CHAT_USER_NAME;

@Service
@RequiredArgsConstructor
public class ChattingRoomService {

    private static final String ENTER_MESSAGE =" 님이 입장하셨습니다.";
    private static final String EXIT_MESSAGE =" 님이 퇴장하셨습니다.";

    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingMessageRepository chattingMessageRepository;
    private final ChattingParticipantService chattingParticipantService;
    private final MemberService memberService;
    private final RedisChatPublisher redisChatPublisher;
    private final ChattingRedisService chattingRedisService;

    @Value("${chat.topic}")
    public String chatTopic;

    @Transactional
    public ChattingRoomResponse save() {
        ChattingRoom chattingRoom = ChattingRoom.builder().build();

        chattingRoomRepository.save(chattingRoom);
        return ChattingRoomResponse.from(chattingRoom);
    }

    @Transactional
    public void delete(long chattingRoomId) {
        ChattingRoom chattingRoom = find(chattingRoomId);

        chattingRoom.delete();
    }

    @Transactional
    public void subscribeChatRoom(long roomId, SimpMessageHeaderAccessor accessor) {
        Long senderId = (Long) accessor.getSessionAttributes().get(CHAT_USER_ID);

        ChattingRoom chattingRoom = find(roomId);
        Members members = memberService.findById(senderId);

        accessor.getSessionAttributes().put(CHAT_ROOM_ID, roomId);
        accessor.getSessionAttributes().put(CHAT_USER_NAME, members.getNickname());

        if (chattingParticipantService.checkSubscription(chattingRoom, members)) {
            chattingRedisService.saveSubscribeMember(chattingRoom.getId(), members.getId(), members.getProfilePicture());

            return;
        }

        chattingRedisService.saveSubscribeMember(chattingRoom.getId(), members.getId(), members.getProfilePicture());

        ChattingParticipant newParticipant = ChattingParticipant.of(chattingRoom, members);
        chattingParticipantService.save(newParticipant);

        publishMessage(roomId, senderId, members.getNickname(), ENTER_MESSAGE, MessageType.ENTER);
    }

    @Transactional
    public void exitChatRoom(long roomId, long senderId) {
        ChattingRoom chattingRoom = find(roomId);
        Members members = memberService.findById(senderId);
        ChattingParticipant chattingParticipant = chattingParticipantService.find(chattingRoom, members);

        chattingParticipantService.delete(chattingParticipant);

        publishMessage(roomId, senderId, members.getNickname(),  EXIT_MESSAGE, MessageType.EXIT);
    }

    public ChattingRoom find(long chattingRoomId) {
        return chattingRoomRepository.findById(chattingRoomId)
                .filter(chattingRoom -> chattingRoom.getStatus() == ChatStatus.ACTIVE)
                .orElseThrow(ChattingRoomNotFoundException::new);
    }

    private void publishMessage(long roomId, long senderId, String senderName, String message, MessageType messageType) {
        ChattingMessage chattingMessage = ChattingMessage.of(roomId, senderId, senderName, senderName + message, messageType);

        chattingMessageRepository.save(chattingMessage);

        ChannelTopic topic = new ChannelTopic(chatTopic + roomId);
        ChatMessage chatMessage = ChatMessage.from(chattingMessage);

        redisChatPublisher.publish(topic, chatMessage);
    }

}
