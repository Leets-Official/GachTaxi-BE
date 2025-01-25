package com.gachtaxi.domain.chat.stomp.strategy;

import com.gachtaxi.global.auth.jwt.exception.TokenNotExistException;
import com.gachtaxi.global.auth.jwt.util.JwtExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import static com.gachtaxi.global.auth.jwt.util.JwtProvider.ACCESS_TOKEN_SUBJECT;

@Component
@RequiredArgsConstructor
public class StompConnectStrategy implements StompCommandStrategy{

    private static final String TOKEN_PREFIX = "Bearer ";
    public static final String CHAT_USER_ID = "CHAT_USER_ID";

    private final JwtExtractor jwtExtractor;

    @Override
    public boolean supports(StompCommand command) {
        return StompCommand.CONNECT.equals(command);
    }

    @Override
    public Message<?> preSend(Message<?> message, StompHeaderAccessor accessor, MessageChannel channel) {
        String jwtToken = accessor.getFirstNativeHeader(ACCESS_TOKEN_SUBJECT);

        if(jwtToken == null || !jwtToken.startsWith(TOKEN_PREFIX)) {
            throw new TokenNotExistException();
        }

        String token = jwtToken.replace(TOKEN_PREFIX, "").trim();

        Long userId = jwtExtractor.getId(token);
        accessor.getSessionAttributes().put(CHAT_USER_ID, userId);

        return message;
    }

    @Override
    public void postSend(Message<?> message, StompHeaderAccessor accessor, MessageChannel channel, boolean sent) {

    }
}
