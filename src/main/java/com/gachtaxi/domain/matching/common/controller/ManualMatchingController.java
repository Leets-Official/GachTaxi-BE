package com.gachtaxi.domain.matching.common.controller;

import static com.gachtaxi.domain.matching.common.controller.ResponseMessage.CREATE_MANUAL_MATCHING_ROOM_SUCCESS;
import static com.gachtaxi.domain.matching.common.controller.ResponseMessage.JOIN_MANUAL_MATCHING_ROOM_SUCCESS;
import static com.gachtaxi.domain.matching.common.controller.ResponseMessage.LEAVE_MANUAL_MATCHING_ROOM_SUCCESS;

import com.gachtaxi.domain.matching.common.dto.request.ManualMatchingJoinRequest;
import com.gachtaxi.domain.matching.common.dto.request.ManualMatchingRequest;
import com.gachtaxi.domain.matching.common.service.ManualMatchingService;
import com.gachtaxi.global.auth.jwt.annotation.CurrentMemberId;
import com.gachtaxi.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "MANUAL", description = "수동매칭 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matching/manual")
public class ManualMatchingController {

    private final ManualMatchingService manualMatchingService;

    @Operation(summary = "수동 매칭방 생성")
    @PostMapping("/creation")
    public ApiResponse<Long> createManualMatchingRoom(@Valid @RequestBody ManualMatchingRequest request) {
        Long roomId = manualMatchingService.createManualMatchingRoom(request);
        return ApiResponse.response(HttpStatus.OK, CREATE_MANUAL_MATCHING_ROOM_SUCCESS.getMessage(), roomId);
    }

    @Operation(summary = "수동 매칭방 참여")
    @PostMapping("/join")
    public ApiResponse<Void> joinManualMatchingRoom(@CurrentMemberId Long userId, @Valid @RequestBody ManualMatchingJoinRequest request) {
        manualMatchingService.joinManualMatchingRoom(userId, request.roomId());
        return ApiResponse.response(HttpStatus.OK, JOIN_MANUAL_MATCHING_ROOM_SUCCESS.getMessage());
    }

    @Operation(summary = "수동 매칭방 퇴장 (방 삭제 포함)")
    @PatchMapping("/exit/{roomId}")
    public ApiResponse<Void> leaveManualMatchingRoom(@CurrentMemberId Long userId, @PathVariable Long roomId) {
        manualMatchingService.leaveManualMatchingRoom(userId, roomId);
        return ApiResponse.response(HttpStatus.OK, LEAVE_MANUAL_MATCHING_ROOM_SUCCESS.getMessage());
    }
}
