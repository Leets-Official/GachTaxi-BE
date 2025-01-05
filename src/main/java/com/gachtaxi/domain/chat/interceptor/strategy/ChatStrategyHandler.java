package com.gachtaxi.domain.chat.interceptor.strategy;

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

    public Message<?> handle(Message<?> message, StompHeaderAccessor accessor, MessageChannel channel) {
        StompCommand command = accessor.getCommand();

        return stompCommandStrategies.stream()
                .filter(strategy -> strategy.supports(command))
                .findFirst()
                .orElse(new DefaultCommandStrategy())
                .preSend(message, accessor, channel);
    }
}
