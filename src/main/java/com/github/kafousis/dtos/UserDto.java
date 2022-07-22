package com.github.kafousis.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


/**
 * @JsonInclude(value = JsonInclude.Include.NON_NULL)
 *  ignore null or what is considered to be empty at serialization of POJO to JSON
 *
 * @JsonIgnoreProperties(ignoreUnknown = true)
 * ignore JSON properties that your POJO does not contain at deserialization
 *
 * Lombok Accessors
 * https://www.baeldung.com/lombok-accessors
 */

@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode @ToString @Accessors(chain = true)
public class UserDto {

    private Long id;

    @NotNull(message = "Username cannot be null.")
    private String username;

    // Password is accessible only during deserialization
    // and won’t be serialized to JSON.
    // password can be null when user is updated

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    //@NotNull(message = "Password cannot be null.")
    private String password;

    @JsonProperty("first_name")
    @NotNull(message = "First name cannot be null.")
    private String firstName;

    @JsonProperty("last_name")
    @NotNull(message = "Last name cannot be null.")
    private String lastName;

    @Email(message = "Email address is not valid.")
    private String email;

    @NotNull(message = "Phone cannot be null.")
    private String phone;

    private String image;
    private boolean enabled;

    @JsonProperty("created_at")
    @Schema(type = "string", example = "2021-09-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @Schema(type = "string", example = "2021-09-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // If @Valid annotation is used here
    // we have to send role obj with corresponding privileges
    // every time we want to create or update a User obj
    // without @Valid annotation we have to send only role_id

    //@Valid
    @NotNull(message = "User must have a Role.")
    private RoleDto role;
}
