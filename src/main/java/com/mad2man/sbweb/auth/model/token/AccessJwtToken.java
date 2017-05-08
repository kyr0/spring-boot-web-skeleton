package com.mad2man.sbweb.auth.model.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.Claims;


/**
 * Raw representation of JWT Token.
 */
public final class AccessJwtToken implements JwtToken {
    private final String rawToken;

    // claims not be be exported
    @JsonIgnore private Claims claims;

    protected AccessJwtToken(final String token, Claims claims) {
        this.rawToken = token;
        this.claims = claims;
    }

    public String getToken() {
        return this.rawToken;
    }
}
