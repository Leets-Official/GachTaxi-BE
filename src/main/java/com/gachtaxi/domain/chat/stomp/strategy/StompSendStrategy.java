package com.gachtaxi.domain.chat.stomp.strategy;


import com.gachtaxi.domain.chat.exception.ChatSendEndPointException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class StompSendStrategy implements StompCommandStrategy {

    private static final String SEND_END_POINT = "/pub/chat/message";

    @Override
    public boolean supports(StompCommand command) {
        return StompCommand.SEND.equals(command);
    }

    @Override
    public Message<?> preSend(Message<?> message, StompHeaderAccessor accessor, MessageChannel channel) {
        String destination = accessor.getDestination();

        if (!destination.startsWith(SEND_END_POINT)) {
            throw new ChatSendEndPointException();
        }

        return message;
    }

    @Override
    public void postSend(Message<?> message, StompHeaderAccessor accessor, MessageChannel channel, boolean sent) {

    }
}
