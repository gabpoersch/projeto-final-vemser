package br.com.dbc.devser.colabore.dto.user;

import lombok.Data;

@Data
public class UserCreateDTO {
    private String name;
    private String email;
    private String password;
    private String profilePhoto;
}