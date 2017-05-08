package com.mad2man.sbweb.auth.token.extractor;

/**
 * Implementations of this interface should always return raw base-64 encoded
 * representation of JWT Token.
 */
public interface TokenExtractor {
    String extract(String payload);
}
