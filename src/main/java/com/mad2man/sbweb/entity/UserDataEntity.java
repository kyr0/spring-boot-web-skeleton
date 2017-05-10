package com.mad2man.sbweb.entity;

import com.mad2man.sbweb.entity.auditable.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * User data.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Table(name = "skeleton_userdata")
public class UserDataEntity extends SpringSecurityAuditorAware implements Serializable {

    private static final long serialVersionUID = 4720418191378718247L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String user_id;

    // example for user data extensibility
    @Size(max = 250)
    @Column(name = "address", length = 250)
    private String address;

    // implement your custom user data fields here
}
