package com.example.delivery.common.response;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.common.exception.enums.SuccessCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.http.HttpStatus;


import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * 모든 API 응답을 하나의 공통 포맷으로 정의한 DTO
 * - 성공/실패 응답 모두 이 포맷을 따른다
 * - statusCode, message, data 를 포함한다
 */
@JsonInclude(value = NON_NULL)
@Builder
public record ApiResponseDto<T>( // record: dto를 간결하게 작성하는 방식으로 생성자/getter/equals 를 자동으로 만들어줌, 불변객체임
                                 int statusCode,                                // HTTP 상태 코드 숫자 (예: 400)
                                 @NotNull String message,                       // 응답 메시지 (ex: "가게 생성 성공", "잘못된 요청입니다")
                                 T data// 실제 응답 데이터 (nullable), null이면 JSON에서 제외됨
) {
    /**
     * 성공 응답을 생성하는 메소드 (데이터 O)
     * 예를 들면 가게 생성을 성공했을 때, 데이터도 함께 내려주고 싶을 때 사용함
     * @param successCode 성공 상태코드/메시지 Enum
     * @param data 응답 데이터
     * @return ApiResponseDto
     */
    public static <T> ApiResponseDto<T> success(final SuccessCode successCode,
                                                @Nullable final T data
                                                ) {
        return new ApiResponseDto<>(
                successCode.getHttpStatus().value(),
                successCode.getMessage(),
                data
        );
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
     * @return ApiResponseDto
     */
    public static <T> ApiResponseDto<T> fail(final ErrorCode errorCode) {
        return new ApiResponseDto<>(
                errorCode.getHttpStatus().value(),
                errorCode.getMessage(),
                null
        );
    }

    /**
     * 실패 응답을 생성하는 메소드 (직접 커스텀 메시지를 줄 경우)
     *
     * @return ApiResponseDto
     */
    public static <T> ApiResponseDto<T> fail(final ErrorCode errorCode, final String message) {
        return new ApiResponseDto<>(
                errorCode.getHttpStatus().value(),
                message,
                null
        );
    }
}