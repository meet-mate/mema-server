package com.mema.server.global.exception;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

/**
 * <b>공통 에러 응답 인터페이스</b>
 *
 * <p>각 도메인의 에러 응답 클래스는 해당 인터페이스를 implements 하여 구현한다.</p>
 */
public interface ErrorResponse{
    String code();
    String message();
    int status();
    String path();
    LocalDateTime timestamp();

    public static ErrorResponse from(ErrorCode e, HttpServletRequest req) {
        return new DefaultErrorResponse(
                e.getCode(),
                e.getMessage(),
                e.getStatus().value(),
                req.getRequestURI(),
                LocalDateTime.now());
    }
}
record DefaultErrorResponse(
        String code,
        String message,
        int status,
        String path,
        LocalDateTime timestamp
) implements ErrorResponse {}