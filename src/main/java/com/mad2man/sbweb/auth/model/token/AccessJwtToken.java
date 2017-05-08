package com.mad2man.sbweb.auth.model.token;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;


/**
 * Raw representation of JWT Token.
 */
@Data
@AllArgsConstructor
public final class AccessJwtToken implements JwtToken {

    private final String token;

    private final Claims claims;

    private final Date expiration;
}
