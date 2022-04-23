package br.com.dbc.devser.colabore.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

    @ApiModelProperty(value = "Id do usuário")
    private Integer userId;

    @ApiModelProperty(value = "Email do usuário")
    private String email;

    @ApiModelProperty(value = "Foto de perfil")
    private String profilePhoto;
}