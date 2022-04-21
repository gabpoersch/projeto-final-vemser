package br.com.dbc.devser.colabore.dto.user;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String name;
    private String email;
    private String password;
}