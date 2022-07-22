package com.github.kafousis.controllers;

import com.github.kafousis.dtos.UserDto;
import com.github.kafousis.entities.Role;
import com.github.kafousis.entities.User;
import com.github.kafousis.error_handling.ApiError;
import com.github.kafousis.error_handling.ErrorMessages;
import com.github.kafousis.services.RoleService;
import com.github.kafousis.services.UserService;
import com.github.kafousis.utils.StringUtils;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @Operation(summary = "Get all users")
    public ResponseEntity<?> getUsers(){

        List<User> users = userService.getUsers();

        List<UserDto> userDtos = users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/role={role}")
    @Operation(summary = "Get users that have a specific role")
    public ResponseEntity<?> getUserByRole(@PathVariable(value = "role") @NotBlank final String role){

        Optional<Role> roleByName = roleService.getRoleByName(role);
        if (roleByName.isPresent()){

            List<User> users = userService.getUsersByRole(roleByName.get());

            List<UserDto> userDtos = users.stream()
                    .map(user -> modelMapper.map(user, UserDto.class))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(userDtos, HttpStatus.OK);

        }else{
            final ApiError error = new ApiError(HttpStatus.NOT_FOUND,
                    ErrorMessages.API_ERROR_MESSAGE_TITLE,
                    ErrorMessages.ROLE_NOT_EXISTS);

            return new ResponseEntity<>(error, error.getStatus());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user with specific id")
    public ResponseEntity<?> getUser(
            @PathVariable(value = "id") @Min(1) final Long id){

        Optional<User> userById = userService.getUserById(id);

        if (userById.isPresent()){
            return new ResponseEntity<>(modelMapper.map(userById.get(), UserDto.class), HttpStatus.OK);
        }else{

            final ApiError error = new ApiError(HttpStatus.NOT_FOUND,
                    ErrorMessages.API_ERROR_MESSAGE_TITLE,
                    ErrorMessages.ERROR_USER_NOT_FOUND);

            return new ResponseEntity<>(error, error.getStatus());
        }
    }

    @GetMapping(value = "/username={username}")
    @Operation(summary = "Get user with specific username")
    public ResponseEntity<?> getUser(
            @PathVariable(value = "username") @NotBlank final String username){

        Optional<User> userByUsername = userService.getUserByUsername(username);

        if (userByUsername.isPresent()){
            return new ResponseEntity<>(modelMapper.map(userByUsername.get(), UserDto.class), HttpStatus.OK);
        }else{

            final ApiError error = new ApiError(HttpStatus.NOT_FOUND,
                    ErrorMessages.API_ERROR_MESSAGE_TITLE,
                    ErrorMessages.ERROR_USER_NOT_FOUND);

            return new ResponseEntity<>(error, error.getStatus());
        }
    }

    @PostMapping()
    @Operation(summary = "Create new user")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDto userDto) {

        Long roleId = userDto.getRole().getId();
        if (roleId != null && roleService.existsById(roleId)){

            Optional<User> userByUsername = userService.getUserByUsername(userDto.getUsername());

            if (userByUsername.isPresent()){

                final ApiError error = new ApiError(HttpStatus.CONFLICT,
                        ErrorMessages.API_ERROR_MESSAGE_TITLE,
                        ErrorMessages.ERROR_USERNAME_ALREADY_EXISTS);

                return new ResponseEntity<>(error, error.getStatus());

            }else {

                if (StringUtils.empty(userDto.getPassword())){

                    final ApiError error = new ApiError(HttpStatus.CONFLICT,
                            ErrorMessages.API_ERROR_MESSAGE_TITLE,
                            ErrorMessages.EMPTY_PASSWORD);

                    return new ResponseEntity<>(error, error.getStatus());

                }else{
                    userService.createUser(modelMapper.map(userDto, User.class));
                    return new ResponseEntity<>(HttpStatus.CREATED);
                }
            }

        }else{

            final ApiError error = new ApiError(HttpStatus.NOT_FOUND,
                    ErrorMessages.API_ERROR_MESSAGE_TITLE,
                    ErrorMessages.ROLE_NOT_EXISTS);

            return new ResponseEntity<>(error, error.getStatus());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing user")
    public ResponseEntity<?> updateUser(
            @RequestBody @Valid UserDto userDto,
            @PathVariable @Min(1) Long id){

        Long roleId = userDto.getRole().getId();
        if (roleId != null && roleService.existsById(roleId)){

            Optional<User> userById = userService.getUserById(id);

            if (userById.isPresent()){

                User storedUser = userById.get();
                User updatedUser = modelMapper.map(userDto, User.class);

                Optional<User> userByUsername = userService.getUserByUsername(userDto.getUsername());

                if (userByUsername.isPresent()){
                    if (storedUser.getId().equals(userByUsername.get().getId())){
                        userService.updateUser(storedUser, updatedUser);
                        return new ResponseEntity<>(HttpStatus.OK);

                    }else{
                        final ApiError error = new ApiError(HttpStatus.CONFLICT,
                                ErrorMessages.API_ERROR_MESSAGE_TITLE,
                                ErrorMessages.ERROR_USERNAME_ALREADY_EXISTS);

                        return new ResponseEntity<>(error, error.getStatus());
                    }

                }else{
                    userService.updateUser(storedUser, updatedUser);
                    return new ResponseEntity<>(HttpStatus.OK);
                }

            }else{
                final ApiError error = new ApiError(HttpStatus.NOT_FOUND,
                        ErrorMessages.API_ERROR_MESSAGE_TITLE,
                        ErrorMessages.ERROR_USER_NOT_FOUND);

                return new ResponseEntity<>(error, error.getStatus());
            }

        }else{
            final ApiError error = new ApiError(HttpStatus.NOT_FOUND,
                    ErrorMessages.API_ERROR_MESSAGE_TITLE,
                    ErrorMessages.ROLE_NOT_EXISTS);

            return new ResponseEntity<>(error, error.getStatus());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete existing user")
    public ResponseEntity<?> deleteUser(@PathVariable @Min(1) Long id){

        Optional<User> userById = userService.getUserById(id);

        if (userById.isPresent()){
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{

            final ApiError error = new ApiError(HttpStatus.NOT_FOUND,
                    ErrorMessages.API_ERROR_MESSAGE_TITLE,
                    ErrorMessages.ERROR_USER_NOT_FOUND);

            return new ResponseEntity<>(error, error.getStatus());
        }
    }

    @Hidden
    @PostMapping("/{id}/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                         @PathVariable @Min(1) Long id){

        Optional<User> userById = userService.getUserById(id);

        if (userById.isPresent()){

            Optional<String> fileExtension = StringUtils.getFileExtension(file.getOriginalFilename());

            if (fileExtension.isPresent() &&
                    (fileExtension.get().equalsIgnoreCase("jpg")
                            || fileExtension.get().equalsIgnoreCase("jpeg"))){

                try {
                    userService.storeImage(file, userById.get());
                    return new ResponseEntity<>(HttpStatus.OK);

                } catch (IOException e) {
                    final ApiError error = new ApiError(HttpStatus.EXPECTATION_FAILED,
                            ErrorMessages.API_ERROR_MESSAGE_TITLE,
                            ErrorMessages.ERROR_FILE_UPLOAD_FAILED);

                    return new ResponseEntity<>(error, error.getStatus());
                }

            } else {
                final ApiError error = new ApiError(HttpStatus.BAD_REQUEST,
                        ErrorMessages.API_ERROR_MESSAGE_TITLE,
                        ErrorMessages.ERROR_INVALID_FILE_FORMAT);

                return new ResponseEntity<>(error, error.getStatus());
            }

        }else{

            final ApiError error = new ApiError(HttpStatus.NOT_FOUND,
                    ErrorMessages.API_ERROR_MESSAGE_TITLE,
                    ErrorMessages.ERROR_USER_NOT_FOUND);

            return new ResponseEntity<>(error, error.getStatus());
        }
    }

    @Hidden
    @GetMapping(value = "/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] showImage(@PathVariable @Min(1) Long id) {

        Optional<User> userById = userService.getUserById(id);
        if (userById.isPresent()){

            try {
                String image = userById.get().getImage();

                if (!StringUtils.empty(image)){
                    Resource imageResource = userService.loadImage(image);
                    return IOUtils.toByteArray(imageResource.getInputStream());
                }
                return new byte[0];
            }catch (IOException e) {
                return new byte[0];
            }

        }else{
            return new byte[0];
        }
    }

    @Hidden
    @GetMapping("/{id}/image/download")
    public ResponseEntity<?> downloadImage(@PathVariable @Min(1) Long id) {

        Optional<User> userById = userService.getUserById(id);

        if (userById.isPresent()){

            try {
                Resource imageResource = userService.loadImage(userById.get().getImage());

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageResource.getFilename() + "\"")
                        .body(imageResource);

            } catch (IOException e) {
                final ApiError error = new ApiError(HttpStatus.EXPECTATION_FAILED,
                        ErrorMessages.API_ERROR_MESSAGE_TITLE,
                        ErrorMessages.ERROR_FILE_DOWNLOAD_FAILED);

                return new ResponseEntity<>(error, error.getStatus());
            }

        }else{

            final ApiError error = new ApiError(HttpStatus.NOT_FOUND,
                    ErrorMessages.API_ERROR_MESSAGE_TITLE,
                    ErrorMessages.ERROR_USER_NOT_FOUND);

            return new ResponseEntity<>(error, error.getStatus());
        }
    }

    @DeleteMapping("/{id}/image")
    @Operation(summary = "Delete user image")
    public ResponseEntity<?> deleteImage(@PathVariable @Min(1) Long id){

        Optional<User> userById = userService.getUserById(id);

        if (userById.isPresent()){

            try {
                userService.deleteImage(userById.get());
                return new ResponseEntity<>(HttpStatus.OK);

            } catch (IOException e) {
                final ApiError error = new ApiError(HttpStatus.EXPECTATION_FAILED,
                        ErrorMessages.API_ERROR_MESSAGE_TITLE,
                        ErrorMessages.ERROR_FILE_DELETE_FAILED);

                return new ResponseEntity<>(error, error.getStatus());

            }

        }else{

            final ApiError error = new ApiError(HttpStatus.NOT_FOUND,
                    ErrorMessages.API_ERROR_MESSAGE_TITLE,
                    ErrorMessages.ERROR_USER_NOT_FOUND);

            return new ResponseEntity<>(error, error.getStatus());
        }
    }
}
