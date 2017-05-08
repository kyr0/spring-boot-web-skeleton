package com.mad2man.sbweb.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration error types
 */
public enum Error {

    // error codes: xxxyyyy
    // xxx  -> error type
    // yyyy -> error specifica

    // 100; General errors
    GENERAL(1000001, "General error"),

    // 101; Authentication - Bad credentials, token issuing, token validation, token expiration, ...
    AUTHENTICATION_GENERAL(1010001, "Authentication failed"),
    AUTHENTICATION_TOKEN_EXPIRED(1010002, "Authentication token expired"),
    AUTHENTICATION_BAD_CREDENTIALS(1010003, "Bad authentication credentials. Check username/password"),
    AUTHENTICATION_METHOD_NOT_SUPPORTED(101004, "Authentication HTTP method not supported. Use HTTP POST instead"),
    AUTHENTICATION_CREDENTIAL_MISSING(1010005, "Bad authentication credentials. Username or password missing"),
    AUTHENTICATION_TOKEN_INVALID(1010006, "Authentication token invalid"),
    AUTHENTICATION_AUTHORIZATION_HEADER_EMPTY(1010007, "Bad Authorization HTTP request header. Authorization header must be present. Token data is blank/missing/empty"),
    AUTHENTICATION_AUTHORIZATION_HEADER_INVALID_SIZE(1010008, "Bad Authorization HTTP request header. Check HTTP 'Authorization' request header and token data"),
    AUTHENTICATION_USERNAME_EMPTY(1010009, "Username is blank/missing/empty"),

    // 102; Authorization - Permission denied, missing roles, ...
    AUTHORIZATION_NO_ROLES_GRANTED(1020001, "Bad authorization. No roles granted");

    private int errorCode;
    private String errorMessage;

    Error(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @JsonValue
    public int getErrorCode() {
        return errorCode;
    }

    @JsonValue
    public String getErrorMessage() { return errorMessage; }
}
