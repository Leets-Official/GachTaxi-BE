package com.gachtaxi.global.auth.jwt.exception;

import com.gachtaxi.global.common.exception.BaseException;
import static com.gachtaxi.global.auth.jwt.exception.JwtErrorMessage.JWT_TOKEN_UN_VALID;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class TokenInvalidException extends BaseException {
    public TokenInvalidException() {
        super(BAD_REQUEST, JWT_TOKEN_UN_VALID.getMessage());
    }
}
