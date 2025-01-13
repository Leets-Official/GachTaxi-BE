package com.gachtaxi.domain.members.dto.response;

import com.gachtaxi.domain.members.controller.ResponseMessage;

public record OauthResponse(
        String status
) {
    public static OauthResponse from(ResponseMessage responseMessage) {
        return new OauthResponse(responseMessage.name());
    }
}
