package br.com.dbc.devser.colabore.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "roles_colabore")
public class RoleEntity implements Serializable, GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_roles")
    @SequenceGenerator(name = "sequence_roles", sequenceName = "sequence_roles", allocationSize = 1)
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToOne
    private UserEntity users;

    @Override
    public String getAuthority() {
        return name;
    }
}
