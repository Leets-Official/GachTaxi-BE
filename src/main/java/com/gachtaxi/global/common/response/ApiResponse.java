package com.gachtaxi.global.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private final int code;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> response(HttpStatus httpStatus, String message, T data){
        return new ApiResponse<>(httpStatus.value(), message, data);
    }

    public static <T> ApiResponse<T> response(HttpStatus httpStatus, String message){
        return new ApiResponse<>(httpStatus.value(), message, null);
    }

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
