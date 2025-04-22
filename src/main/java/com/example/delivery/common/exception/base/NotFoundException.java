package com.example.delivery.common.exception.base;

import com.example.delivery.common.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends CustomException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}