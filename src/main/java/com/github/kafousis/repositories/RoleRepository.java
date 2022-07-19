package com.github.kafousis.repositories;

import com.github.kafousis.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsRoleByName(String name);
    Optional<Role> findByName(String name);
}
