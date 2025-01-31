package com.gachtaxi.domain.chat.stomp.strategy;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

public interface StompCommandStrategy {

    boolean supports(StompCommand command);

    Message<?> preSend(Message<?> message, StompHeaderAccessor accessor, MessageChannel channel);

    default void postSend(Message<?> message, StompHeaderAccessor accessor, MessageChannel channel, boolean sent) {

    }
}
