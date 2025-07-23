package com.mema.server.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    private void logEx(HttpServletRequest req, Exception e) {
        log.error(
                "[EXCEPTION] uri={} method={} type={} msg={}",
                req.getRequestURI(),
                req.getMethod(),
                e.getClass().getSimpleName(),
                e.getMessage(),
                e);
    }

    private ResponseEntity<ErrorResponse> build(ErrorCode code, HttpServletRequest req) {
        return ResponseEntity.status(code.getStatus()).body(ErrorResponse.of(code, req));
    }

    /** 1. Unhandled Custom Exception */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleService(BaseException e, HttpServletRequest req) {
        logEx(req, e);
        return build(e.getErrorCode(), req);
    }

    /** 2. Bean Validation 실패 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest req) {
        logEx(req, ex);

        return build(CommonErrorCode.INVALID_PARAMETER, req);
    }

    /** 3. 파라미터 에러 */
    @ExceptionHandler(MissingRequestValueException.class)
    public ResponseEntity<ErrorResponse> handleMissing(
            MissingRequestValueException ex, HttpServletRequest req) {
        logEx(req, ex);
        return build(CommonErrorCode.INVALID_PARAMETER, req);
    }

    /** 4. HTTP 메서드 오류 */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        logEx(req, ex);
        return build(CommonErrorCode.METHOD_NOT_ALLOWED, req);
    }

    /** 5. 리소스 없음 */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuch(
            NoSuchElementException ex, HttpServletRequest req) {
        logEx(req, ex);
        return build(CommonErrorCode.RESOURCE_NOT_FOUND, req);
    }

    /** 6. 클라이언트 에러 */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandler(
            NoHandlerFoundException ex, HttpServletRequest req) {
        logEx(req, ex);
        return build(CommonErrorCode.CLIENT_ERROR, req);
    }

    /** 7. 예상치 못한 모든 예외 – 500 Fallback */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception ex, HttpServletRequest req) {
        logEx(req, ex);
        return build(CommonErrorCode.UNKNOWN_ERROR, req);
    }
}
