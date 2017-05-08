package com.mad2man.sbweb.auth.exceptions;

import com.mad2man.sbweb.auth.model.token.JwtToken;
import org.springframework.security.core.AuthenticationException;

/**
 *
 */
public class JwtExpiredTokenException extends AuthenticationException {

    private static final long serialVersionUID = 3944843291266752711L;

    private JwtToken token;

    public JwtExpiredTokenException(String msg) {
        super(msg);
    }

    public JwtExpiredTokenException(JwtToken token, String msg, Throwable t) {
        super(msg, t);
        this.token = token;
    }

    public String token() {
        return this.token.getToken();
    }
}
