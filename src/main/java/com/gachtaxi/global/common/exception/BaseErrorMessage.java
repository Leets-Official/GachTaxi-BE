package com.gachtaxi.global.common.exception;

import lombok.Getter;

@Getter
public enum BaseErrorMessage {

    BAD_REQUEST(400),
    SERVER_ERROR(500);

    private final int code;

    BaseErrorMessage(int code) {
        this.code = code;
    }
}
