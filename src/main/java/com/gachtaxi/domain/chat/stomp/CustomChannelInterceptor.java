package com.gachtaxi.domain.chat.stomp;

import com.gachtaxi.domain.chat.stomp.strategy.ChatStrategyHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomChannelInterceptor implements ChannelInterceptor {
    private final ChatStrategyHandler chatStrategyHandler;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        return chatStrategyHandler.handle(message, accessor, channel);
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        chatStrategyHandler.handle(message, accessor, channel, sent);
    }
}
