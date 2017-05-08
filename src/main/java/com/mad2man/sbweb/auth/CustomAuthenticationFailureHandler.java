package com.mad2man.sbweb.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mad2man.sbweb.auth.exceptions.AuthMethodNotSupportedException;
import com.mad2man.sbweb.auth.exceptions.JwtExpiredTokenException;
import com.mad2man.sbweb.common.ErrorCode;
import com.mad2man.sbweb.common.viewmodel.ErrorViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

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
			mapper.writeValue(response.getWriter(), ErrorViewModel.of("Invalid username or password", ErrorCode.AUTHENTICATION_GENERAL, HttpStatus.UNAUTHORIZED));
		} else if (e instanceof JwtExpiredTokenException) {
			mapper.writeValue(response.getWriter(), ErrorViewModel.of("Token has expired", ErrorCode.AUTHENTICATION_TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED));
		} else if (e instanceof AuthMethodNotSupportedException) {
		    mapper.writeValue(response.getWriter(), ErrorViewModel.of(e.getMessage(), ErrorCode.AUTHENTICATION_GENERAL, HttpStatus.UNAUTHORIZED));
		}

		mapper.writeValue(response.getWriter(), ErrorViewModel.of("Authentication failed", ErrorCode.AUTHENTICATION_GENERAL, HttpStatus.UNAUTHORIZED));
	}
}
