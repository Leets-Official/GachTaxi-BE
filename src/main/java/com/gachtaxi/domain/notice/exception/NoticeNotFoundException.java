package com.gachtaxi.domain.notice.exception;

import static com.gachtaxi.domain.notice.exception.ErrorMessage.NOTICE_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.gachtaxi.global.common.exception.BaseException;

public class NoticeNotFoundException extends BaseException {

    public NoticeNotFoundException() { super(NOT_FOUND, NOTICE_NOT_FOUND.getMessage()); }

}
