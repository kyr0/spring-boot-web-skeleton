package com.mad2man.sbweb.rest.model;

import com.mad2man.sbweb.service.dto.UserDTO;

import javax.validation.constraints.Size;
import java.util.Set;

/**
 * View Model extending the UserDTO, which is meant to be used in REST responses (view).
 */
public class UserModel extends UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    public UserModel() {
        // Empty constructor needed for Jackson.
    }

    public UserModel(Long id, String login, String password, String firstName, String lastName,
                     String email, boolean activated, Set<String> roles) {

        super(id, login, firstName, lastName, email, activated, roles);

        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "UserModel{" +
            "} " + super.toString();
    }
}
