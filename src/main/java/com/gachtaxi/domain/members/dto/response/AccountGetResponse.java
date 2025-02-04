package com.gachtaxi.domain.members.dto.response;

public record AccountGetResponse(
    String accountNumber
) {

  public static AccountGetResponse of(String accountNumber) {
    return new AccountGetResponse(accountNumber);
  }
}
