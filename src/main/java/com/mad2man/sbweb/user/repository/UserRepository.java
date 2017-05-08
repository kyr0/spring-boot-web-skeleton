package com.mad2man.sbweb.user.repository;

import com.mad2man.sbweb.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the UserEntity domain.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findOneByActivationKey(String activationKey);

    Optional<UserEntity> findOneByResetKey(String resetKey);

    Optional<UserEntity> findOneByEmail(String email);

    Optional<UserEntity> findOneByUsername(String username);

    @EntityGraph(attributePaths = "roles")
    UserEntity findOneWithRolesById(Long id);

    @EntityGraph(attributePaths = "roles")
    Optional<UserEntity> findOneWithRolesByUsername(String username);

    Page<UserEntity> findAllByUsernameNot(Pageable pageable, String username);
}
