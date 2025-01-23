package com.gachtaxi.domain.matching.common.controller;

import com.gachtaxi.domain.matching.aop.SseSubscribeRequired;
import com.gachtaxi.domain.matching.common.dto.request.AutoMatchingCancelledRequest;
import com.gachtaxi.domain.matching.common.dto.request.AutoMatchingPostRequest;
import com.gachtaxi.domain.matching.common.dto.response.AutoMatchingPostResponse;
import com.gachtaxi.domain.matching.common.service.AutoMatchingService;
import com.gachtaxi.global.auth.jwt.annotation.CurrentMemberId;
import com.gachtaxi.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matching/auto")
public class AutoMatchingController {

  private final AutoMatchingService autoMatchingService;

  @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribeSse(@CurrentMemberId Long memberId) {
    return this.autoMatchingService.handleSubscribe(memberId);
  }

  @PostMapping("/request")
  @SseSubscribeRequired
  public ApiResponse<AutoMatchingPostResponse> requestMatching(
      @CurrentMemberId Long memberId,
      @RequestBody AutoMatchingPostRequest autoMatchingPostRequest
  ) {
    return ApiResponse.response(
        HttpStatus.OK,
        ResponseMessage.AUTO_MATCHING_REQUEST_ACCEPTED.getMessage(),
        this.autoMatchingService.handlerAutoRequestMatching(memberId, autoMatchingPostRequest)
    );
  }

  @PostMapping("/cancel")
  @SseSubscribeRequired
  public ApiResponse<AutoMatchingPostResponse> cancelMatching(
      @CurrentMemberId Long memberId,
      @RequestBody AutoMatchingCancelledRequest autoMatchingCancelledRequest
  ) {
    return ApiResponse.response(
        HttpStatus.OK,
        ResponseMessage.AUTO_MATCHING_REQUEST_CANCELLED.getMessage(),
        this.autoMatchingService.handlerAutoCancelMatching(memberId, autoMatchingCancelledRequest)
    );
  }
}