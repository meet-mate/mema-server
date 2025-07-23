package com.mema.server.global.exception;

import lombok.Getter;

/**
 * <b>공통 예외 abstract 클래스</b>
 *
 * <p>각 도메인의 서비스 예외 클래스는 해당 클래스를 extends 하여 구현한다.
 *
 */
@Getter
public abstract class BaseException extends RuntimeException{

    private final ErrorCode errorCode;


    protected BaseException(ErrorCode errorCode, String customMsg) {
        super(customMsg == null ? errorCode.getMessage() : customMsg);
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public static BaseException of(ErrorCode code) {
        return new DefaultBaseException(code, null);
    }

    public static BaseException of(ErrorCode code, String msg) {
        return new DefaultBaseException(code, msg);
    }
}

class DefaultBaseException extends BaseException {
    protected DefaultBaseException(ErrorCode errorCode) {
        super(errorCode, null);
    }
    protected DefaultBaseException(ErrorCode errorCode, String customMsg) {
        super(errorCode, customMsg);
    }
}
