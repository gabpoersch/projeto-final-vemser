package br.com.dbc.devser.colabore.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "roles")
public class RoleEntity implements Serializable, GrantedAuthority {

    @Id
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "name")
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }

}
