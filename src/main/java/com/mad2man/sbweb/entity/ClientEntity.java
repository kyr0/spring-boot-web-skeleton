package com.mad2man.sbweb.entity;

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

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Client base entity.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "skeleton_client")
public class ClientEntity extends AbstractAuditingEntity implements Serializable {

    public static final String CLIENT_REGEX = "^[_'.@A-Za-z0-9-]*$";

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @NotNull
    @Pattern(regexp = CLIENT_REGEX)
    @Size(min = 1, max = 50)
    @Column(name = "name", length = 50, unique = true, nullable = false)
    private String name;

    @Email
    @Size(min = 5, max = 100)
    @Column(length = 100)
    private String email;

    @NotNull
    @Column(nullable = false)
    private boolean activated = false;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    @Fetch(value = FetchMode.JOIN)
    private ClientDataEntity clientData = null;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "skeleton_user_client",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "client_id", referencedColumnName = "id")})
    private Set<UserEntity> users = new HashSet<>();
}
