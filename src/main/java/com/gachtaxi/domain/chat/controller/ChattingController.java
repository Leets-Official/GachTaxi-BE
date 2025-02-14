package com.gachtaxi.domain.chat.controller;

import com.gachtaxi.domain.chat.dto.request.ChatMessageRequest;
import com.gachtaxi.domain.chat.dto.response.ChatResponse;
import com.gachtaxi.domain.chat.dto.response.ChattingRoomCountResponse;
import com.gachtaxi.domain.chat.dto.response.ChattingRoomResponse;
import com.gachtaxi.domain.chat.service.ChattingRoomService;
import com.gachtaxi.domain.chat.service.ChattingService;
import com.gachtaxi.global.auth.jwt.annotation.CurrentMemberId;
import com.gachtaxi.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.gachtaxi.domain.chat.controller.ResponseMessage.*;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "CHAT")
@RestController
@RequiredArgsConstructor
public class ChattingController {

    private final ChattingService chattingService;
    private final ChattingRoomService chattingRoomService;

    @GetMapping("/api/chat/{roomId}")
    @Operation(summary = "채팅방 재입장시 이전 메시지를 조회하기 위한 API입니다.")
    public ApiResponse<ChatResponse> getChattingMessages(@PathVariable Long roomId,
                                                         @CurrentMemberId Long memberId,
                                                         @RequestParam int pageNumber,
                                                         @RequestParam int pageSize,
                                                         @RequestParam(required = false) LocalDateTime lastMessageTimeStamp) {
        ChatResponse response = chattingService.getMessage(roomId, memberId, pageNumber, pageSize, lastMessageTimeStamp);

        return ApiResponse.response(OK, GET_CHATTING_MESSAGE_SUCCESS.getMessage(), response);
    }

    @GetMapping("/api/chat/count/{roomId}")
    @Operation(summary = "채팅방의 총 참여자 수를 조회하기 위한 API입니다.")
    public ApiResponse<ChattingRoomCountResponse> getChattingMessageCount(@CurrentMemberId Long memberId,
                                                                          @PathVariable Long roomId) {
        ChattingRoomCountResponse response = chattingRoomService.getCount(memberId, roomId);

        return ApiResponse.response(OK, GET_CHATTING_PARTICIPANT_COUNT_SUCCESS.getMessage(), response);
    }

    @DeleteMapping("/api/chat/{roomId}")
    @Operation(summary = "채팅방을 퇴장하는 API입니다. 퇴장시 STOMP UNSUBSCRIBE를 꼭 해주세요")
    public ApiResponse<Void> exitChattingRoom(@PathVariable Long roomId,
                                              @CurrentMemberId Long memberId) {
        chattingRoomService.exitChatRoom(roomId, memberId);

        return ApiResponse.response(OK, EXIT_CHATTING_ROOM_SUCCESS.getMessage());
    }

    @MessageMapping("/chat/message")
    public void message(@Valid ChatMessageRequest request, SimpMessageHeaderAccessor headerAccessor) {
        chattingService.chat(request, headerAccessor);
    }
}
