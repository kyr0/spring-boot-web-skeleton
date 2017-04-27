package com.mad2man.sbweb.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.mad2man.sbweb.auth.model.AccountCredentials;
import com.mad2man.sbweb.auth.service.TokenAuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    public JWTLoginFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException {
        try {
            AccountCredentials creds = new ObjectMapper()
                .readValue(req.getReader(), AccountCredentials.class);

            if ((creds.getUsername() == null || creds.getPassword() == null)) {
                throw new AuthenticationServiceException("Username or Password not provided");
            }
            return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                    creds.getUsername(),
                    //TODO: get here salt from db and add it here!!! to password
                    creds.getPassword(),
                    Collections.emptyList()
                )
            );
        } catch (UnrecognizedPropertyException e) {
            throw new AuthenticationServiceException("Missing (2 known properties: \"password\", \"username\"])");

        }
    }

    @Override
    protected void successfulAuthentication(
        HttpServletRequest req,
        HttpServletResponse res, FilterChain chain,
        Authentication auth) throws IOException, ServletException {
        TokenAuthenticationService
            .addAuthentication(res, auth.getName());
    }
}
