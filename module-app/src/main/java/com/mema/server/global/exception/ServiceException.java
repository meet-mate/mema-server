package com.mema.server.global.exception;

import lombok.Getter;

/**
 * <b>도메인/서비스 계층 공통 예외</b>
 *
 */
@Getter
public class ServiceException extends RuntimeException {

    private final ErrorCode errorCode;

    protected ServiceException(ErrorCode errorCode, String customMsg) {
        super(customMsg == null ? errorCode.getMessage() : customMsg);
        this.errorCode = errorCode;
    }

    public ServiceException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public static ServiceException of(ErrorCode code) {
        return new ServiceException(code, null);
    }

    public static ServiceException of(ErrorCode code, String msg) {
        return new ServiceException(code, msg);
    }
}
