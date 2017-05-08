package com.mad2man.sbweb.user.aggregate;

import com.mad2man.sbweb.entity.RoleEntity;
import com.mad2man.sbweb.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Aggregate of a UserEntity and his Roles
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagedUserAggregate {

    private Long id;

    @Pattern(regexp = UserEntity.USERNAME_REGEX)
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

    public ManagedUserAggregate(UserEntity userEntity) {

        this(userEntity.getId(), userEntity.getUsername(), userEntity.getFirstName(), userEntity.getLastName(),
            userEntity.getEmail(), userEntity.isActivated(),
            userEntity.getRoles().stream().map(RoleEntity::getName)
                .collect(Collectors.toSet()));
    }
}