package com.mad2man.sbweb.common.api.response;

import com.mad2man.sbweb.common.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;

/**
 * ErrorResponse model for interacting with client.
 */
public class ErrorResponse {

    private final HttpStatus status;
    private final String message;
    private final ErrorCode errorCode;
    private List<FieldError> fieldErrors;

    private final Date timestamp;

    protected ErrorResponse(final String message, final ErrorCode errorCode, HttpStatus status) {
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.timestamp = new Date();
    }

    public static ErrorResponse of(final String message, final ErrorCode errorCode, HttpStatus status) {
        return new ErrorResponse(message, errorCode, status);
    }

    public Integer getStatus() {
        return status.value();
    }

    public String getMessage() {
        return message;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
