package com.gachtaxi.global.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final int errorCode;

    public BaseException(final String message, final int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}

