package com.mad2man.sbweb.common.viewmodel;

import com.mad2man.sbweb.common.Error;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;

/**
 * ErrorViewModel viewmodel for interacting with client.
 */
@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class ErrorViewModel {

    @NonNull
    private String message;

    @NonNull
    private Error error;

    private String errorName;

    @NonNull
    private HttpStatus httpStatus;

    private List<FieldErrorViewModel> fieldErrors;

    private Date timestamp;

    private ErrorViewModel(final String message, final Error error, final HttpStatus httpStatus, final List<FieldErrorViewModel> fieldErrors) {
        this.message = message;
        this.error = error;
        this.errorName = error.name();
        this.httpStatus = httpStatus;
        this.timestamp = new Date();
        this.fieldErrors = fieldErrors;
    }

    public static ErrorViewModel of(final String message, final Error error, final HttpStatus httpStatus, final List<FieldErrorViewModel> fieldErrors) {
        return new ErrorViewModel(message, error, httpStatus, fieldErrors);
    }

    public static ErrorViewModel of(final String message, final Error error, final HttpStatus httpStatus) {
        return ErrorViewModel.of(message, error, httpStatus, null);
    }

    public static ErrorViewModel of(final Error error, final HttpStatus httpStatus) {
        return ErrorViewModel.of(error.getErrorMessage(), error, httpStatus, null);
    }
}
