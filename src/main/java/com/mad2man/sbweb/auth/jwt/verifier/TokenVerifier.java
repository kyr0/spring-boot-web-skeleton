package com.mad2man.sbweb.auth.jwt.verifier;

/**
 */
public interface TokenVerifier {
    public boolean verify(String jti);
}
