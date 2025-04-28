package com.example.delivery.common.exception;

import com.example.delivery.common.exception.enums.ErrorCode;

/**
 * 잘못된 요청일 때 사용하는 예외 (400 Bad Request)
 */
public class BadRequestException extends CustomException {

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BadRequestException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}