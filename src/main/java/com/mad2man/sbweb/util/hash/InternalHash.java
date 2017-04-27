package com.mad2man.sbweb.util.hash;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public abstract class InternalHash extends BaseHash {

    private MessageDigest messageDigest;

    public InternalHash(String hashName) {
        try {
            this.messageDigest = MessageDigest.getInstance(hashName);
        } catch (NoSuchAlgorithmException ex) {
        }
    }

    @Override
    protected void reset() {
        messageDigest.reset();
    }

    @Override
    public byte[] hash(byte[] input) throws IllegalArgumentException {
        this.reset();
        messageDigest.update(input);
        return messageDigest.digest();
    }

    @Override
    public byte[] hash(String input) throws IllegalArgumentException {
        return hash(input.getBytes(Charset.forName("UTF8")));
    }


}
