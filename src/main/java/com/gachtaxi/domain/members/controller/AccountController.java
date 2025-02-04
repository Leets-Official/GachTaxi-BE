package com.gachtaxi.domain.members.controller;

import static com.gachtaxi.domain.members.controller.ResponseMessage.ACCOUNT_GET_SUCCESS;
import static com.gachtaxi.domain.members.controller.ResponseMessage.ACCOUNT_UPDATE_SUCCESS;
import static org.springframework.http.HttpStatus.OK;

import com.gachtaxi.domain.members.dto.request.AccountPostRequest;
import com.gachtaxi.domain.members.dto.response.AccountGetResponse;
import com.gachtaxi.domain.members.service.AccountService;
import com.gachtaxi.global.auth.jwt.annotation.CurrentMemberId;
import com.gachtaxi.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @GetMapping
  public ApiResponse<AccountGetResponse> getAccount(
      @CurrentMemberId Long memberId
  ) {
    return ApiResponse.response(OK, ACCOUNT_GET_SUCCESS.getMessage(), AccountGetResponse.of(this.accountService.getAccount(memberId)));
  }

  @PostMapping
  public ApiResponse<Void> updateAccount(
      @CurrentMemberId Long memberId,
      @RequestBody @Valid AccountPostRequest accountPostRequest
  ) {
    this.accountService.updateAccount(memberId, accountPostRequest.accountNumber());
    return ApiResponse.response(OK, ACCOUNT_UPDATE_SUCCESS.getMessage());
  }
}
