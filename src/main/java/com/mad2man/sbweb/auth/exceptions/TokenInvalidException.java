package com.mad2man.sbweb.auth.exceptions;

import com.mad2man.sbweb.auth.model.token.JwtToken;
import org.springframework.security.core.AuthenticationException;

/**
 *
 */
public class TokenInvalidException extends AuthenticationException {

    private static final long serialVersionUID = -3514344383125727867L;

    private JwtToken token;

    public TokenInvalidException(String msg) {
        super(msg);
    }

    public TokenInvalidException(JwtToken token, String msg, Throwable t) {
        super(msg, t);
        this.token = token;
    }

    public String token() {
        return this.token.getToken();
    }
}
