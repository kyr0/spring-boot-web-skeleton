package com.mad2man.sbweb.user.service.dto;

import com.mad2man.sbweb.entity.Role;
import com.mad2man.sbweb.entity.User;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user with roles.
 */
public class ManagedUserDTO {

    private Long id;

    @Pattern(regexp = User.USERNAME_REGEX)
    @Size(min = 1, max = 50)
    private String username;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    private boolean activated = false;

    private Set<String> roles;

    public ManagedUserDTO() {
        // Empty constructor needed for MapStruct.
    }

    public ManagedUserDTO(User user) {
        this(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(),
            user.getEmail(), user.getActivated(),
            user.getRoles().stream().map(Role::getName)
                .collect(Collectors.toSet()));
    }

    public ManagedUserDTO(Long id, String username, String firstName, String lastName,
                          String email, boolean activated,
                          Set<String> roles) {

        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.activated = activated;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActivated() {
        return activated;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "ManagedUserDTO{" +
            "username='" + username + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", activated=" + activated +
            ", roles=" + roles +
            "}";
    }
}
