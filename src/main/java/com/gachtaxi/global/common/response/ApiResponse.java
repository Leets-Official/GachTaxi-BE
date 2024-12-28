package com.gachtaxi.global.common.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private final int code;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> fail(int code, String message, T data){
        return new ApiResponse<>(code, message, data);
    }

    public static <T> ApiResponse<T> fail(int code, String message){
        return new ApiResponse<>(code, message, null);
    }

    public static <T> ApiResponse<T> success(int code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }

    public static <T> ApiResponse<T> success(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
