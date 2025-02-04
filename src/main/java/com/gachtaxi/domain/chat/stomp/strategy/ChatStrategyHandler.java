package com.gachtaxi.domain.chat.stomp.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatStrategyHandler {

    private final List<StompCommandStrategy> stompCommandStrategies;
    private final DefaultCommandStrategy defaultCommandStrategy;

    public Message<?> handle(Message<?> message, StompHeaderAccessor accessor, MessageChannel channel) {
        StompCommand command = accessor.getCommand();

        return stompCommandStrategies.stream()
                .filter(strategy -> strategy.supports(command))
                .findFirst()
                .orElse(defaultCommandStrategy)
                .preSend(message, accessor, channel);
    }

    public void handle(Message<?> message, StompHeaderAccessor accessor, MessageChannel channel, boolean sent) {
        StompCommand command = accessor.getCommand();

        stompCommandStrategies.stream()
                .filter(strategy -> strategy.supports(command))
                .findFirst()
                .orElse(defaultCommandStrategy)
                .postSend(message, accessor, channel, sent);
    }
}
