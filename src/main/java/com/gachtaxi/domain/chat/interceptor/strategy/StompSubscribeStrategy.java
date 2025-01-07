package com.gachtaxi.domain.chat.interceptor.strategy;

import com.gachtaxi.domain.chat.exception.ChatSubscribeException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class StompSubscribeStrategy implements StompCommandStrategy{

    private static final String SUB_END_POINT = "/sub/chat/room/";

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

        return message;
    }
}

