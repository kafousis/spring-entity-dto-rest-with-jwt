package com.github.kafousis;

import com.github.kafousis.entities.Privilege;
import com.github.kafousis.entities.Role;
import com.github.kafousis.entities.User;
import com.github.kafousis.enums.PrivilegeCategory;
import com.github.kafousis.repositories.PrivilegeRepository;
import com.github.kafousis.repositories.RoleRepository;
import com.github.kafousis.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@SpringBootApplication
public class SpringEntityDtoRestWithJwtApplication implements CommandLineRunner {

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringEntityDtoRestWithJwtApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Privilege createPrivileges = new Privilege().setName("PRIVILEGES_CREATE").setCategory(PrivilegeCategory.PRIVILEGES);
		Privilege readPrivileges = new Privilege().setName("PRIVILEGES_READ").setCategory(PrivilegeCategory.PRIVILEGES);
		Privilege updatePrivileges = new Privilege().setName("PRIVILEGES_UPDATE").setCategory(PrivilegeCategory.PRIVILEGES);
		Privilege deletePrivileges = new Privilege().setName("PRIVILEGES_DELETE").setCategory(PrivilegeCategory.PRIVILEGES);
		List<Privilege> privilegePrivileges = Arrays.asList(createPrivileges, readPrivileges, updatePrivileges, deletePrivileges);
		privilegeRepository.saveAll(privilegePrivileges);

		Privilege createRoles = new Privilege().setName("ROLES_CREATE").setCategory(PrivilegeCategory.ROLES);
		Privilege readRoles = new Privilege().setName("ROLES_READ").setCategory(PrivilegeCategory.ROLES);
		Privilege updateRoles = new Privilege().setName("ROLES_UPDATE").setCategory(PrivilegeCategory.ROLES);
		Privilege deleteRoles = new Privilege().setName("ROLES_DELETE").setCategory(PrivilegeCategory.ROLES);
		List<Privilege> rolesPrivileges = Arrays.asList(createRoles, readRoles, updateRoles, deleteRoles);
		privilegeRepository.saveAll(rolesPrivileges);

		Privilege createUsers = new Privilege().setName("USERS_CREATE").setCategory(PrivilegeCategory.USERS);
		Privilege readUsers = new Privilege().setName("USERS_READ").setCategory(PrivilegeCategory.USERS);
		Privilege updateUsers = new Privilege().setName("USERS_UPDATE").setCategory(PrivilegeCategory.USERS);
		Privilege deleteUsers = new Privilege().setName("USERS_DELETE").setCategory(PrivilegeCategory.USERS);
		List<Privilege> usersPrivileges = Arrays.asList(createUsers, readUsers, updateUsers, deleteUsers);
		privilegeRepository.saveAll(usersPrivileges);

		// ---

		Role adminRole = new Role().setName("ADMIN").setDescription("Administrator Account");
		adminRole.setPrivileges(new HashSet<Privilege>(Arrays.asList(
				createPrivileges, readPrivileges, updatePrivileges, deletePrivileges,
				createRoles, readRoles, updateRoles, deleteRoles,
				createUsers, readUsers, updateUsers, deleteUsers)));

		Role managerRole = new Role().setName("MANAGER").setDescription("Manager Account");
		managerRole.setPrivileges(new HashSet<Privilege>(Arrays.asList(readPrivileges,
				createRoles, readRoles, updateRoles, deleteRoles,
				createUsers, readUsers, updateUsers, deleteUsers)));

		Role userRole = new Role().setName("USER").setDescription("User Account");
		userRole.setPrivileges(new HashSet<Privilege>(Arrays.asList(
				createUsers, readUsers, updateUsers, deleteUsers)));

		List<Role> roles = Arrays.asList(adminRole, managerRole, userRole);
		roleRepository.saveAll(roles);

		// ---

		User admin = new User().setUsername("admin")
				.setPassword(passwordEncoder.encode("admin"))
				.setRole(adminRole).setEnabled(true)
				.setFirstName("Giannis")
				.setLastName("Kafousis")
				.setEmail("admin@spring.io")
				.setPhone("+306945000000")
				.setCreatedAt(LocalDateTime.now())
				.setUpdatedAt(LocalDateTime.now());

		User manager = new User().setUsername("manager")
				.setPassword(passwordEncoder.encode("manager"))
				.setRole(managerRole).setEnabled(true)
				.setFirstName("John")
				.setLastName("Doe")
				.setEmail("manager@spring.io")
				.setPhone("+306945000001")
				.setCreatedAt(LocalDateTime.now())
				.setUpdatedAt(LocalDateTime.now());

		User user = new User().setUsername("user")
				.setPassword(passwordEncoder.encode("user"))
				.setRole(userRole).setEnabled(true)
				.setFirstName("Fred")
				.setLastName("Bloggs")
				.setEmail("user@spring.io")
				.setPhone("+306945000002")
				.setCreatedAt(LocalDateTime.now())
				.setUpdatedAt(LocalDateTime.now());

		List<User> users = Arrays.asList(admin, manager, user);
		userRepository.saveAll(users);
	}
}
