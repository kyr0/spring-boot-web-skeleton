package com.mad2man.sbweb.auth.model;

import com.mad2man.sbweb.common.Error;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 *
 */
public class UserContext {

    private final String username;
    private final List<GrantedAuthority> authorities;

    private UserContext(String username, List<GrantedAuthority> authorities) {

        this.username = username;
        this.authorities = authorities;
    }

    public static UserContext create(String username, List<GrantedAuthority> authorities) {

        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("Cannot create a UserContext without an username");
        }

        if (authorities == null || authorities.isEmpty()) {
            throw new IllegalArgumentException("Cannot create a UserContext for an user without any authorities");
        }

        return new UserContext(username, authorities);
    }

    public String getUsername() {
        return username;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
