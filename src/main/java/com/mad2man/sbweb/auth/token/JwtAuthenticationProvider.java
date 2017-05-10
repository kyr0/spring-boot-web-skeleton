package com.mad2man.sbweb.auth.token;

import com.mad2man.sbweb.auth.model.UserContext;
import com.mad2man.sbweb.auth.model.token.JwtToken;
import com.mad2man.sbweb.auth.model.token.RawAccessJwtToken;
import com.mad2man.sbweb.config.TokenConfig;
import com.mad2man.sbweb.entity.UserEntity;
import com.mad2man.sbweb.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Used to validate incoming tokens.
 *
 * An {@link AuthenticationProvider} implementation that will use provided
 * instance of {@link JwtToken} to perform authentication.
 */
@Component
@SuppressWarnings("unchecked")
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final TokenConfig tokenConfig;
    private final UserService userService;

    @Autowired
    public JwtAuthenticationProvider(UserService userService, TokenConfig tokenConfig) {
        this.tokenConfig = tokenConfig;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();

        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(tokenConfig.getSigningKey());

        String userId = jwsClaims.getBody().getSubject();

        UserEntity userEntity = userService.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("UserEntity not found: " + userId));

        UserContext userContext = UserContext.create(userEntity.getId(), userEntity.getUsername(), userEntity.getAuthorities());

        return new JwtAuthenticationToken(userContext, userEntity.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
