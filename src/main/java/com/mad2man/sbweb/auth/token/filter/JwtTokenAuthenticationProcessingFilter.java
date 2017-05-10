package com.mad2man.sbweb.auth.token.filter;

import com.mad2man.sbweb.auth.model.UserContext;
import com.mad2man.sbweb.auth.model.token.AccessJwtToken;
import com.mad2man.sbweb.auth.model.token.JwtToken;
import com.mad2man.sbweb.auth.model.token.JwtTokenFactory;
import com.mad2man.sbweb.auth.model.token.RawAccessJwtToken;
import com.mad2man.sbweb.auth.token.JwtAuthenticationToken;
import com.mad2man.sbweb.auth.token.extractor.TokenExtractor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Performs validation of provided JWT Token.
 */
public class JwtTokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationFailureHandler failureHandler;
    private final TokenExtractor tokenExtractor;
    private final JwtTokenFactory tokenFactory;

    public JwtTokenAuthenticationProcessingFilter(AuthenticationFailureHandler failureHandler,
                                                  TokenExtractor tokenExtractor, RequestMatcher matcher,
                                                  JwtTokenFactory tokenFactory) {

        super(matcher);

        this.failureHandler = failureHandler;
        this.tokenExtractor = tokenExtractor;
        this.tokenFactory = tokenFactory;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        String tokenPayload = request.getHeader(JwtToken.HTTP_AUTHORIZATION_HEADER_NAME);
        RawAccessJwtToken token = new RawAccessJwtToken(tokenExtractor.extract(tokenPayload));

        Authentication authentication = getAuthenticationManager().authenticate(new JwtAuthenticationToken(token));

        UserContext userContext = (UserContext) authentication.getPrincipal();

        // auto-respond with a new accessToken via header response
        AccessJwtToken refreshedAccessToken = tokenFactory.createAccessJwtToken(userContext);
        response.addHeader(JwtToken.HTTP_AUTHORIZATION_HEADER_NAME, refreshedAccessToken.getToken());

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);

        SecurityContextHolder.setContext(context);

        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        SecurityContextHolder.clearContext();

        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
