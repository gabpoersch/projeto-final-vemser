package br.com.dbc.devser.colabore.dto.authentication;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthDTO {

    @NotBlank(message = "Login field cannot be null")
    @ApiModelProperty(value = "Email do usuário")
    private String login;

    @NotBlank(message = "You must enter the password")
    @ApiModelProperty(value = "Senha do usuário")
    private String password;

}
