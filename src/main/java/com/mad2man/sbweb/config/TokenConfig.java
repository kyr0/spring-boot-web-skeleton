package com.mad2man.sbweb.config;

import com.mad2man.sbweb.auth.model.token.JwtToken;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "token")
@Getter
@Setter
public class TokenConfig {
    /**
     * {@link JwtToken} will expire after this time.
     */
    private Integer expirationTimeInMinutes;

    /**
     * Token issuer
     */
    private String issuer;

    /**
     * Key is used to sign {@link JwtToken}.
     */
    private String signingKey;
}
