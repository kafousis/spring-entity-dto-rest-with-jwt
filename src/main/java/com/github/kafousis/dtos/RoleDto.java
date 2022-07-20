package com.github.kafousis.dtos;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode @ToString @Accessors(chain = true)
public class RoleDto {

    private Long id;

    @NotNull(message = "Role name cannot be null.")
    private String name;

    private String description;

    @Valid
    private List<PrivilegeDto> privileges;
}
