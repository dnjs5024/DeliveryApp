package com.example.delivery.common.exception;

import com.example.delivery.common.exception.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // %s 없음
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, Object... args) {
        super(errorCode.getMessage(args)); // ← 동적 메시지가 필요한 경우
        this.errorCode = errorCode;
    }
    //httpStatus 반환 메서드
    public HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }
}