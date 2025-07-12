package com.gachtaxi.domain.matching.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gachtaxi.domain.matching.common.dto.request.AutoMatchingCancelledRequest;
import com.gachtaxi.domain.matching.common.dto.request.AutoMatchingPostRequest;
import com.gachtaxi.domain.matching.common.dto.response.AutoMatchingPostResponse;
import com.gachtaxi.domain.matching.common.dto.response.AutoMatchingStatusGetResponse;
import com.gachtaxi.domain.matching.common.service.AutoMatchingService;
import com.gachtaxi.global.auth.jwt.annotation.CurrentMemberId;
import com.gachtaxi.global.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matching/auto")
public class AutoMatchingController {

  private final AutoMatchingService autoMatchingService;

  @PostMapping("/request")
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

  @GetMapping("/status")
  public ApiResponse<AutoMatchingStatusGetResponse> getMatchingStatus(
      @CurrentMemberId Long memberId
  ) {
    return ApiResponse.response(HttpStatus.OK,
        ResponseMessage.AUTO_MATCHING_INQUIRE_STATUS_SUCCESS.getMessage(),
        this.autoMatchingService.getMatchingStatus(memberId));
  }
}