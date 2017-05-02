package com.mad2man.sbweb.user.repository;

import com.mad2man.sbweb.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the User domain.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByActivationKey(String activationKey);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByUsername(String username);

    @EntityGraph(attributePaths = "roles")
    User findOneWithRolesById(Long id);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findOneWithRolesByUsername(String username);

    Page<User> findAllByUsernameNot(Pageable pageable, String username);
}
