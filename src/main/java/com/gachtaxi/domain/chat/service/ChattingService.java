package com.gachtaxi.domain.chat.service;

import com.gachtaxi.domain.chat.dto.request.ChatMessage;
import com.gachtaxi.domain.chat.dto.request.ChatMessageRequest;
import com.gachtaxi.domain.chat.entity.ChattingMessage;
import com.gachtaxi.domain.chat.exception.WebSocketSessionException;
import com.gachtaxi.domain.chat.redis.RedisChatPublisher;
import com.gachtaxi.domain.chat.repository.ChattingMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.gachtaxi.domain.chat.stomp.strategy.StompConnectStrategy.CHAT_USER_ID;
import static com.gachtaxi.domain.chat.stomp.strategy.StompSubscribeStrategy.CHAT_ROOM_ID;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final ChattingMessageRepository chattingMessageRepository;
    private final RedisChatPublisher redisChatPublisher;

    @Value("${chat.topic}")
    public String chatTopic;

    public void chat(ChatMessageRequest request, SimpMessageHeaderAccessor accessor) {
        long roomId = getSessionAttribute(accessor, CHAT_ROOM_ID, Long.class);
        long userId = getSessionAttribute(accessor, CHAT_USER_ID, Long.class);

        ChatMessage chatMessage = ChatMessage.of(request, roomId, userId);
        ChattingMessage chattingMessage = ChattingMessage.from(chatMessage);

        ChannelTopic topic = new ChannelTopic(chatTopic + chatMessage.roomId());
        redisChatPublisher.publish(topic, chatMessage);

        chattingMessageRepository.save(chattingMessage);
    }

    private <T> T getSessionAttribute(SimpMessageHeaderAccessor accessor, String attributeName, Class<T> type) {
        return Optional.ofNullable(accessor.getSessionAttributes())
                .map(attrs -> type.cast(attrs.get(attributeName)))
                .orElseThrow(() -> new WebSocketSessionException(attributeName));
    }
}
