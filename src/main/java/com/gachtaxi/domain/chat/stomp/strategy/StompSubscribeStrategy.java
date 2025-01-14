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

@Component
@RequiredArgsConstructor
public class StompSubscribeStrategy implements StompCommandStrategy {

    public static final String CHAT_ROOM_ID = "CHAT_ROOM_ID";
    public static final String CHAT_USER_NAME = "CHAT_USER_NAME";
    private static final String SUB_END_POINT = "/sub/chat/room/";
    private static final String ERROR_END_POINT = "/user/queue/errors";
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

        if (destination.startsWith(SUB_END_POINT)) {
            Long roomId = Long.valueOf(destination.replace(SUB_END_POINT, ""));
            chattingRoomService.subscribeChatRoom(roomId, accessor);

            return message;
        }

        if (destination.startsWith(ERROR_END_POINT)) {
            return message;
        }

        throw new ChatSubscribeException();
    }
}

