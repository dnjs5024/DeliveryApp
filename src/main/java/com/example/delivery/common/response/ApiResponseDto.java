package com.example.delivery.common.response;
//API 응답의 공통 포멧
// 1. 응답이 항성 똑같은 구조라 에러 코드를 복잡하게 안짜도 되고 보기에도 편함
// 2. 백엔드 입장에선 API가 늘어나도 다 같은 방식으로 응답하기 때문에 유지보수에도 좋음
// 3. 예외를 하나의 포멧으로 포장하면 클라이언트 입장에서 status, message만 보면 되니까 좋음

import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.common.exception.enums.SuccessCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.micrometer.common.lang.Nullable;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.http.HttpStatus;


import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * 모든 API 응답을 하나의 공통 포맷으로 정의한 DTO
 * - 성공/실패 응답 모두 이 포맷을 따른다
 * - timestamp, statusCode, message, path, data를 포함한다
 */
@Builder
public record ApiResponseDto<T>( // record: dto를 간결하게 작성하는 방식으로 생성자/getter/equals 를 자동으로 만들어줌, 불변객체임
                                 LocalDateTime timestamp,                       // 요청 시각
                                 int statusCode,                                // HTTP 상태 코드 숫자 (예: 400)
                                 @NonNull String message,                       // 응답 메시지 (ex: "가게 생성 성공", "잘못된 요청입니다")
                                 @JsonInclude(value = NON_NULL) String path,    // 요청 경로 (ex : api/stores),  null이면 JSON에서 제외됨
                                 @JsonInclude(value = NON_NULL) T data          // 실제 응답 데이터 (nullable), null이면 JSON에서 제외됨
) {
    /**
     * 성공 응답을 생성하는 메소드 (데이터 O)
     * 예를 들면 가게 생성을 성공했을 때, 데이터도 함께 내려주고 싶을 때 사용함
     * @param successCode 성공 상태코드/메시지 Enum
     * @param data 응답 데이터
     * @return ApiResponseDto
     */
    public static <T> ApiResponseDto<T> success(final SuccessCode successCode, @Nullable final T data) {
        return ApiResponseDto.<T>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(successCode.getHttpStatus().value())
                .message(successCode.getMessage())
                .path(null)
                .data(data)
                .build();
    }
    /**
     * 성공 응답을 생성하는 메소드 (데이터 X)
     * 예를 들면 로그아웃에 성공했습니다 -> 따로 줄 데이터가 없음
     * @param successCode 성공 상태코드/메시지 Enum
     * @return ApiResponseDto
     */
    public static <T> ApiResponseDto<T> success(final SuccessCode successCode) {
        return success(successCode, null);
    }
    /**
     * 실패 응답을 생성하는 메소드 (ErrorCode 기반)
     *
     * @param errorCode 에러 코드 Enum
     * @param path 요청 경로
     * @return ApiResponseDto
     */
    public static <T> ApiResponseDto<T> fail(final ErrorCode errorCode, final String path) {
        return ApiResponseDto.<T>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .path(path)
                .data(null)
                .build();
    }

    /**
     * 실패 응답을 생성하는 메소드 (직접 커스텀 메시지를 줄 경우)
     *
     * @param message 커스텀 실패 메시지
     * @param path 요청 경로
     * @return ApiResponseDto
     */
    public static <T> ApiResponseDto<T> fail(final String message, final String path) {
        return ApiResponseDto.<T>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .path(path)
                .data(null)
                .build();
    }
}