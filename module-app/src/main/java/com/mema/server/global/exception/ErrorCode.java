package com.mema.server.global.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * <b>Error Code Enum</b>
 *
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {


    /* 4xx - Client Error */
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "400_INVALID_INPUT", "입력 값이 올바르지 않습니다."),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "400_MISSING_PARAM", "필수 파라미터가 누락되었습니다."),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "400_VALIDATION_FAIL", "유효성 검증에 실패했습니다."),

    API_NOT_FOUND(HttpStatus.NOT_FOUND, "404_API_NOT_FOUND", "요청한 API를 찾을 수 없습니다."),
    NO_SUCH_RESOURCE(HttpStatus.NOT_FOUND, "404_RESOURCE_NOT_FOUND", "요청한 리소스를 찾을 수 없습니다."), // <-- 코드값 수정

    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "405_METHOD_NOT_ALLOWED", "허용되지 않은 HTTP 메서드입니다."),

    /* 5xx - Server Error */
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500_INTERNAL", "서버 내부 오류가 발생했습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500_DATABASE", "데이터베이스 처리 중 오류가 발생했습니다."),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500_UNKNOWN", "알 수 없는 서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
