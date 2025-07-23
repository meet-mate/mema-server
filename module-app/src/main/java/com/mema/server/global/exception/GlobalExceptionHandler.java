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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


/**
 * <b>전역 예외 처리기</b>
 *
 * <li>Controller 계층에서 던져진 예외를 JSON 형태로 일관성 있게 반환
 * <li>Validation 예외들을 구분해 필드별 오류 정보 제공
 *
 **/
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

    /** 1. 도메인(Service) 예외 */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleService(ServiceException e, HttpServletRequest req) {
        logEx(req, e);
        return build(e.getErrorCode(), req);
    }

    /** 2. Bean Validation 실패 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest req) {
        logEx(req, ex);

        List<ErrorResponse.FieldError> fields =
                ex.getBindingResult().getFieldErrors().stream()
                        .map(
                                f ->
                                        new ErrorResponse.FieldError(
                                                f.getField(),
                                                (f.getDefaultMessage() != null) ? f.getDefaultMessage() : "invalid"))
                        .collect(Collectors.toList());

        ErrorResponse body =
                ErrorResponse.ofValidation(ErrorCode.VALIDATION_FAILED, req, fields);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /** 3. 파라미터 누락 */
    @ExceptionHandler(MissingRequestValueException.class)
    public ResponseEntity<ErrorResponse> handleMissing(
            MissingRequestValueException ex, HttpServletRequest req) {
        logEx(req, ex);
        return build(ErrorCode.MISSING_PARAMETER, req);
    }

    /** 4. HTTP 메서드 오류 */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        logEx(req, ex);
        return build(ErrorCode.METHOD_NOT_ALLOWED, req);
    }

    /** 5. 리소스 없음 */
    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<ErrorResponse> handleNoSuch(
            NoSuchElementException ex, HttpServletRequest req) {
        logEx(req, ex);
        return build(ErrorCode.NO_SUCH_RESOURCE, req);
    }

    /** 6. 존재하지 않는 API */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandler(
            NoHandlerFoundException ex, HttpServletRequest req) {
        logEx(req, ex);
        return build(ErrorCode.API_NOT_FOUND, req);
    }

    /** 7. 예상치 못한 모든 예외 – 500 Fallback */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception ex, HttpServletRequest req) {
        logEx(req, ex);
        return build(ErrorCode.UNKNOWN_ERROR, req);
    }
}
