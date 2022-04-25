package br.com.dbc.devser.colabore.dto.authentication;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthDTO {

    @NotBlank
    @ApiModelProperty(value = "Email do usuário")
    private String login;

    @NotBlank
    @ApiModelProperty(value = "Senha do usuário")
    private String password;

}
