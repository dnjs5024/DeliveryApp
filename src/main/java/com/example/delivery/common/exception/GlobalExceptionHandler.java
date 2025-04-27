package com.example.delivery.common.exception;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.common.response.ApiResponseDto;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ServletRequest httpServletRequest;

    // ========================================================================================
    // 공통 실패 응답 만드는 메소드
    // ========================================================================================
    private ResponseEntity<ApiResponseDto<?>> errorResponse(ErrorCode errorCode, HttpServletRequest request) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponseDto.fail(errorCode));
    }

    private ResponseEntity<ApiResponseDto<?>> errorResponse(ErrorCode errorCode, String message, HttpServletRequest request) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponseDto.fail(errorCode, message));
    }

    /**
     * DTO 유효성 검증 실패 시 발생하는 예외 처리
     * - @Valid 어노테이션이 붙은 DTO 필드 검증 실패 시 발생
     * - 예: @NotBlank, @Size 등 제약 조건을 위반한 경우
     * - 사용자가 설정한 message 값을 추출하여 클라이언트에게 응답
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException
                                                                                           e, HttpServletRequest httpServletRequest) {
        log.error("유효성 검증 실패 " + e.getMessage(), e);
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fieldError -> fieldError.getDefaultMessage())
                .orElse("잘못된 요청입니다");
        return errorResponse(ErrorCode.BAD_REQUEST, errorMessage, httpServletRequest);
    }

    /**
     * DTO 타입 아닌 값들 검증 실패 시
     * - @Validated 어노테이션이 붙은 DTO 필드 검증 실패 시 발생
     * - 사용자가 설정한 message 값을 추출하여 클라이언트에게 응답
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDto<?>> handleConstraintViolationException(ConstraintViolationException
                                                                                        e, HttpServletRequest httpServletRequest) {
        log.error("Validated 유효성 검증 실패 " + e.getMessage(), e);
        String errorMessage = e.getConstraintViolations().iterator().next().getMessage();
        return errorResponse(ErrorCode.BAD_REQUEST, errorMessage, httpServletRequest);
    }

    /**
     * 요청 파라미터 타입이 일치하지 않을 때 발생하는 예외 처리
     * - 예: @RequestParam Long id 에 문자열이 들어올 경우
     */
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiResponseDto<?>> handlerMethodArgumentTypeMismatchException(Exception e, HttpServletRequest httpServletRequest) {
        log.error(
                "handlerMethodArgumentTypeMismatchException() in GlobalExceptionHandler throw MethodArgumentTypeMismatchException : {}",
                e.getMessage());
        return errorResponse(ErrorCode.BAD_REQUEST, httpServletRequest);
    }

    /**
     * @Validated 또는 @RequestParam 등에서 유효성 검증이 실패했을 때 발생
     * 타입이 아예 다를 때 발생하는 예외
     * - Spring 6+에서 유효성 관련 검증 실패 시 발생하는 예외
     */
    @ExceptionHandler(value = {HandlerMethodValidationException.class})
    public ResponseEntity<ApiResponseDto<?>> handlerHandlerMethodValidationException(Exception e, HttpServletRequest httpServletRequest) {
        log.error(
                "handlerHandlerMethodValidationException() in GlobalExceptionHandler throw HandlerMethodValidationException : {}",
                e.getMessage());
        return errorResponse(ErrorCode.BAD_REQUEST, httpServletRequest);
    }

    /**
     * 요청 헤더가 누락된 경우 발생하는 예외 처리
     * - 예: @RequestHeader("Authorization") 이 누락됐을 때
     */
    @ExceptionHandler(value = {MissingRequestHeaderException.class})
    public ResponseEntity<ApiResponseDto<?>> handlerMissingRequestHeaderException(Exception e, HttpServletRequest
            httpServletRequest) {
        log.error(
                "handlerMissingRequestHeaderException() in GlobalExceptionHandler throw MissingRequestHeaderException : {}",
                e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDto.fail(ErrorCode.MISSING_PARAMETER, httpServletRequest.getRequestURI()));
    }

    /**
     * 필수 요청 파라미터가 빠졌을 때 발생하는 예외 처리
     * - 예: @RequestParam(required = true) 가 누락된 경우
     */
    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public ResponseEntity<ApiResponseDto<?>> handlerMissingServletRequestParameterException(Exception e, HttpServletRequest httpServletRequest) {
        log.error(
                "handlerMissingServletRequestParameterException() in GlobalExceptionHandler throw MissingServletRequestParameterException : {}",
                e.getMessage());
        return errorResponse(ErrorCode.INVALID_PARAMETER, httpServletRequest);
    }

    /**
     * 요청 본문(JSON 등)이 올바르지 않아 역직렬화가 실패한 경우, JSON 파싱 실패
     * - 예: 잘못된 JSON 형식, 타입 불일치 등
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDto<?>> handleMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest httpServletRequest) {
        log.error(
                "handleMessageNotReadableException() in GlobalExceptionHandler throw HttpMessageNotReadableException : {}",
                e.getMessage());
        return errorResponse(ErrorCode.BAD_REQUEST, httpServletRequest);
    }

    /**
     * 존재하지 않는 URI로 접근했을 때 발생하는 예외 처리 404
     * - 예: 잘못된 URL 요청
     */
    @ExceptionHandler(value = {NoHandlerFoundException.class})
    public ResponseEntity<ApiResponseDto<?>> handleNoPageFoundException(Exception e, HttpServletRequest
            httpServletRequest) {
        log.error("handleNoPageFoundException() in GlobalExceptionHandler throw NoHandlerFoundException : {}",
                    e.getMessage());
            return errorResponse(ErrorCode.NOT_FOUND, httpServletRequest);
    }

    /**
     * 지원하지 않는 HTTP 메서드로 요청했을 때 발생
     * - 예: POST만 가능한 엔드포인트에 GET 요청 등
     */
    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ApiResponseDto<?>> handleMethodNotSupportedException(Exception e, HttpServletRequest
            httpServletRequest) {
        log.error(
                "handleMethodNotSupportedException() in GlobalExceptionHandler throw HttpRequestMethodNotSupportedException : {}",
                e.getMessage());
        return errorResponse(ErrorCode.METHOD_NOT_ALLOWED, httpServletRequest);
    }

    /**
     * 커스텀 예외(CustomException)를 처리하는 핸들러
     * - 개발자가 정의한 도메인/비즈니스 예외
     * - 예: 로그인 실패, 권한 없음 등
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponseDto<?>> handleCustomException(CustomException e, HttpServletRequest
            httpServletRequest) {
        log.error("handleException() in GlobalExceptionHandler throw BusinessException : {}", e.getMessage());
        return errorResponse(e.getErrorCode(), httpServletRequest);
    }

    /**
     * 그 외 모든 예외 처리
     * - 명시적으로 처리하지 않은 예외들을 잡아 처리
     * - 예: NullPointerException, DB 예외 등
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<?>> handlerException(Exception e, HttpServletRequest httpServletRequest) {
        log.error("handlerException() in GlobalExceptionHandler throw Exception : {} {}", e.getClass(), e.getMessage());
       return errorResponse(ErrorCode.INTERNAL_SERVER_ERROR, httpServletRequest);
    }


}
