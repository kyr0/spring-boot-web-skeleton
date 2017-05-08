package com.mad2man.sbweb.auth.api.request;

import lombok.Data;

/**
 * Model intended to be used for request based authentication.
 */
@Data
public class LoginRequest {

    private String username;
    private String password;
}
