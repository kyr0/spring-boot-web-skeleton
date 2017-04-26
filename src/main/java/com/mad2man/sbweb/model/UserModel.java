package com.mad2man.sbweb.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Mansi on 26.04.2017.
 */
public class UserModel {
    private long id;
    private String email;
    private boolean enabled;
    private boolean authenticated;
    private State state = State.UNKNOWN;


    public final void setStatus(State status) {
        this.state = status;

    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    @ApiModelProperty(required = true)
    public long getId() {

        return id;
    }

    @ApiModelProperty(required = true)
    public String getEmail() {
        return email;
    }

    @ApiModelProperty(required = true)
    public boolean isActive() {
        return enabled;
    }

    @ApiModelProperty(value = "User authenticated already the email address", required = true)
    public boolean isAuthenticated() {
        return authenticated;
    }

    @ApiModelProperty(value = "The final request state", required = true)
    public final State getStatus() {
        return state;
    }
}
