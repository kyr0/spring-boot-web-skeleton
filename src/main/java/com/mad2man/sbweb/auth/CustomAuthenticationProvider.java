package com.mad2man.sbweb.auth;

import com.mad2man.sbweb.auth.model.UserContext;
import com.mad2man.sbweb.common.Error;
import com.mad2man.sbweb.entity.UserEntity;
import com.mad2man.sbweb.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Authentication provider, used by Spring WebSecurity and REST methods.
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final BCryptPasswordEncoder encoder;
    private final UserService userService;

    @Autowired
    public CustomAuthenticationProvider(final UserService userService, final BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (authentication == null) {
            throw new IllegalArgumentException("No authentication data provided");
        }

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        UserEntity userEntity = userService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("UserEntity not found: " + username));

        if (!encoder.matches(password, userEntity.getPassword())) {
            throw new BadCredentialsException(Error.AUTHENTICATION_BAD_CREDENTIALS.getErrorMessage());
        }

        if (userEntity.getRoles() == null) {
            throw new InsufficientAuthenticationException(Error.AUTHORIZATION_NO_ROLES_GRANTED.getErrorMessage());
        }

        UserContext userContext = UserContext.create(userEntity.getId(), userEntity.getUsername(), userEntity.getAuthorities());

        return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
