package com.github.kafousis.services.impl;

import com.github.kafousis.entities.Privilege;
import com.github.kafousis.enums.PrivilegeCategory;
import com.github.kafousis.repositories.PrivilegeRepository;
import com.github.kafousis.services.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Override
    public List<Privilege> getPrivileges() {
        return privilegeRepository.findAll();
    }

    @Override
    public Optional<Privilege> getPrivilegeByName(String name) {
        return privilegeRepository.findByName(name);
    }

    @Override
    public List<Privilege> getPrivilegeByCategory(PrivilegeCategory category) {
        return privilegeRepository.findByCategory(category);
    }

    @Override
    public boolean existsById(Long id) {
        return privilegeRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return privilegeRepository.existsPrivilegeByName(name);
    }
}
