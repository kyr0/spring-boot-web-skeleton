package com.mad2man.sbweb.auth.exceptions;

import org.springframework.security.authentication.AuthenticationServiceException;

/**
 *
 */
public class AuthMethodNotSupportedException extends AuthenticationServiceException {
    private static final long serialVersionUID = 1L;

    public AuthMethodNotSupportedException(String msg) {
        super(msg);
    }
}
