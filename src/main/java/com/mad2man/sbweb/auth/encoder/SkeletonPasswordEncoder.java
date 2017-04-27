package com.mad2man.sbweb.auth.encoder;

import com.mad2man.sbweb.util.hash.SHA512;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by Mansi on 27.04.2017.
 */
public class SkeletonPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        //TODO: check password hash salt already added
        return new SHA512().hashAsString(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
