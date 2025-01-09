package com.gachtaxi.domain.chat.stomp.strategy;

import com.gachtaxi.domain.chat.exception.ChatSubscribeException;
import com.gachtaxi.domain.chat.service.ChattingRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import static com.gachtaxi.domain.chat.stomp.strategy.StompConnectStrategy.CHAT_USER_ID;

@Component
@RequiredArgsConstructor
public class StompSubscribeStrategy implements StompCommandStrategy {

    private static final String SUB_END_POINT = "/sub/chat/room/";

    private final ChattingRoomService chattingRoomService;

    @Value("${chat.topic}")
    public String chatTopic;

    @Override
    public boolean supports(StompCommand command) {
        return StompCommand.SUBSCRIBE.equals(command);
    }

    @Override
    public Message<?> preSend(Message<?> message, StompHeaderAccessor accessor, MessageChannel channel) {
        String destination = accessor.getDestination();

        if (!destination.startsWith(SUB_END_POINT)) {
            throw new ChatSubscribeException();
        }

        Long senderId = (Long) accessor.getSessionAttributes().get(CHAT_USER_ID);
        Long roomId = Long.valueOf(destination.replace(SUB_END_POINT, ""));
        String senderName = accessor.getFirstNativeHeader("senderName");

        chattingRoomService.subscribeChatRoom(roomId, senderId, senderName);

        return message;
    }
}

