package com.mad2man.sbweb.auth.model.token;

import com.mad2man.sbweb.auth.exceptions.TokenExpiredException;
import com.mad2man.sbweb.auth.exceptions.TokenInvalidException;
import com.mad2man.sbweb.common.Error;
import io.jsonwebtoken.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Date;

@Data
@Slf4j
public class RawAccessJwtToken implements JwtToken {

    private String token;
    private Date expiration;

    public RawAccessJwtToken(String token) {
        this.token = token;
    }

    /**
     * Parses and validates JWT Token signature.
     *
     * @throws BadCredentialsException
     * @throws TokenExpiredException
     */
    public Jws<Claims> parseClaims(String signingKey) {

        try {

            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);

        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {

            log.error(Error.AUTHENTICATION_TOKEN_INVALID.getErrorMessage(), ex);

            throw new TokenInvalidException(this, Error.AUTHENTICATION_TOKEN_INVALID.getErrorMessage(), ex);

        } catch (ExpiredJwtException ex) {

            log.debug(Error.AUTHENTICATION_TOKEN_EXPIRED.getErrorMessage(), ex);

            throw new TokenExpiredException(this, Error.AUTHENTICATION_TOKEN_EXPIRED.getErrorMessage(), ex);
        }
    }
}
