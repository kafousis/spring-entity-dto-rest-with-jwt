package com.github.kafousis.controllers;

import com.github.kafousis.dtos.PrivilegeDto;
import com.github.kafousis.entities.Privilege;
import com.github.kafousis.services.PrivilegeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/privileges", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Privileges")
@SecurityRequirement(name = "bearerAuth")
public class PrivilegeController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PrivilegeService privilegeService;

    @GetMapping
    @Operation(summary = "Get all privileges")
    public ResponseEntity<?> getPrivileges(){

        List<Privilege> privileges = privilegeService.getPrivileges();

        List<PrivilegeDto> privilegeDtos = privileges.stream()
                .map(privilege -> modelMapper.map(privilege, PrivilegeDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(privilegeDtos, HttpStatus.OK);
    }
}
