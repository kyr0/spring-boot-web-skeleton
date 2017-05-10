package com.mad2man.sbweb.auth.model.token;

import com.mad2man.sbweb.auth.model.UserContext;
import com.mad2man.sbweb.config.TokenConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Factory class that should be always used to create {@link JwtToken}.
 */
@Component
public class JwtTokenFactory {

    private final TokenConfig settings;

    @Autowired
    public JwtTokenFactory(TokenConfig settings) {
        this.settings = settings;
    }

    /**
     * Factory method for issuing new JWT tokens
     */
    public AccessJwtToken createAccessJwtToken(UserContext userContext) {

        if (StringUtils.isBlank(userContext.getUserId())) {
            throw new IllegalArgumentException("Cannot create JWT token without a user id");
        }

        Claims claims = Jwts.claims().setSubject(userContext.getUserId());

        LocalDateTime now = LocalDateTime.now();
        Instant instant = now.atZone(ZoneId.systemDefault()).toInstant();
        Date expiration = Date.from(now.plusMinutes(settings.getExpirationTimeInMinutes()).atZone(ZoneId.systemDefault()).toInstant());

        String token = Jwts.builder()
          .setClaims(claims)
          .setIssuer(settings.getIssuer())
          .setIssuedAt(Date.from(instant))
          .setExpiration(expiration)
          .signWith(SignatureAlgorithm.HS512, settings.getSigningKey())
            .compact();

        return new AccessJwtToken(token, claims, expiration);
    }
}
