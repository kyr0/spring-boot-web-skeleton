package com.mad2man.sbweb.user.viewmodel;

import com.mad2man.sbweb.user.aggregate.ManagedUserAggregate;

import javax.validation.constraints.Size;
import java.util.Set;

/**
 * View Model extending the ManagedUserAggregate, which is meant to be used in REST responses (view).
 */
public class UserViewModel extends ManagedUserAggregate {

    public static final int PASSWORD_MIN_LENGTH = 4;
    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    public UserViewModel(String id, String username, String password, String firstName, String lastName,
                         String email, boolean activated, UserDataViewModel dataViewModel, Set<String> roles, Set<String> clients) {

        super(id, username, firstName, lastName, email, activated, dataViewModel, roles, clients);

        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {

        return "UserViewModel{" +
            "} " + super.toString();
    }
}
