package com.github.kafousis;

import com.github.kafousis.repositories.PrivilegeRepository;
import com.github.kafousis.repositories.RoleRepository;
import com.github.kafousis.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringEntityDtoRestWithJwtApplication implements CommandLineRunner {

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringEntityDtoRestWithJwtApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
