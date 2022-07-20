package com.github.kafousis.dtos;

import com.github.kafousis.enums.PrivilegeCategory;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode @ToString @Accessors(chain = true)
public class PrivilegeDto {

    private Long id;

    @NotNull(message = "Authority name cannot be null.")
    private String name;

    @NotNull(message = "Authority category cannot be null.")
    private PrivilegeCategory category;
}
