package com.github.kafousis.entities;

import com.github.kafousis.enums.PrivilegeCategory;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Set;

@Entity @Table(name = "privileges")
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode @ToString @Accessors(chain = true)
@SequenceGenerator(name = "privilege_generator", sequenceName = "privilege_id_seq", allocationSize = 1)
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "privilege_generator")
    private Long id;

    private String name;
    private PrivilegeCategory category;

    @ManyToMany(mappedBy = "privileges")
    private Set<Role> roles;
}
