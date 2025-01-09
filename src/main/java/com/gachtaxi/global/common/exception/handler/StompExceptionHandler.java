package com.gachtaxi.global.common.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gachtaxi.global.common.exception.BaseException;
import com.gachtaxi.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class StompExceptionHandler extends StompSubProtocolErrorHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        if (ex instanceof BaseException baseException) {
            return sendErrorMessage(baseException);
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    private Message<byte[]> sendErrorMessage(BaseException ex) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        accessor.setHeader("content-type", "application/json");
        accessor.setMessage(ex.getMessage());
        accessor.setLeaveMutable(true);

        ApiResponse<Object> response = ApiResponse.response(ex.getStatus(), ex.getMessage());

        String payload;

        try {
            payload = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            payload = "{\"error\": \"" + ex.getMessage() + "\"}";
        }

        return MessageBuilder.createMessage(
                payload.getBytes(StandardCharsets.UTF_8),
                accessor.getMessageHeaders()
        );
    }
}
