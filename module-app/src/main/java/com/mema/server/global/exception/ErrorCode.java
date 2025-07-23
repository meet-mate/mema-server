package com.mema.server.global.exception;

import org.springframework.http.HttpStatus;

/**
 * <b>공통 ErrorCode 계약</b>
 *
 * <p>각 도메인의 ErrorCode는 해당 인터페이스를 implements하여 정의한다.</p>
 */
public interface ErrorCode {
    HttpStatus getStatus();
    String getCode();
    String getMessage();
}