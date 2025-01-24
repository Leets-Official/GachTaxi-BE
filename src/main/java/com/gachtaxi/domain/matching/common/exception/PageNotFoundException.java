package com.gachtaxi.domain.matching.common.exception;

import com.gachtaxi.global.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class PageNotFoundException extends BaseException {
    public PageNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorMessage.NOT_FOUND_PAGE.getMessage());
    }
}
