package com.gachtaxi.domain.matching.common.controller;

import static com.gachtaxi.domain.matching.common.controller.ResponseMessage.CREATE_MANUAL_MATCHING_ROOM_SUCCESS;
import com.gachtaxi.domain.matching.common.dto.request.ManualMatchingRequest;
import com.gachtaxi.domain.matching.common.service.ManualMatchingService;
import com.gachtaxi.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "MANUAL", description = "수동매칭 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matching/manual")
public class ManualMatchingController {

    private final ManualMatchingService manualMatchingService;

    @Operation(summary = "수동 매칭방 생성")
    @PostMapping("/create")
    public ApiResponse<Long> createManualMatchingRoom(@Valid @RequestBody ManualMatchingRequest request) {
        Long roomId = manualMatchingService.createManualMatchingRoom(request);
        return ApiResponse.response(HttpStatus.OK, CREATE_MANUAL_MATCHING_ROOM_SUCCESS.getMessage(), roomId);
    }

}
