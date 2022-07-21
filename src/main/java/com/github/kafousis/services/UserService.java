package com.github.kafousis.services;

import com.github.kafousis.entities.Role;
import com.github.kafousis.entities.User;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getUsers();
    List<User> getUsersByRole(Role role);

    Optional<User> getUserById(Long userId);
    Optional<User> getUserByUsername(String username);
    boolean existsById(Long id);

    void createUser(User user);
    void updateUser(User storedUser, User updatedUser);
    void deleteUser(Long userId);

    Resource loadImage(String imageName) throws IOException;
    void storeImage(MultipartFile file, User user) throws IOException;
    void deleteImage(User user) throws IOException;
}
