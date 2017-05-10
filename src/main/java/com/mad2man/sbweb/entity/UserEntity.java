package com.mad2man.sbweb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mad2man.sbweb.entity.auditable.AbstractAuditingEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A user.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "skeleton_user")
public class UserEntity extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = -2070015167954456662L;

    public static final String USERNAME_REGEX = "^[_'.@A-Za-z0-9-]*$";
    public static final String SYSTEM_ACCOUNT_NAME = "system";

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "skeleton_user_client",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "client_id", referencedColumnName = "id")})
    private Set<ClientEntity> clients = new HashSet<>();

    @NotNull
    @Pattern(regexp = USERNAME_REGEX)
    @Size(min = 1, max = 50)
    @Column(name = "username", length = 50, unique = true, nullable = false)
    private String username;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password",length = 60)
    private String password;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    @Column(length = 100)
    private String email;

    @NotNull
    @Column(nullable = false)
    private boolean activated = false;

    @JsonIgnore
    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    private String activationKey;

    @JsonIgnore
    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    private String resetKey;

    @JsonIgnore
    @Column(name = "reset_date")
    private ZonedDateTime resetDate = null;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "skeleton_user_role",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    @Fetch(value = FetchMode.JOIN)
    private Set<RoleEntity> roles = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    @Fetch(value = FetchMode.JOIN)
    private UserDataEntity userData = null;

    public List<GrantedAuthority> getAuthorities() {

        // fetch role authorities
        List<GrantedAuthority> roleAuthorities = this.getRoles().stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
            .collect(Collectors.toList());

        // fetch permission authorities
        List<GrantedAuthority> permissionAuthorities = this.getRoles().stream()
            .map(roleEntity -> roleEntity.getPermissions().stream().map(
                permissionEntity -> new SimpleGrantedAuthority(permissionEntity.getName())
                ).collect(Collectors.toList())
            ).flatMap(Collection::stream).collect(Collectors.toList());

        // merge streams
        return Stream.concat(roleAuthorities.stream(), permissionAuthorities.stream())
            .collect(Collectors.toList());
    }
}
