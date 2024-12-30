package com.gachtaxi.global.common.exception;

import lombok.Builder;

@Builder
public record ValidErrorResponse(

        String errorField,
        String errorMessage,
        Object inputValue

) {
}
