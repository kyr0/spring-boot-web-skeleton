package com.mad2man.sbweb.auth.jwt.extractor;

import com.mad2man.sbweb.auth.jwt.Header;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

/**
 * An implementation of {@link TokenExtractor} extracts token from
 * Authorization: Bearer scheme.
 */
@Component
public class JwtHeaderTokenExtractor implements TokenExtractor {

    @Override
    public String extract(String header) {

        if (StringUtils.isBlank(header)) {
            throw new AuthenticationServiceException("Authorization header cannot be blank!");
        }

        if (header.length() < Header.HTTP_AUTHORIZATION_HEADER_PREFIX.length()) {
            throw new AuthenticationServiceException("Invalid authorization header size.");
        }
        return header.substring(Header.HTTP_AUTHORIZATION_HEADER_PREFIX.length(), header.length());
    }
}
