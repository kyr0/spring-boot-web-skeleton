package com.mad2man.sbweb.role.repository;

import com.mad2man.sbweb.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the RoleEntity domain.
 */
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}