package com.gachtaxi.domain.members.exception;

import com.gachtaxi.global.common.exception.BaseException;

import static com.gachtaxi.domain.members.exception.ErrorMessage.DUPLICATED_STUDENT_NUMBER;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class DuplicatedStudentNumberException extends BaseException {
    public DuplicatedStudentNumberException() {
        super(BAD_REQUEST, DUPLICATED_STUDENT_NUMBER.getMessage());
    }
}
