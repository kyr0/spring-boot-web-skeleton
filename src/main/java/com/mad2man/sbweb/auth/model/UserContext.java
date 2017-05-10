package com.mad2man.sbweb.auth.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 *
 */
@Data
public class UserContext {

    private final String userId;
    private final String username;
    private final List<GrantedAuthority> authorities;

    private UserContext(String userId, String username, List<GrantedAuthority> authorities) {

        this.userId = userId;
        this.username = username;
        this.authorities = authorities;
    }

    public static UserContext create(String userId, String username, List<GrantedAuthority> authorities) {

        if (userId == null || StringUtils.isEmpty(userId)) {
            throw new IllegalArgumentException("Cannot create a UserContext for an user without id");
        }

        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("Cannot create a UserContext without an username");
        }

        if (authorities == null || authorities.isEmpty()) {
            throw new IllegalArgumentException("Cannot create a UserContext for an user without any authorities");
        }

        return new UserContext(userId, username, authorities);
    }
}
