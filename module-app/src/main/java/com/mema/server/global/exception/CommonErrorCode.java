package com.mema.server.global.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * <b>전 모듈 공통 에러 코드</b>
 *
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CommonErrorCode implements ErrorCode {

    /* 4xx - Client Error */
    CLIENT_ERROR(HttpStatus.BAD_REQUEST, "400_CLIENT_REQUEST_ERROR", "클라이언트측의 요청에 오류가 존재합니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "400_INVALID_PARAMETER", "요청 파라미터가 올바르지 않습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "404_RESOURCE_NOT_FOUND", "요청한 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "405_METHOD_NOT_ALLOWED", "허용되지 않은 HTTP 메서드입니다."),

    /* 5xx - Server Error */
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500_SERVER_ERROR", "서버 내부 오류가 발생했습니다."),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500_UNKNOWN", "알 수 없는 서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    CommonErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}