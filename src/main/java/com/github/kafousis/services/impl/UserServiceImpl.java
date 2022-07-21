package com.github.kafousis.services.impl;

import com.github.kafousis.entities.Role;
import com.github.kafousis.entities.User;
import com.github.kafousis.repositories.UserRepository;
import com.github.kafousis.services.UserService;
import com.github.kafousis.utils.Constants;
import com.github.kafousis.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service @Slf4j
public class UserServiceImpl implements UserService {

    private final Path usersImagesDir = Paths.get(Constants.USER_IMAGES_DIRECTORY);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC,"id"));
    }

    @Override
    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public void createUser(User user) {
        String passwordPlain = user.getPassword();
        String passwordEncrypted = passwordEncoder.encode(passwordPlain);
        user.setPassword(passwordEncrypted);
        user.setCreatedAt(LocalDateTime.now());
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void updateUser(User storedUser, User updatedUser) {

        // ensure that the correct user is updated
        updatedUser.setId(storedUser.getId());
        updatedUser.setCreatedAt(storedUser.getCreatedAt());

        if (StringUtils.empty(updatedUser.getPassword())) {
            // updatedUser has empty password means that password was not updated
            updatedUser.setPassword(storedUser.getPassword());
        }else{
            // password is updated and needs to be encrypted
            log.info("Password updated");
            String passwordPlain = updatedUser.getPassword();
            String passwordEncrypted = passwordEncoder.encode(passwordPlain);
            updatedUser.setPassword(passwordEncrypted);
        }
        userRepository.save(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Resource loadImage(String imageName) throws IOException {

        String fileName = StringUtils.empty(imageName) ? Constants.DEFAULT_PROFILE_PICTURE : imageName;

        Path filePath = this.usersImagesDir.resolve(fileName);
        Resource imageFile = new UrlResource(filePath.toUri());

        if (imageFile.exists() && imageFile.isReadable()){
            return imageFile;
        }else{
            throw new RuntimeException("Could not read the file!");
        }
    }

    @Override
    public void storeImage(MultipartFile file, User user) throws IOException {

        String fileName = user.getId() +".jpg";

        Path filePath = this.usersImagesDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        user.setImage(fileName);
        userRepository.save(user);
    }

    @Override
    public void deleteImage(User user) throws IOException {

        String fileName = user.getImage();

        Path filePath = this.usersImagesDir.resolve(fileName);
        Files.delete(filePath);

        user.setImage("");
        userRepository.save(user);
    }
}
