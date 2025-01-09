package com.gachtaxi.domain.chat.service;

import com.gachtaxi.domain.chat.dto.request.ChatMessage;
import com.gachtaxi.domain.chat.dto.request.ChatMessageRequest;
import com.gachtaxi.domain.chat.entity.ChattingMessage;
import com.gachtaxi.domain.chat.redis.RedisChatPublisher;
import com.gachtaxi.domain.chat.repository.ChattingMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import static com.gachtaxi.domain.chat.stomp.strategy.StompConnectStrategy.CHAT_USER_ID;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final ChattingMessageRepository chattingMessageRepository;
    private final RedisChatPublisher redisChatPublisher;

    @Value("${chat.topic}")
    public String chatTopic;

    public void chat(ChatMessageRequest request, SimpMessageHeaderAccessor accessor) {
        Long userId = (Long) accessor.getSessionAttributes().get(CHAT_USER_ID);

        ChatMessage chatMessage = ChatMessage.of(request, userId);
        ChattingMessage chattingMessage = ChattingMessage.of(chatMessage);

        chattingMessageRepository.save(chattingMessage);

        ChannelTopic topic = new ChannelTopic(chatTopic + chatMessage.roomId());

        redisChatPublisher.publish(topic, chatMessage);
    }
}
