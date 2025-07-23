package com.mema.server.global.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <b>에러 응답 DTO</b>
 *
 */
public record ErrorResponse(
        String code,
        String message,
        int status,
        String path,
        LocalDateTime timestamp,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<FieldError> fieldErrors) {

    /**
     * Bean Validation의 필드 오류 정보를 담기 위한 서브 레코드.
     *
     * @param field  오류가 난 필드명
     * @param reason 오류 메시지
     */
    public record FieldError(String field, String reason) {}

    /** 일반 예외용 팩토리 메서드 */
    public static ErrorResponse of(ErrorCode e, HttpServletRequest req) {
        return new ErrorResponse(
                e.getCode(),
                e.getMessage(),
                e.getStatus().value(),
                req.getRequestURI(),
                LocalDateTime.now(),
                List.of());
    }

    /** Validation 실패용 팩토리 메서드 */
    public static ErrorResponse ofValidation(
            ErrorCode e, HttpServletRequest req, List<FieldError> errors) {
        return new ErrorResponse(
                e.getCode(),
                e.getMessage(),
                e.getStatus().value(),
                req.getRequestURI(),
                LocalDateTime.now(),
                errors);
    }
}
