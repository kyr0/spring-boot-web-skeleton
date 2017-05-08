package com.mad2man.sbweb.auth.model.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.Claims;
import lombok.Data;


/**
 * Raw representation of JWT Token.
 */
@Data
public final class AccessJwtToken implements JwtToken {

    private final String token;

    // claims not be be exported
    @JsonIgnore private Claims claims;

    protected AccessJwtToken(final String token, Claims claims) {
        this.token = token;
        this.claims = claims;
    }
}
