package com.mad2man.sbweb.auth.api;

import com.mad2man.sbweb.auth.exceptions.InvalidJwtToken;
import com.mad2man.sbweb.auth.jwt.Header;
import com.mad2man.sbweb.auth.jwt.extractor.TokenExtractor;
import com.mad2man.sbweb.auth.jwt.verifier.TokenVerifier;
import com.mad2man.sbweb.auth.model.UserContext;
import com.mad2man.sbweb.auth.model.token.JwtToken;
import com.mad2man.sbweb.auth.model.token.JwtTokenFactory;
import com.mad2man.sbweb.auth.model.token.RawAccessJwtToken;
import com.mad2man.sbweb.auth.model.token.RefreshToken;
import com.mad2man.sbweb.config.JwtConfig;
import com.mad2man.sbweb.entity.User;
import com.mad2man.sbweb.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RefreshTokenEndpoint
 */
@RestController
public class RefreshTokenResource {
    private final JwtTokenFactory tokenFactory;
    private final JwtConfig jwtConfig;
    private final UserService userService;
    private final TokenVerifier tokenVerifier;
    private final TokenExtractor tokenExtractor;

    @Autowired
    public RefreshTokenResource(JwtTokenFactory tokenFactory, UserService userService, TokenExtractor tokenExtractor, TokenVerifier tokenVerifier, JwtConfig jwtConfig) {
        this.tokenFactory = tokenFactory;
        this.userService = userService;
        this.tokenExtractor = tokenExtractor;
        this.tokenVerifier = tokenVerifier;
        this.jwtConfig = jwtConfig;
    }

    @GetMapping(value="/api/auth/token", produces={ MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    JwtToken refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String tokenPayload = tokenExtractor.extract(request.getHeader(Header.HTTP_AUTHORIZATION_HEADER_NAME));

        RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
        RefreshToken refreshToken = RefreshToken.create(rawToken, jwtConfig.getTokenSigningKey()).orElseThrow(InvalidJwtToken::new);

        String jti = refreshToken.getJti();
        if (!tokenVerifier.verify(jti)) {
            throw new InvalidJwtToken();
        }

        String subject = refreshToken.getSubject();
        User user = userService.findByUsername(subject).orElseThrow(() -> new UsernameNotFoundException("User not found: " + subject));

        if (user.getRoles() == null) throw new InsufficientAuthenticationException("User has no roles assigned");
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        UserContext userContext = UserContext.create(user.getUsername(), authorities);

        return tokenFactory.createAccessJwtToken(userContext);
    }
}
