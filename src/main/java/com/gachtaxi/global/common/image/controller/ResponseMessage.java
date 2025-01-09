package com.gachtaxi.global.common.image.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {

    PRESIGNED_URL_GENERATE_SUCCESS("Presigned url 발급에 성공했습니다.");

    private final String message;

}
