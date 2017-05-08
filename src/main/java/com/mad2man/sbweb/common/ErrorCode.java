package com.mad2man.sbweb.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration of REST ErrorResponse types.
 */
public enum ErrorCode {
    UNKNOWN(2),
    AUTHENTICATION_GENERAL(10),
    AUTHENTICATION_TOKEN_EXPIRED(11);

    private int errorCode;

    private ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @JsonValue
    public int getErrorCode() {
        return errorCode;
    }
}
