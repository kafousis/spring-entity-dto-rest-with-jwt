package com.github.kafousis.services;

import com.github.kafousis.entities.Privilege;
import com.github.kafousis.enums.PrivilegeCategory;

import java.util.List;
import java.util.Optional;

public interface PrivilegeService {
    List<Privilege> getPrivileges();
    Optional<Privilege> getPrivilegeByName(String name);
    List<Privilege> getPrivilegeByCategory(PrivilegeCategory category);
    boolean existsById(Long id);
    boolean existsByName(String name);
}
