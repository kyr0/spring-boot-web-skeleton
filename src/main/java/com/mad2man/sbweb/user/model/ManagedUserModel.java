package com.mad2man.sbweb.user.model;

import com.mad2man.sbweb.user.service.dto.ManagedUserDTO;

import javax.validation.constraints.Size;
import java.util.Set;

/**
 * View Model extending the ManagedUserDTO, which is meant to be used in REST responses (view).
 */
public class ManagedUserModel extends ManagedUserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    public ManagedUserModel() {
        // Empty constructor needed for Jackson.
    }

    public ManagedUserModel(Long id, String username, String password, String firstName, String lastName,
                            String email, boolean activated, Set<String> roles) {

        super(id, username, firstName, lastName, email, activated, roles);

        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "ManagedUserModel{" +
            "} " + super.toString();
    }
}
