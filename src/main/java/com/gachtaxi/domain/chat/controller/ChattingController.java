package com.gachtaxi.domain.chat.controller;

import com.gachtaxi.domain.chat.dto.request.ChatMessageRequest;
import com.gachtaxi.domain.chat.dto.request.ChattingRoomResponse;
import com.gachtaxi.domain.chat.service.ChattingRoomService;
import com.gachtaxi.domain.chat.service.ChattingService;
import com.gachtaxi.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gachtaxi.domain.chat.controller.ResponseMessage.CREATE_CHATTING_ROOM_SUCCESS;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class ChattingController {

    private final ChattingService chattingService;
    private final ChattingRoomService chattingRoomService;

    @GetMapping("/api/chat/room")
    public ApiResponse<ChattingRoomResponse> createChattingRoom() {
        ChattingRoomResponse response = chattingRoomService.save();

        return ApiResponse.response(OK, CREATE_CHATTING_ROOM_SUCCESS.getMessage(), response);
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessageRequest request, SimpMessageHeaderAccessor headerAccessor) {
        chattingService.chat(request, headerAccessor);
    }
}
