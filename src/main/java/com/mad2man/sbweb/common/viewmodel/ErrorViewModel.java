package com.mad2man.sbweb.common.viewmodel;

import com.mad2man.sbweb.common.ErrorCode;
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
@RequiredArgsConstructor(staticName = "of")
public class ErrorViewModel {

    @NonNull
    private String message;

    @NonNull
    private ErrorCode errorCode;

    @NonNull
    private HttpStatus status;

    @NonNull
    private Date timestamp;

    private List<FieldErrorViewModel> fieldErrors;

    public static ErrorViewModel of(String message, ErrorCode errorCode, HttpStatus status) {
        return ErrorViewModel.of(message, errorCode, status, new Date());
    }
}
