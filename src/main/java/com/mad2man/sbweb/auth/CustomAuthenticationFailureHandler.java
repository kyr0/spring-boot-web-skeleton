package com.mad2man.sbweb.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mad2man.sbweb.auth.exceptions.AuthMethodNotSupportedException;
import com.mad2man.sbweb.auth.exceptions.TokenExpiredException;
import com.mad2man.sbweb.auth.exceptions.TokenInvalidException;
import com.mad2man.sbweb.common.Error;
import com.mad2man.sbweb.common.viewmodel.ErrorViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper mapper;

    @Autowired
    public CustomAuthenticationFailureHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		if (e instanceof BadCredentialsException) {
			mapper.writeValue(response.getWriter(), ErrorViewModel.of(e.getMessage(), Error.AUTHENTICATION_BAD_CREDENTIALS, HttpStatus.UNAUTHORIZED));
		} else if (e instanceof TokenExpiredException) {
			mapper.writeValue(response.getWriter(), ErrorViewModel.of(Error.AUTHENTICATION_TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED));
		} else if (e instanceof TokenInvalidException) {
            mapper.writeValue(response.getWriter(), ErrorViewModel.of(Error.AUTHENTICATION_TOKEN_INVALID, HttpStatus.UNAUTHORIZED));
        } else if (e instanceof AuthMethodNotSupportedException) {
		    mapper.writeValue(response.getWriter(), ErrorViewModel.of(Error.AUTHENTICATION_METHOD_NOT_SUPPORTED, HttpStatus.UNAUTHORIZED));
		} else if (e instanceof InsufficientAuthenticationException) {

            // in case of not AUTHORIZED -> 403 Forbidden
            mapper.writeValue(response.getWriter(), ErrorViewModel.of(e.getMessage(), Error.AUTHORIZATION_NO_ROLES_GRANTED, HttpStatus.FORBIDDEN));

		} else {

            if (StringUtils.isEmpty(e.getMessage())) {
                mapper.writeValue(response.getWriter(), ErrorViewModel.of(Error.AUTHENTICATION_GENERAL, HttpStatus.UNAUTHORIZED));
            } else {
                mapper.writeValue(response.getWriter(), ErrorViewModel.of(e.getMessage(), Error.AUTHENTICATION_GENERAL, HttpStatus.UNAUTHORIZED));
            }
        }
	}
}
