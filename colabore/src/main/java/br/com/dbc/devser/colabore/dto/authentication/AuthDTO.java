package br.com.dbc.devser.colabore.dto.authentication;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AuthDTO {
    @NotNull
    @NotEmpty
    @ApiModelProperty(value = "Email do usuário")
    private String login;

    @NotNull
    @NotEmpty
    @ApiModelProperty(value = "Senha do usuário")
    private String password;
}
