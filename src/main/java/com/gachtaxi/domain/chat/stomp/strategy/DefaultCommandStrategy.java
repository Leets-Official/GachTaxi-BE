package com.gachtaxi.domain.chat.stomp.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultCommandStrategy implements StompCommandStrategy{

    @Override
    public boolean supports(StompCommand command) {
        return false;
    }

    @Override
    public Message<?> preSend(Message<?> message, StompHeaderAccessor accessor, MessageChannel channel) {
        return message;
    }

    @Override
    public void postSend(Message<?> message, StompHeaderAccessor accessor, MessageChannel channel, boolean sent) {

    }
}
