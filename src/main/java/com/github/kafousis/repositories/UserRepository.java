package com.github.kafousis.repositories;

import com.github.kafousis.entities.Role;
import com.github.kafousis.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByUsername(String username);
    boolean existsUserByEmail(String email);
    List<User> findByRole(Role role);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    // we use JOIN FETCH to get user-roles-authorities in a single transaction
    @Query(value = "SELECT u FROM User u JOIN FETCH u.role r JOIN FETCH r.privileges p WHERE u.username = ?1")
    Optional<User> findByUsernameJoinFetch(String username);
}
