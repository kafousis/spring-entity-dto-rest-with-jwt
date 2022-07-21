package com.github.kafousis.services;

import com.github.kafousis.entities.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> getRoles();
    Optional<Role> getRoleByName(String name);
    boolean existsById(Long id);
    boolean existsByName(String name);
}
