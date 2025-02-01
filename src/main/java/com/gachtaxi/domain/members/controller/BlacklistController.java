package com.gachtaxi.domain.members.controller;

import static com.gachtaxi.domain.members.controller.ResponseMessage.BLACKLIST_DELETE_SUCCESS;
import static com.gachtaxi.domain.members.controller.ResponseMessage.BLACKLIST_FIND_ALL_SUCCESS;
import static com.gachtaxi.domain.members.controller.ResponseMessage.BLACKLIST_SAVE_SUCCESS;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.gachtaxi.domain.members.dto.response.BlacklistGetResponse;
import com.gachtaxi.domain.members.dto.response.BlacklistPostResponse;
import com.gachtaxi.domain.members.service.BlacklistService;
import com.gachtaxi.global.auth.jwt.annotation.CurrentMemberId;
import com.gachtaxi.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BLACKLIST")
@RestController
@RequestMapping("/api/blacklists")
@RequiredArgsConstructor
public class BlacklistController {

  private final BlacklistService blacklistService;

  @Operation(summary = "블랙리스트 등록 API")
  @PostMapping("/{receiverId}")
  public ApiResponse<BlacklistPostResponse> postBlacklist(
      @CurrentMemberId Long memberId,
      @RequestParam Long receiverId) {

    return ApiResponse.response(CREATED, BLACKLIST_SAVE_SUCCESS.getMessage(),
        this.blacklistService.save(memberId, receiverId));
  }

  @Operation(summary = "블랙리스트 삭제 API")
  @DeleteMapping("/{receiverId}")
  public ApiResponse<Void> deleteBlacklist(
      @CurrentMemberId Long memberId,
      @PathVariable Long receiverId) {
    this.blacklistService.delete(memberId, receiverId);

    return ApiResponse.response(OK, BLACKLIST_DELETE_SUCCESS.getMessage());
  }

  @Operation(summary = "블랙리스트 조회 API")
  @GetMapping("/{pageNum}")
  public ApiResponse<BlacklistGetResponse> getAllBlacklist(
      @CurrentMemberId Long requesterId,
      @PathVariable int pageNum) {
    BlacklistGetResponse blacklistPage = this.blacklistService.findBlacklistPage(requesterId,
        pageNum);

    return ApiResponse.response(OK, BLACKLIST_FIND_ALL_SUCCESS.getMessage(), blacklistPage);
  }
}
