package com.mad2man.sbweb.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration error types
 */
public enum ErrorCode {

    GENERAL(1000001),

    AUTHENTICATION_GENERAL(1010001),
    AUTHENTICATION_TOKEN_EXPIRED(1010002);

    private int errorCode;

    private ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @JsonValue
    public int getErrorCode() {
        return errorCode;
    }
}
