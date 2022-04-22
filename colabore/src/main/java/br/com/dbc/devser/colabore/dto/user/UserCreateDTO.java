package br.com.dbc.devser.colabore.dto.user;

import br.com.dbc.devser.colabore.entity.RoleEntity;
import lombok.Data;

@Data
public class UserCreateDTO {
    private String name;
    private String email;
    private String password;
    private String profilePhoto;
}