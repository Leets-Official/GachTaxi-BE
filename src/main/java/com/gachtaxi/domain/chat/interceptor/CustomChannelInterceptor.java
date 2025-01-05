package com.gachtaxi.domain.chat.interceptor;

import com.gachtaxi.domain.chat.interceptor.strategy.ChatStrategyHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomChannelInterceptor implements ChannelInterceptor {
    private final ChatStrategyHandler chatStrategyHandler;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        return chatStrategyHandler.handle(message, accessor, channel);
    }
}