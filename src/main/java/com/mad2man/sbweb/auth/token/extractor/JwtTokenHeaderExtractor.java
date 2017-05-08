package com.mad2man.sbweb.auth.token.extractor;

import com.mad2man.sbweb.auth.model.token.JwtToken;
import com.mad2man.sbweb.common.Error;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

/**
 * An implementation of {@link TokenExtractor} extracts token from
 * Authorization: Bearer scheme.
 */
@Component
public class JwtTokenHeaderExtractor implements TokenExtractor {

    @Override
    public String extract(String header) {

        if (StringUtils.isBlank(header)) {
            throw new AuthenticationServiceException(Error.AUTHENTICATION_AUTHORIZATION_HEADER_EMPTY.getErrorMessage());
        }

        if (header.length() < JwtToken.HTTP_AUTHORIZATION_HEADER_PREFIX.length()) {
            throw new AuthenticationServiceException(Error.AUTHENTICATION_AUTHORIZATION_HEADER_INVALID_SIZE.getErrorMessage());
        }
        return header.substring(JwtToken.HTTP_AUTHORIZATION_HEADER_PREFIX.length(), header.length());
    }
}
