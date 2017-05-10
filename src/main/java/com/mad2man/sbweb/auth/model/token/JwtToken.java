package com.mad2man.sbweb.auth.model.token;

import java.util.Date;

public interface JwtToken {

    String HTTP_AUTHORIZATION_HEADER_NAME = "Authorization";
    String HTTP_AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    String getToken();
    Date getExpiration();
}
