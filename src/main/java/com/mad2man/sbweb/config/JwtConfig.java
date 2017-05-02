package com.mad2man.sbweb.config;

import com.mad2man.sbweb.auth.model.token.JwtToken;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    /**
     * {@link JwtToken} will expire after this time.
     */
    private Integer tokenExpirationTimeInMinutes;

    /**
     * Token issuer
     */
    private String tokenIssuer;

    /**
     * Key is used to sign {@link JwtToken}.
     */
    private String tokenSigningKey;

    /**
     * {@link JwtToken} can be refreshed during this timeframe.
     */
    private Integer refreshTokenExpirationTimeInMinutes;

    public Integer getRefreshTokenExpirationTimeInMinutes() {
        return refreshTokenExpirationTimeInMinutes;
    }

    public void setRefreshTokenExpirationTimeInMinutes(Integer refreshTokenExpirationTimeInMinutes) {
        this.refreshTokenExpirationTimeInMinutes = refreshTokenExpirationTimeInMinutes;
    }

    public Integer getTokenExpirationTimeInMinutes() {
        return tokenExpirationTimeInMinutes;
    }

    public void setTokenExpirationTimeInMinutes(Integer tokenExpirationTimeInMinutes) {
        this.tokenExpirationTimeInMinutes = tokenExpirationTimeInMinutes;
    }

    public String getTokenIssuer() {
        return tokenIssuer;
    }
    public void setTokenIssuer(String tokenIssuer) {
        this.tokenIssuer = tokenIssuer;
    }

    public String getTokenSigningKey() {
        return tokenSigningKey;
    }

    public void setTokenSigningKey(String tokenSigningKey) {
        this.tokenSigningKey = tokenSigningKey;
    }
}
