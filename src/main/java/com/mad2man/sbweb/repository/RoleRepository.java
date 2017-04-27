package com.mad2man.sbweb.repository;

import com.mad2man.sbweb.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Role domain.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
}
