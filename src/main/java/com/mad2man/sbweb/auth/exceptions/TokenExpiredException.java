package com.mad2man.sbweb.auth.exceptions;

import com.mad2man.sbweb.auth.model.token.JwtToken;
import org.springframework.security.core.AuthenticationException;

/**
 *
 */
public class TokenExpiredException extends AuthenticationException {

    private static final long serialVersionUID = 3944843291266752711L;

    private JwtToken token;

    public TokenExpiredException(String msg) {
        super(msg);
    }

    public TokenExpiredException(JwtToken token, String msg, Throwable t) {
        super(msg, t);
        this.token = token;
    }

    public String token() {
        return this.token.getToken();
    }
}
